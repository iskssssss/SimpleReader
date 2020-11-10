package com.xrzx.commonlibrary.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xrzx.commonlibrary.database.dao.base.BaseDao;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.entity.Chapter;
import com.xrzx.commonlibrary.utils.SQLiteUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 章节信息Dao
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class ChapterInfoDao extends BaseDao {
    private static final String TABLE_NAME = Chapter.class.getSimpleName().toLowerCase();

    private static final String[] COLUMNS = SQLiteUtils.getDeclaredFieldsPressSqlType(Chapter.class);

    /**
     * 创建数据表
     *
     * @param db
     */
    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateSql(TABLE_NAME, Chapter.class));
    }

    /**
     * 写数据
     *
     * @param chapters 章节列表
     */
    public static void writeChapters(List<Chapter> chapters) {
        try (SQLiteDatabase db = CUSTOM_DATABASE_HELPER.getWritableDatabase()) {
            db.beginTransaction();
            for (Chapter chapter : chapters) {
                ContentValues contentValues = null;
                try {
                    contentValues = getContentValues(Chapter.class, chapter);
                    db.insert(TABLE_NAME, null, contentValues);
                    contentValues.clear();
                } catch (Exception e) {
                    if (null != contentValues) {
                        contentValues.clear();
                    }
                    e.printStackTrace();
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public static void writeChapter(Chapter chapter) {
        try (SQLiteDatabase db = CUSTOM_DATABASE_HELPER.getWritableDatabase()) {
            ContentValues contentValues = getContentValues(Chapter.class, chapter);
            db.insert(TABLE_NAME, null, contentValues);
            contentValues.clear();
        }
    }

    /**
     * 更新章节正文
     *
     * @param cId     章节id
     * @param content 正文
     */
    public static void updateChapterContent(String cId, String content) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("c_content", content);
        try (SQLiteDatabase db = CUSTOM_DATABASE_HELPER.getWritableDatabase()) {
            db.update(TABLE_NAME, contentValues, "c_id = ?", new String[]{cId});
        }
    }

    public static void updateChapter(Chapter chapter) {
        final ContentValues contentValues = getContentValues(Chapter.class, chapter);
        try (SQLiteDatabase db = CUSTOM_DATABASE_HELPER.getWritableDatabase()) {
            db.update(TABLE_NAME, contentValues, "c_id = ?", new String[]{String.valueOf(chapter.getcId())});
        }
    }

    public static boolean isExistChapters(Book book) {
        final List<Chapter> chapterList = findAll(TABLE_NAME, COLUMNS, "b_uniquely_identifies = ?", new String[]{book.getbUniquelyIdentifies()}, null, null, null);
        return chapterList.isEmpty();
    }

    /**
     * 读取数据
     *
     * @param book     书籍信息
     * @param chapters 章节容器
     */
    public static void readChapters(Book book, List<Chapter> chapters) {
        final List<Chapter> chapterList = findAll(TABLE_NAME, COLUMNS, "b_uniquely_identifies = ?", new String[]{book.getbUniquelyIdentifies()}, null, null, null);
        chapters.addAll(chapterList);
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
    public static List<Chapter> findAll(String table, String[] columns,
                                        String selection, String[] selectionArgs,
                                        String groupBy, String having, String orderBy) {
        final List<Chapter> chapterList = new ArrayList<>();
        List<Object> objectList = new ArrayList<>();
        try (SQLiteDatabase db = CUSTOM_DATABASE_HELPER.getReadableDatabase()) {
            try (Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)) {
                if (writeEntity(objectList, Chapter.class, cursor)) {
                    for (Object o : objectList) {
                        chapterList.add((Chapter) o);
                    }
                    objectList.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapterList;
    }

    public static int deleteChapters(Book book) {
        return deleteChapters("b_uniquely_identifies = ?", book.getbUniquelyIdentifies());
    }

    public static int deleteChapters(String whereClause, String... whereArgs) {
        try (SQLiteDatabase db = CUSTOM_DATABASE_HELPER.getWritableDatabase()) {
            return db.delete(TABLE_NAME, whereClause, whereArgs);
        }
    }
}
