package com.ledger.task.ui.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.core.content.FileProvider
import com.ledger.task.domain.model.RichContent
import com.ledger.task.domain.model.RichTextItem
import com.ledger.task.domain.model.Task
import java.io.ByteArrayInputStream
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 台账导出工具
 */
object LedgerExporter {

    private const val TAG = "LedgerExporter"
    private val FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")

    /**
     * 生成唯一文件名
     */
    private fun generateFileName(extension: String): String {
        val timestamp = java.time.LocalDateTime.now().format(FILE_DATE_FORMAT)
        return "任务台账_${timestamp}.$extension"
    }

    /**
     * 导出字段定义
     */
    object Fields {
        const val TITLE = "标题"
        const val CATEGORY = "分类"
        const val PRIORITY = "优先级"
        const val DEADLINE = "时限"
        const val STATUS = "完成状态"
        const val RELATED_SUMMARY = "关联事项摘要"
        const val NOTE = "备注"
    }

    /**
     * 导出任务列表为 CSV 文件（带 BOM 头，Excel 中文兼容）
     */
    fun exportToCSV(
        context: Context,
        tasks: List<Task>,
        relatedTaskSummaries: Map<Long, String> = emptyMap(),
        fileName: String = generateFileName("csv")
    ): Uri? {
        return try {
            // 创建图片临时目录
            val imageDir = File(context.cacheDir, "export_images")
            imageDir.mkdirs()

            val bom = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())
            val header = "${Fields.TITLE},${Fields.CATEGORY},${Fields.PRIORITY},${Fields.DEADLINE},${Fields.STATUS},${Fields.RELATED_SUMMARY},${Fields.NOTE}\n"
            val rows = tasks.mapIndexed { index, task ->
                val note = extractNoteText(task.richContent, context, imageDir, index)
                val relatedSummary = relatedTaskSummaries[task.id] ?: ""
                "\"${escapeCsv(task.title)}\",\"${escapeCsv(task.category)}\",${task.priority.label},${formatDeadline(task.deadline)},${task.displayStatus.label},\"${escapeCsv(relatedSummary)}\",\"${escapeCsv(note)}\""
            }.joinToString("\n")

            val content = bom + (header + rows).toByteArray(Charsets.UTF_8)

            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "text/csv")
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                ?: return null

