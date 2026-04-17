package com.ledger.task.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ledger.task.data.model.Priority;
import com.ledger.task.data.model.TaskStatus;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TaskDao_Impl implements TaskDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TaskEntity> __insertionAdapterOfTaskEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<TaskEntity> __deletionAdapterOfTaskEntity;

  private final EntityDeletionOrUpdateAdapter<TaskEntity> __updateAdapterOfTaskEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSortOrder;

  public TaskDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTaskEntity = new EntityInsertionAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `tasks` (`id`,`title`,`priority`,`deadline`,`status`,`description`,`category`,`hasImage`,`predecessorIds`,`relatedIds`,`richContent`,`sortOrder`,`createdAt`,`completedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        final String _tmp = __converters.fromPriority(entity.getPriority());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        final long _tmp_1 = __converters.fromLocalDateTime(entity.getDeadline());
        statement.bindLong(4, _tmp_1);
        final String _tmp_2 = __converters.fromTaskStatus(entity.getStatus());
        if (_tmp_2 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_2);
        }
        if (entity.getDescription() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDescription());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCategory());
        }
        final int _tmp_3 = entity.getHasImage() ? 1 : 0;
        statement.bindLong(8, _tmp_3);
        if (entity.getPredecessorIds() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getPredecessorIds());
        }
        if (entity.getRelatedIds() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getRelatedIds());
        }
        if (entity.getRichContent() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getRichContent());
        }
        statement.bindLong(12, entity.getSortOrder());
        statement.bindLong(13, entity.getCreatedAt());
        if (entity.getCompletedAt() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getCompletedAt());
        }
      }
    };
    this.__deletionAdapterOfTaskEntity = new EntityDeletionOrUpdateAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `tasks` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTaskEntity = new EntityDeletionOrUpdateAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `tasks` SET `id` = ?,`title` = ?,`priority` = ?,`deadline` = ?,`status` = ?,`description` = ?,`category` = ?,`hasImage` = ?,`predecessorIds` = ?,`relatedIds` = ?,`richContent` = ?,`sortOrder` = ?,`createdAt` = ?,`completedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        final String _tmp = __converters.fromPriority(entity.getPriority());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        final long _tmp_1 = __converters.fromLocalDateTime(entity.getDeadline());
        statement.bindLong(4, _tmp_1);
        final String _tmp_2 = __converters.fromTaskStatus(entity.getStatus());
        if (_tmp_2 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_2);
        }
        if (entity.getDescription() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDescription());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCategory());
        }
        final int _tmp_3 = entity.getHasImage() ? 1 : 0;
        statement.bindLong(8, _tmp_3);
        if (entity.getPredecessorIds() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getPredecessorIds());
        }
        if (entity.getRelatedIds() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getRelatedIds());
        }
        if (entity.getRichContent() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getRichContent());
        }
        statement.bindLong(12, entity.getSortOrder());
        statement.bindLong(13, entity.getCreatedAt());
        if (entity.getCompletedAt() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getCompletedAt());
        }
        statement.bindLong(15, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateSortOrder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE tasks SET sortOrder = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final TaskEntity task, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTaskEntity.insertAndReturnId(task);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object delete(final TaskEntity task, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTaskEntity.handle(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final TaskEntity task, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTaskEntity.handle(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateSortOrder(final long id, final int sortOrder,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSortOrder.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, sortOrder);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateSortOrder.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<TaskEntity>> getAll() {
    final String _sql = "SELECT * FROM tasks ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfHasImage = CursorUtil.getColumnIndexOrThrow(_cursor, "hasImage");
          final int _cursorIndexOfPredecessorIds = CursorUtil.getColumnIndexOrThrow(_cursor, "predecessorIds");
          final int _cursorIndexOfRelatedIds = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedIds");
          final int _cursorIndexOfRichContent = CursorUtil.getColumnIndexOrThrow(_cursor, "richContent");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final Priority _tmpPriority;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfPriority)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfPriority);
            }
            _tmpPriority = __converters.toPriority(_tmp);
            final LocalDateTime _tmpDeadline;
            final long _tmp_1;
            _tmp_1 = _cursor.getLong(_cursorIndexOfDeadline);
            _tmpDeadline = __converters.toLocalDateTime(_tmp_1);
            final TaskStatus _tmpStatus;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfStatus);
            }
            _tmpStatus = __converters.toTaskStatus(_tmp_2);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpHasImage;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfHasImage);
            _tmpHasImage = _tmp_3 != 0;
            final String _tmpPredecessorIds;
            if (_cursor.isNull(_cursorIndexOfPredecessorIds)) {
              _tmpPredecessorIds = null;
            } else {
              _tmpPredecessorIds = _cursor.getString(_cursorIndexOfPredecessorIds);
            }
            final String _tmpRelatedIds;
            if (_cursor.isNull(_cursorIndexOfRelatedIds)) {
              _tmpRelatedIds = null;
            } else {
              _tmpRelatedIds = _cursor.getString(_cursorIndexOfRelatedIds);
            }
            final String _tmpRichContent;
            if (_cursor.isNull(_cursorIndexOfRichContent)) {
              _tmpRichContent = null;
            } else {
              _tmpRichContent = _cursor.getString(_cursorIndexOfRichContent);
            }
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpPriority,_tmpDeadline,_tmpStatus,_tmpDescription,_tmpCategory,_tmpHasImage,_tmpPredecessorIds,_tmpRelatedIds,_tmpRichContent,_tmpSortOrder,_tmpCreatedAt,_tmpCompletedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getById(final long id, final Continuation<? super TaskEntity> continuation) {
    final String _sql = "SELECT * FROM tasks WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TaskEntity>() {
      @Override
      @Nullable
      public TaskEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfHasImage = CursorUtil.getColumnIndexOrThrow(_cursor, "hasImage");
          final int _cursorIndexOfPredecessorIds = CursorUtil.getColumnIndexOrThrow(_cursor, "predecessorIds");
          final int _cursorIndexOfRelatedIds = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedIds");
          final int _cursorIndexOfRichContent = CursorUtil.getColumnIndexOrThrow(_cursor, "richContent");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final TaskEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final Priority _tmpPriority;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfPriority)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfPriority);
            }
            _tmpPriority = __converters.toPriority(_tmp);
            final LocalDateTime _tmpDeadline;
            final long _tmp_1;
            _tmp_1 = _cursor.getLong(_cursorIndexOfDeadline);
            _tmpDeadline = __converters.toLocalDateTime(_tmp_1);
            final TaskStatus _tmpStatus;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfStatus);
            }
            _tmpStatus = __converters.toTaskStatus(_tmp_2);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpHasImage;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfHasImage);
            _tmpHasImage = _tmp_3 != 0;
            final String _tmpPredecessorIds;
            if (_cursor.isNull(_cursorIndexOfPredecessorIds)) {
              _tmpPredecessorIds = null;
            } else {
              _tmpPredecessorIds = _cursor.getString(_cursorIndexOfPredecessorIds);
            }
            final String _tmpRelatedIds;
            if (_cursor.isNull(_cursorIndexOfRelatedIds)) {
              _tmpRelatedIds = null;
            } else {
              _tmpRelatedIds = _cursor.getString(_cursorIndexOfRelatedIds);
            }
            final String _tmpRichContent;
            if (_cursor.isNull(_cursorIndexOfRichContent)) {
              _tmpRichContent = null;
            } else {
              _tmpRichContent = _cursor.getString(_cursorIndexOfRichContent);
            }
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _result = new TaskEntity(_tmpId,_tmpTitle,_tmpPriority,_tmpDeadline,_tmpStatus,_tmpDescription,_tmpCategory,_tmpHasImage,_tmpPredecessorIds,_tmpRelatedIds,_tmpRichContent,_tmpSortOrder,_tmpCreatedAt,_tmpCompletedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<Integer> countAll() {
    final String _sql = "SELECT COUNT(*) FROM tasks";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> countInProgress() {
    final String _sql = "SELECT COUNT(*) FROM tasks WHERE status = 'IN_PROGRESS'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> countDone() {
    final String _sql = "SELECT COUNT(*) FROM tasks WHERE status = 'DONE'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> countOverdue(final long todayEpochDay) {
    final String _sql = "SELECT COUNT(*) FROM tasks WHERE status != 'DONE' AND deadline < ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, todayEpochDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TaskEntity>> searchAndFilter(final String query, final String priorityName) {
    final String _sql = "\n"
            + "        SELECT * FROM tasks\n"
            + "        WHERE title LIKE '%' || ? || '%'\n"
            + "        AND (? IS NULL OR priority = ?)\n"
            + "        ORDER BY id DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 2;
    if (priorityName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, priorityName);
    }
    _argIndex = 3;
    if (priorityName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, priorityName);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfHasImage = CursorUtil.getColumnIndexOrThrow(_cursor, "hasImage");
          final int _cursorIndexOfPredecessorIds = CursorUtil.getColumnIndexOrThrow(_cursor, "predecessorIds");
          final int _cursorIndexOfRelatedIds = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedIds");
          final int _cursorIndexOfRichContent = CursorUtil.getColumnIndexOrThrow(_cursor, "richContent");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final Priority _tmpPriority;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfPriority)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfPriority);
            }
            _tmpPriority = __converters.toPriority(_tmp);
            final LocalDateTime _tmpDeadline;
            final long _tmp_1;
            _tmp_1 = _cursor.getLong(_cursorIndexOfDeadline);
            _tmpDeadline = __converters.toLocalDateTime(_tmp_1);
            final TaskStatus _tmpStatus;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfStatus);
            }
            _tmpStatus = __converters.toTaskStatus(_tmp_2);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpHasImage;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfHasImage);
            _tmpHasImage = _tmp_3 != 0;
            final String _tmpPredecessorIds;
            if (_cursor.isNull(_cursorIndexOfPredecessorIds)) {
              _tmpPredecessorIds = null;
            } else {
              _tmpPredecessorIds = _cursor.getString(_cursorIndexOfPredecessorIds);
            }
            final String _tmpRelatedIds;
            if (_cursor.isNull(_cursorIndexOfRelatedIds)) {
              _tmpRelatedIds = null;
            } else {
              _tmpRelatedIds = _cursor.getString(_cursorIndexOfRelatedIds);
            }
            final String _tmpRichContent;
            if (_cursor.isNull(_cursorIndexOfRichContent)) {
              _tmpRichContent = null;
            } else {
              _tmpRichContent = _cursor.getString(_cursorIndexOfRichContent);
            }
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpPriority,_tmpDeadline,_tmpStatus,_tmpDescription,_tmpCategory,_tmpHasImage,_tmpPredecessorIds,_tmpRelatedIds,_tmpRichContent,_tmpSortOrder,_tmpCreatedAt,_tmpCompletedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TaskEntity>> getTodayTasks(final long todayStartMillis,
      final long todayEndMillis) {
    final String _sql = "\n"
            + "        SELECT * FROM tasks\n"
            + "        WHERE deadline >= ?\n"
            + "        AND deadline < ?\n"
            + "        ORDER BY\n"
            + "            CASE WHEN status = 'DONE' THEN 1 ELSE 0 END,\n"
            + "            CASE priority\n"
            + "                WHEN 'HIGH' THEN 0\n"
            + "                WHEN 'MEDIUM' THEN 1\n"
            + "                WHEN 'NORMAL' THEN 2\n"
            + "                WHEN 'LOW' THEN 3\n"
            + "                ELSE 4\n"
            + "            END,\n"
            + "            sortOrder ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, todayStartMillis);
    _argIndex = 2;
    _statement.bindLong(_argIndex, todayEndMillis);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfHasImage = CursorUtil.getColumnIndexOrThrow(_cursor, "hasImage");
          final int _cursorIndexOfPredecessorIds = CursorUtil.getColumnIndexOrThrow(_cursor, "predecessorIds");
          final int _cursorIndexOfRelatedIds = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedIds");
          final int _cursorIndexOfRichContent = CursorUtil.getColumnIndexOrThrow(_cursor, "richContent");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final Priority _tmpPriority;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfPriority)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfPriority);
            }
            _tmpPriority = __converters.toPriority(_tmp);
            final LocalDateTime _tmpDeadline;
            final long _tmp_1;
            _tmp_1 = _cursor.getLong(_cursorIndexOfDeadline);
            _tmpDeadline = __converters.toLocalDateTime(_tmp_1);
            final TaskStatus _tmpStatus;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfStatus);
            }
            _tmpStatus = __converters.toTaskStatus(_tmp_2);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpHasImage;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfHasImage);
            _tmpHasImage = _tmp_3 != 0;
            final String _tmpPredecessorIds;
            if (_cursor.isNull(_cursorIndexOfPredecessorIds)) {
              _tmpPredecessorIds = null;
            } else {
              _tmpPredecessorIds = _cursor.getString(_cursorIndexOfPredecessorIds);
            }
            final String _tmpRelatedIds;
            if (_cursor.isNull(_cursorIndexOfRelatedIds)) {
              _tmpRelatedIds = null;
            } else {
              _tmpRelatedIds = _cursor.getString(_cursorIndexOfRelatedIds);
            }
            final String _tmpRichContent;
            if (_cursor.isNull(_cursorIndexOfRichContent)) {
              _tmpRichContent = null;
            } else {
              _tmpRichContent = _cursor.getString(_cursorIndexOfRichContent);
            }
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpPriority,_tmpDeadline,_tmpStatus,_tmpDescription,_tmpCategory,_tmpHasImage,_tmpPredecessorIds,_tmpRelatedIds,_tmpRichContent,_tmpSortOrder,_tmpCreatedAt,_tmpCompletedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TaskEntity>> getPriorityTasks(final long nowMillis) {
    final String _sql = "\n"
            + "        SELECT * FROM tasks\n"
            + "        WHERE priority IN ('HIGH', 'MEDIUM')\n"
            + "        AND status != 'DONE'\n"
            + "        ORDER BY\n"
            + "            CASE WHEN deadline < ? THEN 0 ELSE 1 END,\n"
            + "            CASE priority\n"
            + "                WHEN 'HIGH' THEN 0\n"
            + "                WHEN 'MEDIUM' THEN 1\n"
            + "                ELSE 2\n"
            + "            END,\n"
            + "            sortOrder ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, nowMillis);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfHasImage = CursorUtil.getColumnIndexOrThrow(_cursor, "hasImage");
          final int _cursorIndexOfPredecessorIds = CursorUtil.getColumnIndexOrThrow(_cursor, "predecessorIds");
          final int _cursorIndexOfRelatedIds = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedIds");
          final int _cursorIndexOfRichContent = CursorUtil.getColumnIndexOrThrow(_cursor, "richContent");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final Priority _tmpPriority;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfPriority)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfPriority);
            }
            _tmpPriority = __converters.toPriority(_tmp);
            final LocalDateTime _tmpDeadline;
            final long _tmp_1;
            _tmp_1 = _cursor.getLong(_cursorIndexOfDeadline);
            _tmpDeadline = __converters.toLocalDateTime(_tmp_1);
            final TaskStatus _tmpStatus;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfStatus);
            }
            _tmpStatus = __converters.toTaskStatus(_tmp_2);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpHasImage;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfHasImage);
            _tmpHasImage = _tmp_3 != 0;
            final String _tmpPredecessorIds;
            if (_cursor.isNull(_cursorIndexOfPredecessorIds)) {
              _tmpPredecessorIds = null;
            } else {
              _tmpPredecessorIds = _cursor.getString(_cursorIndexOfPredecessorIds);
            }
            final String _tmpRelatedIds;
            if (_cursor.isNull(_cursorIndexOfRelatedIds)) {
              _tmpRelatedIds = null;
            } else {
              _tmpRelatedIds = _cursor.getString(_cursorIndexOfRelatedIds);
            }
            final String _tmpRichContent;
            if (_cursor.isNull(_cursorIndexOfRichContent)) {
              _tmpRichContent = null;
            } else {
              _tmpRichContent = _cursor.getString(_cursorIndexOfRichContent);
            }
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpPriority,_tmpDeadline,_tmpStatus,_tmpDescription,_tmpCategory,_tmpHasImage,_tmpPredecessorIds,_tmpRelatedIds,_tmpRichContent,_tmpSortOrder,_tmpCreatedAt,_tmpCompletedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TaskEntity>> filterAllTasks(final String query, final String priorityName,
      final String statusName, final Boolean hasImage) {
    final String _sql = "\n"
            + "        SELECT * FROM tasks\n"
            + "        WHERE (? IS NULL OR title LIKE '%' || ? || '%')\n"
            + "        AND (? IS NULL OR priority = ?)\n"
            + "        AND (? IS NULL OR status = ?)\n"
            + "        AND (? IS NULL OR hasImage = ?)\n"
            + "        ORDER BY id DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 8);
    int _argIndex = 1;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 2;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 3;
    if (priorityName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, priorityName);
    }
    _argIndex = 4;
    if (priorityName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, priorityName);
    }
    _argIndex = 5;
    if (statusName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, statusName);
    }
    _argIndex = 6;
    if (statusName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, statusName);
    }
    _argIndex = 7;
    final Integer _tmp = hasImage == null ? null : (hasImage ? 1 : 0);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 8;
    final Integer _tmp_1 = hasImage == null ? null : (hasImage ? 1 : 0);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp_1);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfHasImage = CursorUtil.getColumnIndexOrThrow(_cursor, "hasImage");
          final int _cursorIndexOfPredecessorIds = CursorUtil.getColumnIndexOrThrow(_cursor, "predecessorIds");
          final int _cursorIndexOfRelatedIds = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedIds");
          final int _cursorIndexOfRichContent = CursorUtil.getColumnIndexOrThrow(_cursor, "richContent");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final Priority _tmpPriority;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfPriority)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfPriority);
            }
            _tmpPriority = __converters.toPriority(_tmp_2);
            final LocalDateTime _tmpDeadline;
            final long _tmp_3;
            _tmp_3 = _cursor.getLong(_cursorIndexOfDeadline);
            _tmpDeadline = __converters.toLocalDateTime(_tmp_3);
            final TaskStatus _tmpStatus;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfStatus);
            }
            _tmpStatus = __converters.toTaskStatus(_tmp_4);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpHasImage;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfHasImage);
            _tmpHasImage = _tmp_5 != 0;
            final String _tmpPredecessorIds;
            if (_cursor.isNull(_cursorIndexOfPredecessorIds)) {
              _tmpPredecessorIds = null;
            } else {
              _tmpPredecessorIds = _cursor.getString(_cursorIndexOfPredecessorIds);
            }
            final String _tmpRelatedIds;
            if (_cursor.isNull(_cursorIndexOfRelatedIds)) {
              _tmpRelatedIds = null;
            } else {
              _tmpRelatedIds = _cursor.getString(_cursorIndexOfRelatedIds);
            }
            final String _tmpRichContent;
            if (_cursor.isNull(_cursorIndexOfRichContent)) {
              _tmpRichContent = null;
            } else {
              _tmpRichContent = _cursor.getString(_cursorIndexOfRichContent);
            }
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpPriority,_tmpDeadline,_tmpStatus,_tmpDescription,_tmpCategory,_tmpHasImage,_tmpPredecessorIds,_tmpRelatedIds,_tmpRichContent,_tmpSortOrder,_tmpCreatedAt,_tmpCompletedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TaskEntity>> getLedgerTasks(final long startDateEpoch, final long endDateEpoch,
      final boolean includeArchived, final String category) {
    final String _sql = "\n"
            + "        SELECT * FROM tasks\n"
            + "        WHERE (? = 1 OR status != 'DONE')\n"
            + "        AND (? IS NULL OR category = ?)\n"
            + "        AND deadline >= ?\n"
            + "        AND deadline <= ?\n"
            + "        ORDER BY deadline DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 5);
    int _argIndex = 1;
    final int _tmp = includeArchived ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    _argIndex = 2;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    _argIndex = 3;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    _argIndex = 4;
    _statement.bindLong(_argIndex, startDateEpoch);
    _argIndex = 5;
    _statement.bindLong(_argIndex, endDateEpoch);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfHasImage = CursorUtil.getColumnIndexOrThrow(_cursor, "hasImage");
          final int _cursorIndexOfPredecessorIds = CursorUtil.getColumnIndexOrThrow(_cursor, "predecessorIds");
          final int _cursorIndexOfRelatedIds = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedIds");
          final int _cursorIndexOfRichContent = CursorUtil.getColumnIndexOrThrow(_cursor, "richContent");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final Priority _tmpPriority;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfPriority)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfPriority);
            }
            _tmpPriority = __converters.toPriority(_tmp_1);
            final LocalDateTime _tmpDeadline;
            final long _tmp_2;
            _tmp_2 = _cursor.getLong(_cursorIndexOfDeadline);
            _tmpDeadline = __converters.toLocalDateTime(_tmp_2);
            final TaskStatus _tmpStatus;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfStatus);
            }
            _tmpStatus = __converters.toTaskStatus(_tmp_3);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpHasImage;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfHasImage);
            _tmpHasImage = _tmp_4 != 0;
            final String _tmpPredecessorIds;
            if (_cursor.isNull(_cursorIndexOfPredecessorIds)) {
              _tmpPredecessorIds = null;
            } else {
              _tmpPredecessorIds = _cursor.getString(_cursorIndexOfPredecessorIds);
            }
            final String _tmpRelatedIds;
            if (_cursor.isNull(_cursorIndexOfRelatedIds)) {
              _tmpRelatedIds = null;
            } else {
              _tmpRelatedIds = _cursor.getString(_cursorIndexOfRelatedIds);
            }
            final String _tmpRichContent;
            if (_cursor.isNull(_cursorIndexOfRichContent)) {
              _tmpRichContent = null;
            } else {
              _tmpRichContent = _cursor.getString(_cursorIndexOfRichContent);
            }
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpPriority,_tmpDeadline,_tmpStatus,_tmpDescription,_tmpCategory,_tmpHasImage,_tmpPredecessorIds,_tmpRelatedIds,_tmpRichContent,_tmpSortOrder,_tmpCreatedAt,_tmpCompletedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<String>> getAllCategories() {
    final String _sql = "SELECT DISTINCT category FROM tasks WHERE category != ''";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            if (_cursor.isNull(0)) {
              _item = null;
            } else {
              _item = _cursor.getString(0);
            }
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TaskEntity>> getTasksByIds(final List<Long> ids) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM tasks WHERE id IN (");
    final int _inputSize = ids.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (Long _item : ids) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindLong(_argIndex, _item);
      }
      _argIndex++;
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfHasImage = CursorUtil.getColumnIndexOrThrow(_cursor, "hasImage");
          final int _cursorIndexOfPredecessorIds = CursorUtil.getColumnIndexOrThrow(_cursor, "predecessorIds");
          final int _cursorIndexOfRelatedIds = CursorUtil.getColumnIndexOrThrow(_cursor, "relatedIds");
          final int _cursorIndexOfRichContent = CursorUtil.getColumnIndexOrThrow(_cursor, "richContent");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item_1;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final Priority _tmpPriority;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfPriority)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfPriority);
            }
            _tmpPriority = __converters.toPriority(_tmp);
            final LocalDateTime _tmpDeadline;
            final long _tmp_1;
            _tmp_1 = _cursor.getLong(_cursorIndexOfDeadline);
            _tmpDeadline = __converters.toLocalDateTime(_tmp_1);
            final TaskStatus _tmpStatus;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfStatus);
            }
            _tmpStatus = __converters.toTaskStatus(_tmp_2);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpHasImage;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfHasImage);
            _tmpHasImage = _tmp_3 != 0;
            final String _tmpPredecessorIds;
            if (_cursor.isNull(_cursorIndexOfPredecessorIds)) {
              _tmpPredecessorIds = null;
            } else {
              _tmpPredecessorIds = _cursor.getString(_cursorIndexOfPredecessorIds);
            }
            final String _tmpRelatedIds;
            if (_cursor.isNull(_cursorIndexOfRelatedIds)) {
              _tmpRelatedIds = null;
            } else {
              _tmpRelatedIds = _cursor.getString(_cursorIndexOfRelatedIds);
            }
            final String _tmpRichContent;
            if (_cursor.isNull(_cursorIndexOfRichContent)) {
              _tmpRichContent = null;
            } else {
              _tmpRichContent = _cursor.getString(_cursorIndexOfRichContent);
            }
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _item_1 = new TaskEntity(_tmpId,_tmpTitle,_tmpPriority,_tmpDeadline,_tmpStatus,_tmpDescription,_tmpCategory,_tmpHasImage,_tmpPredecessorIds,_tmpRelatedIds,_tmpRichContent,_tmpSortOrder,_tmpCreatedAt,_tmpCompletedAt);
            _result.add(_item_1);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
