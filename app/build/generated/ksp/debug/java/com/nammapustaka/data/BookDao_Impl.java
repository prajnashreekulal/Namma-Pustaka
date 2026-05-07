package com.nammapustaka.data;

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
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
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
public final class BookDao_Impl implements BookDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Book> __insertionAdapterOfBook;

  private final EntityInsertionAdapter<Student> __insertionAdapterOfStudent;

  private final EntityInsertionAdapter<BorrowHistory> __insertionAdapterOfBorrowHistory;

  private final EntityDeletionOrUpdateAdapter<Book> __updateAdapterOfBook;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatusAndBorrower;

  private final SharedSQLiteStatement __preparedStmtOfReturnBookCheckout;

  private final SharedSQLiteStatement __preparedStmtOfAddScoreToStudent;

  public BookDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBook = new EntityInsertionAdapter<Book>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `books` (`id`,`title`,`author`,`status`,`dueDate`,`borrowerId`,`category`,`coverImageUrl`,`isbn`,`shelfLocation`,`imageUri`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Book entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getAuthor());
        statement.bindString(4, entity.getStatus());
        if (entity.getDueDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getDueDate());
        }
        if (entity.getBorrowerId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getBorrowerId());
        }
        statement.bindString(7, entity.getCategory());
        if (entity.getCoverImageUrl() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getCoverImageUrl());
        }
        if (entity.getIsbn() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getIsbn());
        }
        if (entity.getShelfLocation() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getShelfLocation());
        }
        if (entity.getImageUri() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getImageUri());
        }
      }
    };
    this.__insertionAdapterOfStudent = new EntityInsertionAdapter<Student>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `students` (`id`,`name`,`score`,`booksRead`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Student entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getScore());
        statement.bindLong(4, entity.getBooksRead());
      }
    };
    this.__insertionAdapterOfBorrowHistory = new EntityInsertionAdapter<BorrowHistory>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `borrow_history` (`id`,`bookId`,`bookTitle`,`borrowDate`,`studentId`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BorrowHistory entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBookId());
        statement.bindString(3, entity.getBookTitle());
        statement.bindLong(4, entity.getBorrowDate());
        statement.bindLong(5, entity.getStudentId());
      }
    };
    this.__updateAdapterOfBook = new EntityDeletionOrUpdateAdapter<Book>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `books` SET `id` = ?,`title` = ?,`author` = ?,`status` = ?,`dueDate` = ?,`borrowerId` = ?,`category` = ?,`coverImageUrl` = ?,`isbn` = ?,`shelfLocation` = ?,`imageUri` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Book entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getAuthor());
        statement.bindString(4, entity.getStatus());
        if (entity.getDueDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getDueDate());
        }
        if (entity.getBorrowerId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getBorrowerId());
        }
        statement.bindString(7, entity.getCategory());
        if (entity.getCoverImageUrl() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getCoverImageUrl());
        }
        if (entity.getIsbn() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getIsbn());
        }
        if (entity.getShelfLocation() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getShelfLocation());
        }
        if (entity.getImageUri() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getImageUri());
        }
        statement.bindLong(12, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateStatusAndBorrower = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE books SET status = ?, borrowerId = ?, dueDate = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfReturnBookCheckout = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE books SET status = 'Available', borrowerId = NULL, dueDate = NULL WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfAddScoreToStudent = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE students SET booksRead = booksRead + 1, score = score + ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertBook(final Book book, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBook.insert(book);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertStudent(final Student student, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfStudent.insert(student);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBorrowHistory(final BorrowHistory history,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBorrowHistory.insert(history);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBook(final Book book, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBook.handle(book);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStatusAndBorrower(final int bookId, final String status,
      final Integer borrowerId, final Long dueDate, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatusAndBorrower.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        if (borrowerId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, borrowerId);
        }
        _argIndex = 3;
        if (dueDate == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, dueDate);
        }
        _argIndex = 4;
        _stmt.bindLong(_argIndex, bookId);
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
          __preparedStmtOfUpdateStatusAndBorrower.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object returnBookCheckout(final int bookId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfReturnBookCheckout.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, bookId);
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
          __preparedStmtOfReturnBookCheckout.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object addScoreToStudent(final int studentId, final int points,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAddScoreToStudent.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, points);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, studentId);
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
          __preparedStmtOfAddScoreToStudent.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Book>> getAllBooks() {
    final String _sql = "SELECT * FROM books ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<Book>>() {
      @Override
      @NonNull
      public List<Book> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfBorrowerId = CursorUtil.getColumnIndexOrThrow(_cursor, "borrowerId");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCoverImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImageUrl");
          final int _cursorIndexOfIsbn = CursorUtil.getColumnIndexOrThrow(_cursor, "isbn");
          final int _cursorIndexOfShelfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "shelfLocation");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Book _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final Integer _tmpBorrowerId;
            if (_cursor.isNull(_cursorIndexOfBorrowerId)) {
              _tmpBorrowerId = null;
            } else {
              _tmpBorrowerId = _cursor.getInt(_cursorIndexOfBorrowerId);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpCoverImageUrl;
            if (_cursor.isNull(_cursorIndexOfCoverImageUrl)) {
              _tmpCoverImageUrl = null;
            } else {
              _tmpCoverImageUrl = _cursor.getString(_cursorIndexOfCoverImageUrl);
            }
            final String _tmpIsbn;
            if (_cursor.isNull(_cursorIndexOfIsbn)) {
              _tmpIsbn = null;
            } else {
              _tmpIsbn = _cursor.getString(_cursorIndexOfIsbn);
            }
            final String _tmpShelfLocation;
            if (_cursor.isNull(_cursorIndexOfShelfLocation)) {
              _tmpShelfLocation = null;
            } else {
              _tmpShelfLocation = _cursor.getString(_cursorIndexOfShelfLocation);
            }
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            _item = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpStatus,_tmpDueDate,_tmpBorrowerId,_tmpCategory,_tmpCoverImageUrl,_tmpIsbn,_tmpShelfLocation,_tmpImageUri);
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
  public Flow<List<Book>> getBorrowedBooks() {
    final String _sql = "SELECT * FROM books WHERE status = 'Borrowed'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<Book>>() {
      @Override
      @NonNull
      public List<Book> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfBorrowerId = CursorUtil.getColumnIndexOrThrow(_cursor, "borrowerId");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCoverImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImageUrl");
          final int _cursorIndexOfIsbn = CursorUtil.getColumnIndexOrThrow(_cursor, "isbn");
          final int _cursorIndexOfShelfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "shelfLocation");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Book _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final Integer _tmpBorrowerId;
            if (_cursor.isNull(_cursorIndexOfBorrowerId)) {
              _tmpBorrowerId = null;
            } else {
              _tmpBorrowerId = _cursor.getInt(_cursorIndexOfBorrowerId);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpCoverImageUrl;
            if (_cursor.isNull(_cursorIndexOfCoverImageUrl)) {
              _tmpCoverImageUrl = null;
            } else {
              _tmpCoverImageUrl = _cursor.getString(_cursorIndexOfCoverImageUrl);
            }
            final String _tmpIsbn;
            if (_cursor.isNull(_cursorIndexOfIsbn)) {
              _tmpIsbn = null;
            } else {
              _tmpIsbn = _cursor.getString(_cursorIndexOfIsbn);
            }
            final String _tmpShelfLocation;
            if (_cursor.isNull(_cursorIndexOfShelfLocation)) {
              _tmpShelfLocation = null;
            } else {
              _tmpShelfLocation = _cursor.getString(_cursorIndexOfShelfLocation);
            }
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            _item = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpStatus,_tmpDueDate,_tmpBorrowerId,_tmpCategory,_tmpCoverImageUrl,_tmpIsbn,_tmpShelfLocation,_tmpImageUri);
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
  public Flow<List<Book>> searchBooksByTitle(final String query) {
    final String _sql = "SELECT * FROM books WHERE title LIKE '%' || ? || '%' ORDER BY title ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<Book>>() {
      @Override
      @NonNull
      public List<Book> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfBorrowerId = CursorUtil.getColumnIndexOrThrow(_cursor, "borrowerId");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCoverImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImageUrl");
          final int _cursorIndexOfIsbn = CursorUtil.getColumnIndexOrThrow(_cursor, "isbn");
          final int _cursorIndexOfShelfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "shelfLocation");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Book _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final Integer _tmpBorrowerId;
            if (_cursor.isNull(_cursorIndexOfBorrowerId)) {
              _tmpBorrowerId = null;
            } else {
              _tmpBorrowerId = _cursor.getInt(_cursorIndexOfBorrowerId);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpCoverImageUrl;
            if (_cursor.isNull(_cursorIndexOfCoverImageUrl)) {
              _tmpCoverImageUrl = null;
            } else {
              _tmpCoverImageUrl = _cursor.getString(_cursorIndexOfCoverImageUrl);
            }
            final String _tmpIsbn;
            if (_cursor.isNull(_cursorIndexOfIsbn)) {
              _tmpIsbn = null;
            } else {
              _tmpIsbn = _cursor.getString(_cursorIndexOfIsbn);
            }
            final String _tmpShelfLocation;
            if (_cursor.isNull(_cursorIndexOfShelfLocation)) {
              _tmpShelfLocation = null;
            } else {
              _tmpShelfLocation = _cursor.getString(_cursorIndexOfShelfLocation);
            }
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            _item = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpStatus,_tmpDueDate,_tmpBorrowerId,_tmpCategory,_tmpCoverImageUrl,_tmpIsbn,_tmpShelfLocation,_tmpImageUri);
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
  public Object getBookById(final int bookId, final Continuation<? super Book> $completion) {
    final String _sql = "SELECT * FROM books WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Book>() {
      @Override
      @Nullable
      public Book call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfBorrowerId = CursorUtil.getColumnIndexOrThrow(_cursor, "borrowerId");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCoverImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImageUrl");
          final int _cursorIndexOfIsbn = CursorUtil.getColumnIndexOrThrow(_cursor, "isbn");
          final int _cursorIndexOfShelfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "shelfLocation");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final Book _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final Long _tmpDueDate;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmpDueDate = null;
            } else {
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            }
            final Integer _tmpBorrowerId;
            if (_cursor.isNull(_cursorIndexOfBorrowerId)) {
              _tmpBorrowerId = null;
            } else {
              _tmpBorrowerId = _cursor.getInt(_cursorIndexOfBorrowerId);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpCoverImageUrl;
            if (_cursor.isNull(_cursorIndexOfCoverImageUrl)) {
              _tmpCoverImageUrl = null;
            } else {
              _tmpCoverImageUrl = _cursor.getString(_cursorIndexOfCoverImageUrl);
            }
            final String _tmpIsbn;
            if (_cursor.isNull(_cursorIndexOfIsbn)) {
              _tmpIsbn = null;
            } else {
              _tmpIsbn = _cursor.getString(_cursorIndexOfIsbn);
            }
            final String _tmpShelfLocation;
            if (_cursor.isNull(_cursorIndexOfShelfLocation)) {
              _tmpShelfLocation = null;
            } else {
              _tmpShelfLocation = _cursor.getString(_cursorIndexOfShelfLocation);
            }
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            _result = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpStatus,_tmpDueDate,_tmpBorrowerId,_tmpCategory,_tmpCoverImageUrl,_tmpIsbn,_tmpShelfLocation,_tmpImageUri);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Student>> getLeaderboard() {
    final String _sql = "SELECT * FROM students ORDER BY score DESC, booksRead DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"students"}, new Callable<List<Student>>() {
      @Override
      @NonNull
      public List<Student> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfBooksRead = CursorUtil.getColumnIndexOrThrow(_cursor, "booksRead");
          final List<Student> _result = new ArrayList<Student>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Student _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpScore;
            _tmpScore = _cursor.getInt(_cursorIndexOfScore);
            final int _tmpBooksRead;
            _tmpBooksRead = _cursor.getInt(_cursorIndexOfBooksRead);
            _item = new Student(_tmpId,_tmpName,_tmpScore,_tmpBooksRead);
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
  public Flow<Student> getCurrentStudent() {
    final String _sql = "SELECT * FROM students LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"students"}, new Callable<Student>() {
      @Override
      @Nullable
      public Student call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfBooksRead = CursorUtil.getColumnIndexOrThrow(_cursor, "booksRead");
          final Student _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpScore;
            _tmpScore = _cursor.getInt(_cursorIndexOfScore);
            final int _tmpBooksRead;
            _tmpBooksRead = _cursor.getInt(_cursorIndexOfBooksRead);
            _result = new Student(_tmpId,_tmpName,_tmpScore,_tmpBooksRead);
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
  public Flow<Integer> getTotalUsersCount() {
    final String _sql = "SELECT COUNT(*) FROM students";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"students"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
  public Flow<List<BorrowHistory>> getBorrowHistoryForUser(final int studentId) {
    final String _sql = "SELECT * FROM borrow_history WHERE studentId = ? ORDER BY borrowDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, studentId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"borrow_history"}, new Callable<List<BorrowHistory>>() {
      @Override
      @NonNull
      public List<BorrowHistory> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfBookTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "bookTitle");
          final int _cursorIndexOfBorrowDate = CursorUtil.getColumnIndexOrThrow(_cursor, "borrowDate");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final List<BorrowHistory> _result = new ArrayList<BorrowHistory>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BorrowHistory _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpBookId;
            _tmpBookId = _cursor.getInt(_cursorIndexOfBookId);
            final String _tmpBookTitle;
            _tmpBookTitle = _cursor.getString(_cursorIndexOfBookTitle);
            final long _tmpBorrowDate;
            _tmpBorrowDate = _cursor.getLong(_cursorIndexOfBorrowDate);
            final int _tmpStudentId;
            _tmpStudentId = _cursor.getInt(_cursorIndexOfStudentId);
            _item = new BorrowHistory(_tmpId,_tmpBookId,_tmpBookTitle,_tmpBorrowDate,_tmpStudentId);
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
  public Flow<List<BorrowHistory>> getAllBorrowHistory() {
    final String _sql = "SELECT * FROM borrow_history ORDER BY borrowDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"borrow_history"}, new Callable<List<BorrowHistory>>() {
      @Override
      @NonNull
      public List<BorrowHistory> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfBookTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "bookTitle");
          final int _cursorIndexOfBorrowDate = CursorUtil.getColumnIndexOrThrow(_cursor, "borrowDate");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final List<BorrowHistory> _result = new ArrayList<BorrowHistory>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BorrowHistory _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpBookId;
            _tmpBookId = _cursor.getInt(_cursorIndexOfBookId);
            final String _tmpBookTitle;
            _tmpBookTitle = _cursor.getString(_cursorIndexOfBookTitle);
            final long _tmpBorrowDate;
            _tmpBorrowDate = _cursor.getLong(_cursorIndexOfBorrowDate);
            final int _tmpStudentId;
            _tmpStudentId = _cursor.getInt(_cursorIndexOfStudentId);
            _item = new BorrowHistory(_tmpId,_tmpBookId,_tmpBookTitle,_tmpBorrowDate,_tmpStudentId);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
