package com.ledger.task.ui.component

import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import com.ledger.task.data.model.RichContent
import com.ledger.task.data.model.RichTextItem
import com.ledger.task.data.model.QuickTag
import com.ledger.task.ui.theme.Accent
import com.ledger.task.ui.theme.BorderDim
import com.ledger.task.ui.theme.DeepBackground
import com.ledger.task.ui.theme.ElevatedBackground
import com.ledger.task.ui.theme.TextMuted
import com.ledger.task.ui.theme.TextPrimary
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.util.UUID
import kotlin.math.roundToInt

/**
 * 富文本编辑器
 * 支持文字、图片、附件链接
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RichTextEditor(
    richContent: RichContent,
    onRichContentChange: (RichContent) -> Unit,
    label: String = "备注",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // 图片预览状态
    var previewImageBase64 by remember { mutableStateOf<String?>(null) }

    // 图片选择器
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            runCatching {
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    // 压缩图片
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
                    val base64Image = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)

                    val newItems = richContent.items + RichTextItem.Image(base64Image)
                    onRichContentChange(RichContent(newItems))
                }
            }.onFailure {
                // 图片加载失败，忽略
            }
        }
    }

    // 文件选择器
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val fileName = it.lastPathSegment?.split("/")?.lastOrNull() ?: "附件"
            val newItems = richContent.items + RichTextItem.Attachment(fileName, it.toString())
            onRichContentChange(RichContent(newItems))
        }
    }

    Column(modifier = modifier) {
        Text(
            text = label,
            color = TextMuted,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 富文本编辑区域
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderDim, RoundedCornerShape(8.dp))
                .background(ElevatedBackground)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // 显示已添加的内容
                richContent.items.forEachIndexed { index, item ->
                    when (item) {
                        is RichTextItem.Text -> {
                            Text(
                                text = item.content,
                                color = TextPrimary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        is RichTextItem.Image -> {
                            // 解码并显示图片
                            val bitmap = remember(item.data) {
                                runCatching {
                                    val bytes = Base64.decode(item.data, Base64.DEFAULT)
                                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                }.getOrNull()
                            }

                            bitmap?.let { bmp ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp, bottom = 8.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFE0E0E0))
                                        .clickable { previewImageBase64 = item.data }
                                ) {
                                    Image(
                                        bitmap = bmp.asImageBitmap(),
                                        contentDescription = "图片 ${index + 1}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                    // 删除按钮
                                    IconButton(
                                        onClick = {
                                            val newItems = richContent.items.filterIndexed { i, _ -> i != index }
                                            onRichContentChange(RichContent(newItems))
                                        },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(4.dp)
                                            .size(24.dp)
                                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "删除图片",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            } ?: run {
                                // 图片加载失败时显示占位符
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .padding(top = 8.dp, bottom = 8.dp)
                                        .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                                        .clickable { /* 预览图片 */ },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "图片加载失败",
                                        color = TextMuted,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                        is RichTextItem.Attachment -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 4.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFF5F5F5))
                                    .clickable {
                                        // 打开附件
                                        runCatching {
                                            val uri = Uri.parse(item.url)
                                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                                setDataAndType(uri, getMimeType(item.name))
                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            }
                                            context.startActivity(Intent.createChooser(intent, "打开附件"))
                                        }
                                    }
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AttachFile,
                                        contentDescription = null,
                                        tint = Accent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = item.name,
                                        color = Accent,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        val newItems = richContent.items.filterIndexed { i, _ -> i != index }
                                        onRichContentChange(RichContent(newItems))
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "删除附件",
                                        tint = TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                        is RichTextItem.QuickTagItem -> {
                            Row(
                                modifier = Modifier
                                    .padding(top = 4.dp, bottom = 4.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(item.tag.color.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(item.tag.color, CircleShape)
                                )
                                Text(
                                    text = item.tag.label,
                                    color = item.tag.color,
                                    style = MaterialTheme.typography.labelSmall
                                )
                                IconButton(
                                    onClick = {
                                        val newItems = richContent.items.filterIndexed { i, _ -> i != index }
                                        onRichContentChange(RichContent(newItems))
                                    },
                                    modifier = Modifier.size(18.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "删除标签",
                                        tint = item.tag.color,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 文字输入框
                BasicTextField(
                    value = TextFieldValue(richContent.toPlainText()),
                    onValueChange = { newText ->
                        val newItems = richContent.items + RichTextItem.Text(newText.text)
                        onRichContentChange(RichContent(newItems))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .border(0.dp, Color.Transparent, RoundedCornerShape(8.dp))
                        .background(Color.Transparent),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    cursorBrush = androidx.compose.ui.graphics.Brush.linearGradient(
                        listOf(Color(0xFF00BFA5), Color(0xFF40C4FF))
                    )
                )
            }
        }

        // 工具栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 添加文字
            IconButton(
                onClick = {
                    val newItems = richContent.items + RichTextItem.Text("\n")
                    onRichContentChange(RichContent(newItems))
                }
            ) {
                Icon(
                    imageVector = Icons.Default.TextFields,
                    contentDescription = "添加文字",
                    tint = if (richContent.items.isEmpty()) Accent else TextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }

            // 添加图片
            IconButton(
                onClick = { imageLauncher.launch("image/*") }
            ) {
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = "添加图片",
                    tint = TextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }

            // 添加附件
            IconButton(
                onClick = { fileLauncher.launch("*/*") }
            ) {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = "添加附件",
                    tint = TextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }

            // 快捷标签下拉菜单
            var showTagMenu by remember { mutableStateOf(false) }
            Box {
                IconButton(
                    onClick = { showTagMenu = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Label,
                        contentDescription = "添加快捷标签",
                        tint = TextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }
                DropdownMenu(
                    expanded = showTagMenu,
                    onDismissRequest = { showTagMenu = false }
                ) {
                    QuickTag.values().forEach { tag ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(tag.color, CircleShape)
                                    )
                                    Text(
                                        text = tag.label,
                                        color = tag.color
                                    )
                                }
                            },
                            onClick = {
                                val newItems = richContent.items + RichTextItem.QuickTagItem(tag)
                                onRichContentChange(RichContent(newItems))
                                showTagMenu = false
                            }
                        )
                    }
                }
            }
        }
    }

    // 图片预览对话框
    if (previewImageBase64 != null) {
        ImagePreviewDialog(
            base64Image = previewImageBase64!!,
            onDismiss = { previewImageBase64 = null }
        )
    }
}

