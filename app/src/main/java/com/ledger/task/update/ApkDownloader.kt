package com.ledger.task.update

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

/**
 * APK 下载管理器
 * 独立模块，下载到独立缓存目录，与应用数据隔离
 */
class ApkDownloader(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    // APK 下载到独立缓存目录，与应用数据隔离
    private val downloadDir = File(context.cacheDir, "updates")

    /**
     * 下载进度
     */
    data class DownloadProgress(
        val progress: Int,      // 0-100
        val downloaded: Long,   // 已下载字节数
        val total: Long,        // 总字节数
        val isComplete: Boolean = false,
        val file: File? = null,
        val error: String? = null
    )

    /**
     * 下载 APK
     */
    fun download(url: String, fileName: String = "update.apk"): Flow<DownloadProgress> = flow {
        // 确保下载目录存在
        if (!downloadDir.exists()) {
            downloadDir.mkdirs()
        }

        val targetFile = File(downloadDir, fileName)

        // 如果文件已存在且完整，直接返回
        if (targetFile.exists()) {
            emit(DownloadProgress(
                progress = 100,
                downloaded = targetFile.length(),
                total = targetFile.length(),
                isComplete = true,
                file = targetFile
            ))
            return@flow
        }

        try {
            val request = Request.Builder()
                .url(url)
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                emit(DownloadProgress(
                    progress = 0,
                    downloaded = 0,
                    total = 0,
                    error = "下载失败: ${response.code}"
                ))
                return@flow
            }

            val body = response.body ?: run {
                emit(DownloadProgress(
                    progress = 0,
                    downloaded = 0,
                    total = 0,
                    error = "响应体为空"
                ))
                return@flow
            }

            val contentLength = body.contentLength()
            var downloaded = 0L

            // 使用临时文件下载
            val tempFile = File(downloadDir, "$fileName.tmp")

            body.byteStream().use { input ->
                FileOutputStream(tempFile).use { output ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Int

                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        downloaded += bytesRead

                        val progress = if (contentLength > 0) {
                            ((downloaded * 100) / contentLength).toInt()
                        } else {
                            -1 // 未知总大小
                        }

                        emit(DownloadProgress(
                            progress = progress,
                            downloaded = downloaded,
                            total = contentLength
                        ))
                    }
                }
            }

            // 下载完成，重命名临时文件
            tempFile.renameTo(targetFile)

            emit(DownloadProgress(
                progress = 100,
                downloaded = downloaded,
                total = contentLength,
                isComplete = true,
                file = targetFile
            ))

        } catch (e: Exception) {
            emit(DownloadProgress(
                progress = 0,
                downloaded = 0,
                total = 0,
                error = "下载失败: ${e.message}"
            ))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 清理旧的下载文件
     */
    fun cleanup() {
        if (downloadDir.exists()) {
            downloadDir.listFiles()?.forEach { it.delete() }
        }
    }

    /**
     * 获取已下载文件（如果存在）
     */
    fun getDownloadedFile(fileName: String = "update.apk"): File? {
        val file = File(downloadDir, fileName)
        return if (file.exists()) file else null
    }

    /**
     * 格式化文件大小
     */
    fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> String.format("%.1f KB", bytes / 1024.0)
            else -> String.format("%.1f MB", bytes / (1024.0 * 1024))
        }
    }
}
