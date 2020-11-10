package com.xrzx.commonlibrary.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xrzx.commonlibrary.database.dao.base.BaseDao;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.utils.SQLiteUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description 书籍信息Dao
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class BookInfoDao extends BaseDao {
    private static final String TABLE_NAME = Book.class.getSimpleName().toLowerCase();
    private static final String[] COLUMNS = SQLiteUtils.getDeclaredFieldsPressSqlType(Book.class);

    /**
     * 创建数据表
     *
     * @param db
     */
    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateSql(TABLE_NAME, Book.class));
    }

    public static Book find(String selection, String... selectionVal) {
        final List<Book> all = findAll(TABLE_NAME, COLUMNS, selection, selectionVal, null, null, null);
        if (all.isEmpty()) {
            return null;
        }
        return all.get(0);
    }

    public static ArrayList<Book> findAllOrderBy(String orderBy) {
        return findAll(TABLE_NAME, COLUMNS, null, null, null, null, orderBy);
    }

    public static ArrayList<Book> findSelection(String selection, String... selectionVal) {
        return findAll(TABLE_NAME, COLUMNS, selection, selectionVal, null, null, null);
    }

    /**
     * 读取数据
     *
     * @param table         数据表
     * @param columns       数据库列名
     * @param selection     选择条件
     * @param selectionArgs 选择条件的值
     * @param groupBy       分组函数
     * @param having        聚合函数
     * @param orderBy       排序
     * @return 数据列表
     */
    public static ArrayList<Book> findAll(String table, String[] columns,
                                          String selection, String[] selectionArgs,
                                          String groupBy, String having, String orderBy) {
        ArrayList<Book> bookList = new ArrayList<>();
        try (SQLiteDatabase db = CUSTOM_DATABASE_HELPER.getWritableDatabase()) {
            try (Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)) {
                List<Object> objectList = new ArrayList<>();
                if (!writeEntity(objectList, Book.class, cursor)) {
                    return null;
                }
                objectList.forEach(book -> {
                    final Book t = (Book) book;
                    t.setBookShelf(true);
                    t.setChapterList(new ArrayList<>());
                    bookList.add(t);
                });
                objectList.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public static boolean writeEntity(Book entity) {
        ContentValues contentValues = getContentValues(Book.class, entity);
        try (SQLiteDatabase db = CUSTOM_DATABASE_HELPER.getWritableDatabase()) {
            db.insert(TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            contentValues.clear();
        }
        return true;
    }

    public static boolean writeEntity(List<Book> books) {
        for (Book entity : books) {
            try {
                return writeEntity(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static int deleteEntity(Book book) {
        return delete("b_name = ? and b_author = ?", book.getbName(), book.getbAuthor());
    }

    public static int delete(String whereClause, String... whereArgs) {
        try (SQLiteDatabase db = CUSTOM_DATABASE_HELPER.getWritableDatabase()) {
            return db.delete(TABLE_NAME, whereClause, whereArgs);
        }
    }

    public static void updateBook(Book book) {
        final ContentValues contentValues = getContentValues(Book.class, book);
        try (SQLiteDatabase db = CUSTOM_DATABASE_HELPER.getWritableDatabase()) {
            db.update(TABLE_NAME, contentValues, "b_uniquely_identifies = ?", new String[]{String.valueOf(book.getbUniquelyIdentifies())});
        }
    }

    public static String getUniquelyIdentifies() {
        String uuid;
        List<Book> books;
        do {
            uuid = UUID.randomUUID().toString();
            books = BookInfoDao.findSelection("b_uniquely_identifies = ?", uuid);
        } while (!books.isEmpty());
        return uuid;
    }
}