/**
 * 获取文件 MIME 类型
 */
private fun getMimeType(fileName: String): String {
    val extension = fileName.substringAfterLast(".", "")
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "*/*"
}

/**
 * 图片预览对话框
 */
@Composable
private fun ImagePreviewDialog(
    base64Image: String,
    onDismiss: () -> Unit
) {
    val bitmap = remember(base64Image) {
        runCatching {
            val bytes = Base64.decode(base64Image, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }.getOrNull()
    }

    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { onDismiss() })
                }
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(0.5f, 3f)
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            bitmap?.let { bmp ->
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = "图片预览",
                    modifier = Modifier
                        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        },
                    contentScale = ContentScale.Fit
                )
            }

            // 关闭提示
            Text(
                text = "点击任意位置关闭",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
            )
        }
    }
}

/**
 * 富文本预览组件
 */
@Composable
fun RichTextPreview(
    richContent: RichContent,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // 图片预览状态
    var previewImageBase64 by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier) {
        richContent.items.forEach { item ->
            when (item) {
                is RichTextItem.Text -> {
                    Text(
                        text = item.content,
                        color = TextPrimary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                is RichTextItem.Image -> {
                    // 解码并显示图片
                    val bitmap = remember(item.data) {
                        runCatching {
                            val bytes = Base64.decode(item.data, Base64.DEFAULT)
                            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        }.getOrNull()
                    }

                    bitmap?.let { bmp ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { previewImageBase64 = item.data }
                        ) {
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = "图片",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    } ?: run {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .padding(top = 8.dp, bottom = 8.dp)
                                .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "图片",
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
                is RichTextItem.Attachment -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF5F5F5))
                            .clickable {
                                // 打开附件
                                runCatching {
                                    val uri = Uri.parse(item.url)
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(uri, getMimeType(item.name))
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    context.startActivity(Intent.createChooser(intent, "打开附件"))
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = null,
                            tint = Accent,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = item.name,
                            color = Accent,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                is RichTextItem.QuickTagItem -> {
                    Row(
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(item.tag.color.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(item.tag.color, CircleShape)
                        )
                        Text(
                            text = item.tag.label,
                            color = item.tag.color,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }

    // 图片预览对话框
    if (previewImageBase64 != null) {
        ImagePreviewDialog(
            base64Image = previewImageBase64!!,
            onDismiss = { previewImageBase64 = null }
        )
    }
}
