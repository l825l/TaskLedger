package com.ledger.task.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

/**
 * APK 安装工具
 * 独立模块，调用系统安装器
 */
object ApkInstaller {

    /**
     * 安装 APK
     */
    fun install(context: Context, apkFile: File): Boolean {
        return try {
            val intent = createInstallIntent(context, apkFile)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 创建安装 Intent
     */
    private fun createInstallIntent(context: Context, apkFile: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = getApkUri(context, apkFile)

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
            context.packageManager.canRequestPackageInstalls()
        } else {
            true
        }
    }
}
