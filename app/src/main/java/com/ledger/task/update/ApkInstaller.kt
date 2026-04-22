package com.ledger.task.update

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.security.MessageDigest

/**
 * APK 安装工具
 * 独立模块，调用系统安装器
 */
object ApkInstaller {

    private const val TAG = "ApkInstaller"

    // 当前应用的签名证书指纹（SHA-256）
    // 此值需要在发布时更新为实际的签名证书指纹
    private const val EXPECTED_SIGNATURE_SHA256 = "EXPECTED_SIGNATURE_PLACEHOLDER"

    /**
     * 安装 APK
     */
    fun install(context: Context, apkFile: File): Boolean {
        return try {
            Log.i(TAG, "开始安装 APK: ${apkFile.absolutePath}, 存在=${apkFile.exists()}, 大小=${apkFile.length()}")

            // 检查文件是否存在
            if (!apkFile.exists()) {
                Log.e(TAG, "APK 文件不存在")
                return false
            }

            // 验证 APK 签名
            if (!verifyApkSignature(context, apkFile)) {
                Log.e(TAG, "APK 签名验证失败，拒绝安装")
                return false
            }

            val intent = createInstallIntent(context, apkFile)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            Log.i(TAG, "安装 Intent 已发送")
            true
        } catch (e: Exception) {
            Log.e(TAG, "安装失败: ${e.message}", e)
            false
        }
    }

    /**
     * 验证 APK 签名
     * 检查 APK 的签名证书是否与当前应用匹配
     */
    private fun verifyApkSignature(context: Context, apkFile: File): Boolean {
        return try {
            // 获取当前应用的签名
            val currentSignature = getCurrentAppSignature(context)
            if (currentSignature == null) {
                Log.w(TAG, "无法获取当前应用签名，跳过验证")
                return true // 开发模式下允许跳过
            }

            // 获取 APK 的签名
            val apkSignature = getApkSignature(context, apkFile)
            if (apkSignature == null) {
                Log.e(TAG, "无法获取 APK 签名")
                return false
            }

            // 比较签名
            val currentSha256 = calculateSha256(currentSignature.toByteArray())
            val apkSha256 = calculateSha256(apkSignature.toByteArray())

            Log.i(TAG, "当前应用签名 SHA-256: ${currentSha256.take(16)}...")
            Log.i(TAG, "APK 签名 SHA-256: ${apkSha256.take(16)}...")

            val isValid = currentSha256 == apkSha256
            if (isValid) {
                Log.i(TAG, "APK 签名验证通过")
            } else {
                Log.e(TAG, "APK 签名不匹配")
            }
            isValid
        } catch (e: Exception) {
            Log.e(TAG, "签名验证异常: ${e.message}", e)
            false
        }
    }

    /**
     * 获取当前应用的签名
     */
    @Suppress("DEPRECATION")
    private fun getCurrentAppSignature(context: Context): Signature? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val signingInfo = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                ).signingInfo
                signingInfo.apkContentsSigners.firstOrNull()
            } else {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNATURES
                ).signatures.firstOrNull()
            }
        } catch (e: Exception) {
            Log.e(TAG, "获取当前应用签名失败: ${e.message}")
            null
        }
    }

    /**
     * 获取 APK 文件的签名
     */
    @Suppress("DEPRECATION")
    private fun getApkSignature(context: Context, apkFile: File): Signature? {
        return try {
            val packageManager = context.packageManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val packageInfo = packageManager.getPackageArchiveInfo(
                    apkFile.absolutePath,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )
                packageInfo?.signingInfo?.apkContentsSigners?.firstOrNull()
            } else {
                val packageInfo = packageManager.getPackageArchiveInfo(
                    apkFile.absolutePath,
                    PackageManager.GET_SIGNATURES
                )
                packageInfo?.signatures?.firstOrNull()
            }
        } catch (e: Exception) {
            Log.e(TAG, "获取 APK 签名失败: ${e.message}")
            null
        }
    }

    /**
     * 计算字节数组的 SHA-256 哈希值
     */
    private fun calculateSha256(data: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(data)
        return hash.joinToString("") { "%02x".format(it) }
    }

    /**
     * 创建安装 Intent
     */
    private fun createInstallIntent(context: Context, apkFile: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = getApkUri(context, apkFile)
        Log.i(TAG, "APK Uri: $uri")

        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        return intent
    }

    /**
     * 获取 APK Uri（适配 Android 7.0+）
     */
    private fun getApkUri(context: Context, apkFile: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apkFile
            )
        } else {
            Uri.fromFile(apkFile)
        }
    }

    /**
     * 检查是否有安装权限（Android 8.0+）
     */
    fun canRequestPackageInstalls(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val result = context.packageManager.canRequestPackageInstalls()
            Log.i(TAG, "安装权限检查结果: $result")
            result
        } else {
            true
        }
    }
}
