package com.ledger.task.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.task.update.ApkDownloader
import com.ledger.task.update.ApkInstaller
import com.ledger.task.update.ReleaseInfo
import com.ledger.task.update.VersionChecker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

/**
 * 更新状态
 */
sealed interface UpdateState {
    data object Idle : UpdateState
    data object Checking : UpdateState
    data class UpdateAvailable(val info: ReleaseInfo) : UpdateState
    data object NoUpdate : UpdateState
    data class Downloading(val progress: Int, val downloaded: Long, val total: Long) : UpdateState
    data class DownloadComplete(val file: File) : UpdateState
    data class Error(val message: String) : UpdateState
}

/**
 * 更新页面 UI 状态
 */
data class UpdateUiState(
    val updateState: UpdateState = UpdateState.Idle,
    val lastCheckTime: String? = null,
    val isChecking: Boolean = false,
    val isDownloading: Boolean = false
)

/**
 * 更新专用 ViewModel
 * 独立模块，不访问应用内数据
 */
class UpdateViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(UpdateUiState())
    val uiState: StateFlow<UpdateUiState> = _uiState.asStateFlow()

    private val versionChecker = VersionChecker(application)
    private val apkDownloader = ApkDownloader(application)

    init {
        loadLastCheckTime()
    }

    private fun loadLastCheckTime() {
        _uiState.value = _uiState.value.copy(
            lastCheckTime = versionChecker.getLastCheckDescription()
        )
    }

    /**
     * 检查更新
     */
    fun checkUpdate(manual: Boolean = true) {
        if (_uiState.value.isChecking) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isChecking = true,
                updateState = UpdateState.Checking
            )

            val result = versionChecker.checkUpdate()

            result.fold(
                onSuccess = { releaseInfo ->
                    versionChecker.recordCheckTime()
                    _uiState.value = _uiState.value.copy(
                        isChecking = false,
                        updateState = if (releaseInfo != null) {
                            UpdateState.UpdateAvailable(releaseInfo)
                        } else {
                            UpdateState.NoUpdate
                        },
                        lastCheckTime = "刚刚"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isChecking = false,
                        updateState = UpdateState.Error(error.message ?: "检查更新失败")
                    )
                }
            )
        }
    }

    /**
     * 自动检查更新（仅在满足条件时执行）
     */
    fun autoCheckIfNeeded() {
        if (versionChecker.shouldAutoCheck()) {
            checkUpdate(manual = false)
        }
    }

    /**
     * 下载更新
     */
    fun downloadUpdate(info: ReleaseInfo) {
        if (_uiState.value.isDownloading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDownloading = true)

            apkDownloader.download(info.apkUrl).collect { progress ->
                when {
                    progress.error != null -> {
                        _uiState.value = _uiState.value.copy(
                            isDownloading = false,
                            updateState = UpdateState.Error(progress.error)
                        )
                    }
                    progress.isComplete && progress.file != null -> {
                        _uiState.value = _uiState.value.copy(
                            isDownloading = false,
                            updateState = UpdateState.DownloadComplete(progress.file)
                        )
                    }
                    else -> {
                        _uiState.value = _uiState.value.copy(
                            updateState = UpdateState.Downloading(
                                progress.progress,
                                progress.downloaded,
                                progress.total
                            )
                        )
                    }
                }
            }
        }
    }

    /**
     * 安装更新
     */
    fun installUpdate(file: File): Boolean {
        val context = getApplication<Application>()

        // 检查安装权限
        if (!ApkInstaller.canRequestPackageInstalls(context)) {
            _uiState.value = _uiState.value.copy(
                updateState = UpdateState.Error("请授予安装应用权限")
            )
            return false
        }

        return ApkInstaller.install(context, file)
    }

    /**
     * 重置状态
     */
    fun resetState() {
        _uiState.value = _uiState.value.copy(
            updateState = UpdateState.Idle,
            isChecking = false,
            isDownloading = false
        )
    }

    /**
     * 清理下载缓存
     */
    fun cleanupDownloads() {
        apkDownloader.cleanup()
    }

    /**
     * 格式化文件大小
     */
    fun formatFileSize(bytes: Long): String {
        return apkDownloader.formatFileSize(bytes)
    }

    override fun onCleared() {
        super.onCleared()
        // 清理下载缓存
        apkDownloader.cleanup()
    }
}
