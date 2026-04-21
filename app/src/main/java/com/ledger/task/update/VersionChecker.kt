package com.ledger.task.update

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.ledger.task.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * 版本检查器
 * 独立模块，不访问应用内数据
 */
class VersionChecker(private val context: Context) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
        coerceInputValues = true
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * 检查更新（双源：Gitee + GitHub）
     */
    suspend fun checkUpdate(): Result<ReleaseInfo?> = withContext(Dispatchers.IO) {
        // 先尝试 Gitee（国内访问更快）
        val giteeResult = checkGitee()
        if (giteeResult.isSuccess) {
            // 成功（包括 null = 已是最新版本）
            return@withContext giteeResult
        }

        // Gitee 失败，尝试 GitHub
        val githubResult = checkGitHub()
        if (githubResult.isSuccess) {
            return@withContext githubResult
        }

        // 两个源都失败，返回更详细的错误信息
        val giteeError = giteeResult.exceptionOrNull()?.message ?: "未知错误"
        val githubError = githubResult.exceptionOrNull()?.message ?: "未知错误"
        Result.failure(Exception("检查更新失败\nGitee: $giteeError\nGitHub: $githubError"))
    }

    private fun checkGitHub(): Result<ReleaseInfo?> {
        return try {
            val request = Request.Builder()
                .url(GITHUB_API_URL)
                .header("Accept", "application/vnd.github.v3+json")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return Result.failure(Exception("GitHub API 请求失败: ${response.code}"))
                }

                val body = response.body?.string() ?: return Result.failure(Exception("响应体为空"))
                val release = json.decodeFromString<GitHubRelease>(body)
                val info = parseGitHubRelease(release)

                if (isNewerVersion(info.version)) {
                    Result.success(info)
                } else {
                    Result.success(null) // 已是最新版本
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun checkGitee(): Result<ReleaseInfo?> {
        return try {
            Log.d(TAG, "开始检查 Gitee 更新: $GITEE_API_URL")
            val request = Request.Builder()
                .url(GITEE_API_URL)
                .build()

            client.newCall(request).execute().use { response ->
                Log.d(TAG, "Gitee 响应码: ${response.code}")
                if (!response.isSuccessful) {
                    return Result.failure(Exception("Gitee API 请求失败: ${response.code}"))
                }

                val body = response.body?.string() ?: return Result.failure(Exception("响应体为空"))
                Log.d(TAG, "Gitee 响应体长度: ${body.length}")
                val release = json.decodeFromString<GiteeRelease>(body)
                Log.d(TAG, "Gitee 解析成功: version=${release.tagName}, assets=${release.assets.size}")
                val info = parseGiteeRelease(release)
                Log.d(TAG, "Gitee 版本信息: version=${info.version}, apkUrl=${info.apkUrl}")

                if (isNewerVersion(info.version)) {
                    Result.success(info)
                } else {
                    Result.success(null) // 已是最新版本
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gitee 检查失败", e)
            Result.failure(e)
        }
    }

    private fun parseGitHubRelease(release: GitHubRelease): ReleaseInfo {
        val version = release.tagName.removePrefix("v")
        val apkAsset = release.assets.find { it.name.endsWith(".apk") }
        return ReleaseInfo(
            version = version,
            changelog = release.body,
            apkUrl = apkAsset?.browserDownloadUrl ?: "",
            fileSize = apkAsset?.size ?: 0
        )
    }

    private fun parseGiteeRelease(release: GiteeRelease): ReleaseInfo {
        val version = release.tagName.removePrefix("v")
        val apkAsset = release.assets.find { it.name.endsWith(".apk") }
        return ReleaseInfo(
            version = version,
            changelog = release.body,
            apkUrl = apkAsset?.browserDownloadUrl ?: "",
            fileSize = 0  // Gitee API 不返回文件大小
        )
    }

    /**
     * 比较版本号
     */
    private fun isNewerVersion(remoteVersion: String): Boolean {
        val currentVersion = BuildConfig.VERSION_NAME
        return compareVersions(remoteVersion, currentVersion) > 0
    }

    private fun compareVersions(v1: String, v2: String): Int {
        val parts1 = v1.split(".").map { it.toIntOrNull() ?: 0 }
        val parts2 = v2.split(".").map { it.toIntOrNull() ?: 0 }

        for (i in 0 until maxOf(parts1.size, parts2.size)) {
            val p1 = parts1.getOrElse(i) { 0 }
            val p2 = parts2.getOrElse(i) { 0 }
            if (p1 != p2) return p1.compareTo(p2)
        }
        return 0
    }

    /**
     * 是否应该自动检查（距离上次检查超过 24 小时）
     */
    fun shouldAutoCheck(): Boolean {
        val lastCheck = prefs.getLong(KEY_LAST_CHECK, 0)
        val now = System.currentTimeMillis()
        return (now - lastCheck) > CHECK_INTERVAL
    }

    /**
     * 记录检查时间
     */
    fun recordCheckTime() {
        prefs.edit().putLong(KEY_LAST_CHECK, System.currentTimeMillis()).apply()
    }

    /**
     * 获取上次检查时间描述
     */
    fun getLastCheckDescription(): String? {
        val lastCheck = prefs.getLong(KEY_LAST_CHECK, 0)
        if (lastCheck == 0L) return null

        val now = System.currentTimeMillis()
        val diff = now - lastCheck

        return when {
            diff < HOUR -> "${diff / MINUTE} 分钟前"
            diff < DAY -> "${diff / HOUR} 小时前"
            else -> "${diff / DAY} 天前"
        }
    }

    companion object {
        private const val TAG = "VersionChecker"
        private const val GITHUB_API_URL = "https://api.github.com/repos/l825l/TaskLedger/releases/latest"
        private const val GITEE_API_URL = "https://gitee.com/api/v5/repos/l825l/task-ledger/releases/latest"

        private const val PREFS_NAME = "update_prefs"
        private const val KEY_LAST_CHECK = "last_check_time"

        private const val MINUTE = 60_000L
        private const val HOUR = 60 * MINUTE
        private const val DAY = 24 * HOUR
        private const val CHECK_INTERVAL = 24 * HOUR // 24 小时
    }
}
