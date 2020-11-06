package com.xrzx.reader;

import android.content.ContentValues;

import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.annotation.SQLiteAnnotation;
import com.xrzx.commonlibrary.database.dao.BookInfoDao;
import com.xrzx.commonlibrary.enums.TouchMoveDirection;
import com.xrzx.commonlibrary.utils.DateUtils;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {



    @Test
    public void test4() {
        final Long SECOND = DateUtils.getTimestamp(DateUtils.TimestampType.SECOND);
        final Long MILLISECOND = DateUtils.getTimestamp(DateUtils.TimestampType.MILLISECOND);

        System.out.println(SECOND);
        System.out.println(DateUtils.timestamp2LocalDateTime(SECOND, DateUtils.TimestampType.SECOND));
        System.out.println(MILLISECOND);
        System.out.println(DateUtils.timestamp2LocalDateTime(MILLISECOND, DateUtils.TimestampType.MILLISECOND));
    }

    private static List<Field> sortField(Class mClass) {
        Field[] fs = mClass.getDeclaredFields();
        List<Field> fields = new ArrayList<>();
        for (Field f : fs) {
            if (!f.isAnnotationPresent(SQLiteAnnotation.class)) {
                continue;
            }
            fields.add(f);
        }
        fields.sort(Comparator.comparingInt(m -> m.getAnnotation(SQLiteAnnotation.class).order()));
        return fields;
    }

    protected static LinkedHashMap<String, String> getDeclaredFields(Class mClass) {
        List<Field> fields = sortField(mClass);
        LinkedHashMap<String, String> fieldMaps = new LinkedHashMap<>();
        for (int i = 0; i < fields.size(); i++) {
            String fieldName = fields.get(i).getName();
            fieldMaps.put(fieldName, nameConversion(fieldName));
        }
        return fieldMaps;
    }

    @Test
    public void test3() {
        final LinkedHashMap<String, String> declaredFields = getDeclaredFields(Book.class);
        Book book = new Book("name", "author", "type", "introduction",
                "bookUrl", "chapterUrl", "lastUpdateTime", "lastUpdateChapter");
        book.setbId(1L);
        book.setbUniquelyIdentifies("uniquelyIdentifies");
        book.setbCurrentReadChapterPage(1);
        book.setbCurrentReadChapterId(1);
        Class mClass = Book.class;
        Method[] methods = mClass.getMethods().clone();
        List<Method> methodList = new ArrayList<>();
        ContentValues contentValues = new ContentValues();
        for (Method method : methods) {
            if (!method.getName().toLowerCase().contains("get") || method.getName().contains("getClass")){
                continue;
            }
            methodList.add(method);
        }
        List<Field> fields = sortField(mClass);
        for (Field field : fields) {
            final String fieldName = field.getName();
            final String sqLiteName = nameConversion(fieldName);
            final Iterator<Method> iterator = methodList.iterator();
            while (iterator.hasNext()) {
                final Method method = iterator.next();
                if (!method.getName().toLowerCase().contains("get" + fieldName.toLowerCase())) {
                    continue;
                }
                try {
                    final Object invoke = method.invoke(book);
                    if (null == invoke) {
                        break;
                    }
                    String fieldType = field.getType().getSimpleName();
                    System.out.println(String.format("方法名：%s 字段名：%s 字段值：%s 字段类型：%s", method.getName(), fieldName, invoke, fieldType));
                    switch (fieldType){
                        case "String":
                            contentValues.put(sqLiteName, (String) invoke);
                            break;
                        case "Long":
                            contentValues.put(sqLiteName, (Long) invoke);
                            break;
                        case "Integer":
                            contentValues.put(sqLiteName, (Integer) invoke);
                            break;
                        default:
                            contentValues.put(sqLiteName, (byte[]) invoke);
                            break;
                    }
//
                    iterator.remove();
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                    System.out.println(ignored.getMessage());
                }
                break;
            }
        }
        System.out.println("");
        /*for (String key : declaredFields.keySet()) {
            for (Method method : methods) {
                if (!method.getName().toLowerCase().contains("get" + key.toLowerCase())) {
                    continue;
                }
                try {
                    final Object invoke = method.invoke(book);
                    if (null == invoke) {
                        break;
                    }
                    System.out.println(String.format("方法名：%s 字段名：%s 字段值：%s", method.getName(), key, method.invoke(book)));
                    // System.out.println("" + method.invoke(book) + " " + key + " " + declaredFields.get(key));
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                    System.out.println(ignored.getMessage());
                }
                break;
            }
        }*/
    }

    @Test
    public void test1() {
        List<Field> fields = new ArrayList<>();
        Class aClass = Book.class;
        Field[] fs = aClass.getDeclaredFields();
        for (Field f : fs) {
            if (!f.isAnnotationPresent(SQLiteAnnotation.class)) {
                continue;
            }
            fields.add(f);
        }
        fields.sort(Comparator.comparingInt(
                m -> m.getAnnotation(SQLiteAnnotation.class).order()));
        System.out.println(fields);
    }

    @Test
    public void test() {
        Class aClass = Book.class;
        Field[] fs = aClass.getDeclaredFields();

        List<Field> fields = new ArrayList<>();
        for (Field f : fs) {
            if (!f.isAnnotationPresent(SQLiteAnnotation.class)) {
                continue;
            }
            fields.add(f);
        }
        fields.sort(Comparator.comparingInt(m -> m.getAnnotation(SQLiteAnnotation.class).order()));

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE book\n");
        sb.append("(");
        for (Field field : fields) {
            final SQLiteAnnotation annotation = field.getAnnotation(SQLiteAnnotation.class);
            String name = field.getType().getName();
            String fieldName = field.getName();
            sb.append(nameConversion(fieldName));
            sb.append(" ");
            sb.append(BookInfoDao.KEY_DICT.get(name));
            if (annotation.key()) {
                sb.append(" ");
                sb.append("PRIMARY KEY");
            }
            if (annotation.autoincrement()) {
                sb.append(" ");
                sb.append("AUTOINCREMENT");
            }
            if (annotation.notNull()) {
                sb.append(" ");
                sb.append("NOT NULL");
            }
            sb.append(",");
            sb.append("\n");
        }
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append(");");
        System.out.println(sb);
    }

    public static String nameConversion(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            final char c = name.charAt(i);
            if (c > 64 && c < 91) {
                stringBuilder.append("_");
                stringBuilder.append((char) (c + 32));
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }


    @Test
    public void addition_isCorrect() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("create table ")
                .append("TABLE_NAME")
                .append(" (")
                .append("b_id integer primary key autoincrement, ")
                .append("b_name text NOT NULL, ")
                .append("b_author text NOT NULL, ")
                .append("b_type text, ")
                .append("b_introduction text, ")
                .append("b_book_url text NOT NULL, ")
                .append("b_chapter_url text NOT NULL, ")
                .append("b_last_update_time text NOT NULL, ")
                .append("b_last_update_chapter_id text NOT NULL, ")
                .append("b_current_read_chapter_id text NOT NULL")
                .append(");");
        System.out.println(stringBuilder);

        oldEventX = 541f;
        System.out.println(touchMoveDirection(541f, 2.5f));
    }


    private float width = 1080f; // 540
    private float oldEventX;

    public TouchMoveDirection touchMoveDirection(float x, float y) {
        float diff = 1e-6f;
        if (Math.abs(x - oldEventX) < diff) {
            if (oldEventX > (width / 2.0)) {
                return TouchMoveDirection.RIGHT;
            }
        } else if (x - oldEventX < 0.0) {
            return TouchMoveDirection.RIGHT;
        }
        return TouchMoveDirection.LEFT;
    }
}