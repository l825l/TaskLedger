package com.ledger.task.update

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 发布版本信息
 * 仅包含版本更新所需的数据，不涉及应用内数据
 */
@Serializable
data class ReleaseInfo(
    val version: String,
    val changelog: String,
    val apkUrl: String,
    val fileSize: Long = 0
)

/**
 * GitHub API 发布响应
 */
@Serializable
data class GitHubRelease(
    val tagName: String,
    val body: String,
    val assets: List<GitHubAsset>
) {
    @Serializable
    data class GitHubAsset(
        val name: String,
        val browserDownloadUrl: String,
        val size: Long
    )
}

/**
 * Gitee API 发布响应
 */
@Serializable
data class GiteeRelease(
    @SerialName("tag_name")
    val tagName: String,
    val body: String,
    val assets: List<GiteeAsset>
) {
    @Serializable
    data class GiteeAsset(
        val name: String,
        @SerialName("browser_download_url")
        val browserDownloadUrl: String
    )
}