            resolver.openOutputStream(uri)?.use { stream ->
                stream.write(content)
            }

            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)

            Log.i(TAG, "CSV export successful: $fileName")
            uri
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException during CSV export: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Failed to export CSV: ${e.message}", e)
            null
        }
    }

    /**
     * 导出任务列表为 Excel (.xls) 文件
     * 使用 Excel XML Spreadsheet 格式
     */
    fun exportToExcel(
        context: Context,
        tasks: List<Task>,
        relatedTaskSummaries: Map<Long, String> = emptyMap(),
        fileName: String = generateFileName("xls")
    ): Uri? {
        return try {
            // 创建图片临时目录
            val imageDir = File(context.cacheDir, "export_images")
            imageDir.mkdirs()

            val xmlContent = buildString {
                appendLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                appendLine("<?mso-application progid=\"Excel.Sheet\"?>")
                appendLine("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"")
                appendLine(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\">")
                appendLine("  <Styles>")
                appendLine("    <Style ss:ID=\"sHeader\">")
                appendLine("      <Font ss:Bold=\"1\"/>")
                appendLine("      <Alignment ss:Horizontal=\"Center\"/>")
                appendLine("    </Style>")
                appendLine("  </Styles>")
                appendLine("  <Worksheet ss:Name=\"任务台账\">")
                appendLine("    <Table>")

                // 表头
                appendLine("      <Row>")
                appendHeaderCell(Fields.TITLE)
                appendHeaderCell(Fields.CATEGORY)
                appendHeaderCell(Fields.PRIORITY)
                appendHeaderCell(Fields.DEADLINE)
                appendHeaderCell(Fields.STATUS)
                appendHeaderCell(Fields.RELATED_SUMMARY)
                appendHeaderCell(Fields.NOTE)
                appendLine("      </Row>")

                // 数据行
                tasks.forEachIndexed { index, task ->
                    appendLine("      <Row>")
                    appendDataCell(task.title)
                    appendDataCell(task.category)
                    appendDataCell(task.priority.label)
                    appendDataCell(formatDeadline(task.deadline))
                    appendDataCell(task.displayStatus.label)
                    val relatedSummary = relatedTaskSummaries[task.id] ?: ""
                    appendDataCell(relatedSummary)
                    val note = extractNoteText(task.richContent, context, imageDir, index)
                    appendDataCell(note)
                    appendLine("      </Row>")
                }

                appendLine("    </Table>")
                appendLine("  </Worksheet>")
                appendLine("</Workbook>")
            }

            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/vnd.ms-excel")
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                ?: return null

            resolver.openOutputStream(uri)?.use { stream ->
                stream.write(xmlContent.toByteArray(Charsets.UTF_8))
            }

            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)

            Log.i(TAG, "Excel export successful: $fileName")
            uri
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException during Excel export: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Failed to export Excel: ${e.message}", e)
            null
        }
    }

    /**
     * 分享文件
     */
    fun share(context: Context, uri: Uri, mimeType: String = "text/csv") {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = mimeType
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(intent, "分享台账文件").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to share file: ${e.message}", e)
        }
    }

    /**
     * 分享到微信
     */
    fun shareToWechat(context: Context, uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "*/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                setPackage("com.tencent.mm")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.w(TAG, "WeChat share failed, falling back to generic share: ${e.message}")
            // 微信未安装或分享失败，回退到通用分享
            share(context, uri)
        }
    }

    /**
     * 分享到邮件
     */
    fun shareToEmail(context: Context, uri: Uri, subject: String = "任务台账") {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(intent, "发送邮件").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to share via email: ${e.message}", e)
        }
    }

    /**
     * 打开文件所在位置（使用系统文件管理器）
     */
    fun openFileLocation(context: Context) {
        try {
            // 尝试打开下载文件夹
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(
                    Uri.parse("content://com.android.providers.downloads.documents/document/downloads"),
                    "vnd.android.document/directory"
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.w(TAG, "DocumentsUI failed: ${e.message}")
            try {
                // 方案2：打开文件管理器应用
                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_LAUNCHER)
                    setClassName("com.android.documentsui", "com.android.documentsui.DocumentsActivity")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            } catch (e2: Exception) {
                Log.e(TAG, "All methods failed", e2)
            }
        }
    }

    /**
     * 从富文本内容中提取文本部分
     * 图片保存到临时目录并导出文件路径
     * 附件解析URI并导出路径
     */
    private fun extractNoteText(
        richContent: RichContent,
        context: Context,
        imageDir: File,
        taskIndex: Int
    ): String {
        var imageCounter = 0

        return richContent.items.mapNotNull { item ->
            when (item) {
                is RichTextItem.Text -> item.content
                is RichTextItem.Image -> {
                    try {
                        // 将Base64图片解码保存到临时目录
                        val imageBytes = Base64.decode(item.data, Base64.DEFAULT)
                        val imageFile = File(imageDir, "task_${taskIndex}_img_${imageCounter++}.jpg")
                        imageFile.writeBytes(imageBytes)
                        "[图片:${imageFile.absolutePath}]"
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to save image: ${e.message}")
                        "[图片:保存失败]"
                    }
                }
                is RichTextItem.Attachment -> {
                    try {
                        val uri = Uri.parse(item.url)
                        "[附件:${item.name}|路径:${uri.path ?: item.url}]"
                    } catch (e: Exception) {
                        "[附件:${item.name}]"
                    }
                }
                is RichTextItem.QuickTagItem -> item.tag.label
            }
        }.joinToString("")
    }

    /**
     * 格式化截止时间
     */
    private fun formatDeadline(deadline: java.time.LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return deadline.format(formatter)
    }

    /**
     * CSV 字段转义
     */
    private fun escapeCsv(text: String): String {
        return text.replace("\"", "\"\"")
    }

    /**
     * XML 特殊字符转义
     */
    private fun escapeXml(text: String): String {
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }

    /**
     * 添加表头单元格
     */
    private fun StringBuilder.appendHeaderCell(text: String) {
        appendLine("        <Cell ss:StyleID=\"sHeader\"><Data ss:Type=\"String\">${escapeXml(text)}</Data></Cell>")
    }

    /**
     * 添加数据单元格
     */
    private fun StringBuilder.appendDataCell(text: String) {
        appendLine("        <Cell><Data ss:Type=\"String\">${escapeXml(text)}</Data></Cell>")
    }
}
