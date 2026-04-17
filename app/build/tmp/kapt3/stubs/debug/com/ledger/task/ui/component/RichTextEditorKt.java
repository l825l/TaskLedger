package com.ledger.task.ui.component;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 2, d1 = {"\u0000,\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u001e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a8\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\rH\u0007\u001a\u001a\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\f\u001a\u00020\rH\u0007\u001a\u0010\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0003H\u0002\u00a8\u0006\u0011"}, d2 = {"ImagePreviewDialog", "", "base64Image", "", "onDismiss", "Lkotlin/Function0;", "RichTextEditor", "richContent", "Lcom/ledger/task/data/model/RichContent;", "onRichContentChange", "Lkotlin/Function1;", "label", "modifier", "Landroidx/compose/ui/Modifier;", "RichTextPreview", "getMimeType", "fileName", "app_debug"})
public final class RichTextEditorKt {
    
    /**
     * 富文本编辑器
     * 支持文字、图片、附件链接
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void RichTextEditor(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.RichContent richContent, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.ledger.task.data.model.RichContent, kotlin.Unit> onRichContentChange, @org.jetbrains.annotations.NotNull
    java.lang.String label, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 获取文件 MIME 类型
     */
    private static final java.lang.String getMimeType(java.lang.String fileName) {
        return null;
    }
    
    /**
     * 图片预览对话框
     */
    @androidx.compose.runtime.Composable
    private static final void ImagePreviewDialog(java.lang.String base64Image, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    /**
     * 富文本预览组件
     */
    @androidx.compose.runtime.Composable
    public static final void RichTextPreview(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.RichContent richContent, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
}