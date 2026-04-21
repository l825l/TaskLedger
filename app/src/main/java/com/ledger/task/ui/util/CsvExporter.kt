package com.ledger.task.ui.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import com.ledger.task.domain.model.Task
import java.time.LocalDate

/**
 * CSV 导出工具
 */
object CsvExporter {

    /**
     * 导出任务列表为 CSV 文件（带 BOM 头，Excel 中文兼容）
     * 保存到 Downloads 目录，返回文件 Uri
     */
    fun exportToDownloads(
        context: Context,
        tasks: List<Task>,
        fileName: String = "任务台账_${LocalDate.now()}.csv"
    ): Uri? {
        return try {
            val bom = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())
            val header = "序号,标题,优先级,时限,状态\n"
            val rows = tasks.mapIndexed { i, task ->
                "${i + 1},\"${task.title}\",${task.priority.label},${task.deadline},${task.displayStatus.label}"
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

            uri
        } catch (e: SecurityException) {
            // 权限拒绝
            null
        } catch (e: Exception) {
            // 其他 I/O 错误
            null
        }
    }

    /**
     * 分享 CSV 文件
     */
    fun share(context: Context, uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "分享台账文件"))
        } catch (e: Exception) {
            // 分享失败（如没有应用可以处理）
        }
    }

    /**
     * 导出任务列表为 Excel (.xlsx) 文件
     * 使用简单的 XML 格式（Excel XML Spreadsheet）
     */
    fun exportToExcel(
        context: Context,
        tasks: List<Task>,
        fileName: String = "任务台账_${LocalDate.now()}.xlsx"
    ): Uri? {
        return try {
            // 简单的 Excel XML 格式（兼容 .xlsx 扩展名）
            val xmlContent = buildString {
                appendLine("<?xml version=\"1.0\"?>")
                appendLine("<ss:Workbook xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\">")
                appendLine("  <ss:Styles>")
                appendLine("    <ss:Style ss:ID=\"s1\">")
                appendLine("      <ss:Font ss:Bold=\"1\"/>")
                appendLine("    </ss:Style>")
                appendLine("  </ss:Styles>")
                appendLine("  <ss:Worksheet ss:Name=\"任务台账\">")
                appendLine("    <ss:Table>")

                // 表头
                appendLine("      <ss:Row>")
                appendLine("        <ss:Cell ss:StyleID=\"s1\"><ss:Data ss:Type=\"String\">序号</ss:Data></ss:Cell>")
                appendLine("        <ss:Cell ss:StyleID=\"s1\"><ss:Data ss:Type=\"String\">标题</ss:Data></ss:Cell>")
                appendLine("        <ss:Cell ss:StyleID=\"s1\"><ss:Data ss:Type=\"String\">优先级</ss:Data></ss:Cell>")
                appendLine("        <ss:Cell ss:StyleID=\"s1\"><ss:Data ss:Type=\"String\">时限</ss:Data></ss:Cell>")
                appendLine("        <ss:Cell ss:StyleID=\"s1\"><ss:Data ss:Type=\"String\">状态</ss:Data></ss:Cell>")
                appendLine("        <ss:Cell ss:StyleID=\"s1\"><ss:Data ss:Type=\"String\">分类</ss:Data></ss:Cell>")
                appendLine("        <ss:Cell ss:StyleID=\"s1\"><ss:Data ss:Type=\"String\">备注</ss:Data></ss:Cell>")
                appendLine("      </ss:Row>")

                // 数据行
                tasks.forEachIndexed { i, task ->
                    appendLine("      <ss:Row>")
                    appendLine("        <ss:Cell><ss:Data ss:Type=\"Number\">${i + 1}</ss:Data></ss:Cell>")
                    appendLine("        <ss:Cell><ss:Data ss:Type=\"String\">${escapeXml(task.title)}</ss:Data></ss:Cell>")
                    appendLine("        <ss:Cell><ss:Data ss:Type=\"String\">${task.priority.label}</ss:Data></ss:Cell>")
                    appendLine("        <ss:Cell><ss:Data ss:Type=\"String\">${task.deadline}</ss:Data></ss:Cell>")
                    appendLine("        <ss:Cell><ss:Data ss:Type=\"String\">${task.displayStatus.label}</ss:Data></ss:Cell>")
                    appendLine("        <ss:Cell><ss:Data ss:Type=\"String\">${escapeXml(task.category)}</ss:Data></ss:Cell>")
                    appendLine("        <ss:Cell><ss:Data ss:Type=\"String\">${escapeXml(task.description)}</ss:Data></ss:Cell>")
                    appendLine("      </ss:Row>")
                }

                appendLine("    </ss:Table>")
                appendLine("  </ss:Worksheet>")
                appendLine("</ss:Workbook>")
            }

            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
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

            uri
        } catch (e: SecurityException) {
            // 权限拒绝
            null
        } catch (e: Exception) {
            // 其他 I/O 错误
            null
        }
    }

    /**
     * 转义 XML 特殊字符
     */
    private fun escapeXml(text: String): String {
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }
}
