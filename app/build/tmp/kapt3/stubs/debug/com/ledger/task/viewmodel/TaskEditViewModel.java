package com.ledger.task.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u008c\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0011\u001a\u00020\u0012J%\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\n0\t2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\tH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016J\u0006\u0010\u0017\u001a\u00020\u0012J\u000e\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u0015J\u0006\u0010\u001a\u001a\u00020\u0012J\u000e\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u001dJ\u0015\u0010\u001e\u001a\u00020\u00122\b\u0010\u001f\u001a\u0004\u0018\u00010\u0015\u00a2\u0006\u0002\u0010 J\u000e\u0010!\u001a\u00020\u00122\u0006\u0010\"\u001a\u00020#J\u000e\u0010$\u001a\u00020\u00122\u0006\u0010%\u001a\u00020&J\u000e\u0010\'\u001a\u00020\u00122\u0006\u0010(\u001a\u00020)J\u000e\u0010*\u001a\u00020\u00122\u0006\u0010+\u001a\u00020\u001dJ\u000e\u0010,\u001a\u00020\u00122\u0006\u0010-\u001a\u00020\u001dJ\u0014\u0010.\u001a\u00020\u00122\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00150\tJ\u0014\u00100\u001a\u00020\u00122\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00150\tJ\u000e\u00101\u001a\u00020\u00122\u0006\u0010+\u001a\u00020\u001dJ\u001b\u00102\u001a\u00020\u00122\u0006\u00103\u001a\u000204\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b5\u00106J\u000e\u00107\u001a\u00020\u00122\u0006\u00108\u001a\u00020\u001dJ\u000e\u00109\u001a\u00020\u00122\u0006\u0010:\u001a\u00020\u0015J\u0014\u0010;\u001a\u00020\u00122\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00150\tJ\u000e\u0010<\u001a\u00020\u00122\u0006\u0010:\u001a\u00020\u0015J\u000e\u0010=\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u0015J\u000e\u0010>\u001a\u00020\u00122\u0006\u0010?\u001a\u00020@J\u000e\u0010A\u001a\u00020\u00122\u0006\u0010B\u001a\u00020\u0015J\u0014\u0010C\u001a\u00020\u00122\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00150\tJ\u000e\u0010D\u001a\u00020\u00122\u0006\u0010B\u001a\u00020\u0015J\u000e\u0010E\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u0015J\u000e\u0010F\u001a\u00020\u00122\u0006\u0010G\u001a\u00020HJ\u000e\u0010I\u001a\u00020\u00122\u0006\u0010J\u001a\u00020KJ\u000e\u0010L\u001a\u00020\u00122\u0006\u0010J\u001a\u00020KJ\u000e\u0010M\u001a\u00020\u00122\u0006\u0010J\u001a\u00020KJ\u000e\u0010N\u001a\u00020\u00122\u0006\u0010J\u001a\u00020KJ\u000e\u0010O\u001a\u00020\u00122\u0006\u0010P\u001a\u00020QJ\u000e\u0010R\u001a\u00020\u00122\u0006\u0010S\u001a\u00020\u001dJ\u0006\u0010T\u001a\u00020\u0012J\u0006\u0010U\u001a\u00020\u0012R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u0082\u0002\u000b\n\u0002\b\u0019\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006V"}, d2 = {"Lcom/ledger/task/viewmodel/TaskEditViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/ledger/task/viewmodel/TaskEditUiState;", "cachedAvailableTasks", "", "Lcom/ledger/task/data/model/Task;", "repository", "Lcom/ledger/task/data/repository/TaskRepositoryImpl;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearAvailableTasksCache", "", "getTasksByIds", "ids", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadAvailableTasks", "loadTask", "taskId", "onAddCategory", "onCategoryChange", "category", "", "onCompletedAtChange", "completedAt", "(Ljava/lang/Long;)V", "onDeadlineChange", "deadline", "Ljava/time/LocalDateTime;", "onDeadlineDateChange", "date", "Ljava/time/LocalDate;", "onDeadlineTimeChange", "time", "Ljava/time/LocalTime;", "onDeleteCategory", "categoryId", "onDescriptionChange", "desc", "onDialogConfirmPredecessors", "newIds", "onDialogConfirmRelated", "onEditCategory", "onNewCategoryColorChange", "color", "Landroidx/compose/ui/graphics/Color;", "onNewCategoryColorChange-8_81llA", "(J)V", "onNewCategoryNameChange", "name", "onPredecessorAdd", "predecessorId", "onPredecessorIdsChange", "onPredecessorRemove", "onPredecessorSelect", "onPriorityChange", "priority", "Lcom/ledger/task/data/model/Priority;", "onRelatedAdd", "relatedId", "onRelatedIdsChange", "onRelatedRemove", "onRelatedSelect", "onRichContentChange", "richContent", "Lcom/ledger/task/data/model/RichContent;", "onShowAddCategoryDialog", "show", "", "onShowCategoryDialog", "onShowPredecessorDialog", "onShowRelatedDialog", "onStatusChange", "status", "Lcom/ledger/task/data/model/TaskStatus;", "onTitleChange", "title", "onUpdateCategory", "save", "app_debug"})
public final class TaskEditViewModel extends androidx.lifecycle.AndroidViewModel {
    private final com.ledger.task.data.repository.TaskRepositoryImpl repository = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.ledger.task.viewmodel.TaskEditUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.ledger.task.viewmodel.TaskEditUiState> uiState = null;
    private java.util.List<com.ledger.task.data.model.Task> cachedAvailableTasks;
    
    public TaskEditViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.ledger.task.viewmodel.TaskEditUiState> getUiState() {
        return null;
    }
    
    public final void loadTask(long taskId) {
    }
    
    private final java.lang.Object getTasksByIds(java.util.List<java.lang.Long> ids, kotlin.coroutines.Continuation<? super java.util.List<com.ledger.task.data.model.Task>> continuation) {
        return null;
    }
    
    public final void onTitleChange(@org.jetbrains.annotations.NotNull
    java.lang.String title) {
    }
    
    public final void onDescriptionChange(@org.jetbrains.annotations.NotNull
    java.lang.String desc) {
    }
    
    public final void onPriorityChange(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.Priority priority) {
    }
    
    public final void onDeadlineChange(@org.jetbrains.annotations.NotNull
    java.time.LocalDateTime deadline) {
    }
    
    public final void onDeadlineDateChange(@org.jetbrains.annotations.NotNull
    java.time.LocalDate date) {
    }
    
    public final void onDeadlineTimeChange(@org.jetbrains.annotations.NotNull
    java.time.LocalTime time) {
    }
    
    public final void onStatusChange(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.TaskStatus status) {
    }
    
    public final void onCompletedAtChange(@org.jetbrains.annotations.Nullable
    java.lang.Long completedAt) {
    }
    
    public final void onRichContentChange(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.RichContent richContent) {
    }
    
    public final void onPredecessorIdsChange(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Long> newIds) {
    }
    
    public final void onRelatedIdsChange(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Long> newIds) {
    }
    
    public final void onPredecessorAdd(long predecessorId) {
    }
    
    public final void onPredecessorRemove(long predecessorId) {
    }
    
    public final void onRelatedAdd(long relatedId) {
    }
    
    public final void onRelatedRemove(long relatedId) {
    }
    
    public final void save() {
    }
    
    /**
     * 加载可用任务列表（使用缓存避免重复查询）
     */
    public final void loadAvailableTasks() {
    }
    
    /**
     * 清除缓存（在保存新任务后调用）
     */
    public final void clearAvailableTasksCache() {
    }
    
    public final void onShowPredecessorDialog(boolean show) {
    }
    
    public final void onShowRelatedDialog(boolean show) {
    }
    
    public final void onPredecessorSelect(long taskId) {
    }
    
    public final void onRelatedSelect(long taskId) {
    }
    
    public final void onDialogConfirmPredecessors(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Long> newIds) {
    }
    
    public final void onDialogConfirmRelated(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Long> newIds) {
    }
    
    public final void onShowCategoryDialog(boolean show) {
    }
    
    public final void onCategoryChange(@org.jetbrains.annotations.NotNull
    java.lang.String category) {
    }
    
    public final void onShowAddCategoryDialog(boolean show) {
    }
    
    public final void onNewCategoryNameChange(@org.jetbrains.annotations.NotNull
    java.lang.String name) {
    }
    
    public final void onAddCategory() {
    }
    
    public final void onEditCategory(@org.jetbrains.annotations.NotNull
    java.lang.String categoryId) {
    }
    
    public final void onUpdateCategory() {
    }
    
    public final void onDeleteCategory(@org.jetbrains.annotations.NotNull
    java.lang.String categoryId) {
    }
}