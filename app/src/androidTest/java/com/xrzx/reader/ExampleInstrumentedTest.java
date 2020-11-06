package com.xrzx.reader;

import android.content.ContentValues;
import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.xrzx.commonlibrary.database.dao.base.BaseDao;
import com.xrzx.commonlibrary.utils.SQLiteUtils;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.entity.Chapter;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.xrzx.reader", appContext.getPackageName());

        Book book = new Book("name", "author", "type", "introduction",
                "bookUrl", "chapterUrl", "lastUpdateTime", "lastUpdateChapter");
        book.setbId(1L);
        book.setbUniquelyIdentifies("百度");
        book.setbCurrentReadChapterPage(1);
        book.setbCurrentReadChapterId(1);
        Chapter chapter = new Chapter();
        chapter.setcId(1L);
        chapter.setbUniquelyIdentifies(book.getbUniquelyIdentifies());
        chapter.setcNumber(1);
        chapter.setcTitle("百度新闻");
        chapter.setcUrl("https:www.baidu.com");
//        chapter.setcContent("百度正文");

        final ContentValues insertContentValues = getInsertContentValues(Chapter.class, chapter);
        final Set<String> strings = insertContentValues.keySet();
        for (String key : strings) {
            System.out.println(key + " " + insertContentValues.get(key));
        }
        System.out.println("");
    }

    private static ContentValues getInsertContentValues(Class mClass, Object object) {
        ContentValues contentValues = new ContentValues();
        List<Method> methodList = new ArrayList<>();
        Method[] methods = mClass.getMethods().clone();
        for (Method method : methods) {
            if (!method.getName().toLowerCase().contains("get") || method.getName().contains("getClass")) {
                continue;
            }
            methodList.add(method);
        }
        List<Field> fields = SQLiteUtils.getSortFieldByOrder(mClass);
        for (Field field : fields) {
            final String fieldName = field.getName();
            final String sqLiteName = SQLiteUtils.entityName2SqlName(fieldName);
            final Iterator<Method> iterator = methodList.iterator();
            while (iterator.hasNext()) {
                final Method method = iterator.next();
                if (!method.getName().toLowerCase().contains("get" + fieldName.toLowerCase())) {
                    continue;
                }
                try {
                    final Object invoke = method.invoke(object);
                    if (null == invoke) {
                        break;
                    }
                    String fieldType = field.getType().getSimpleName();
//                    System.out.println(String.format("方法名：%s 字段名：%s 字段值：%s 字段类型：%s", method.getName(), fieldName, invoke, fieldType));
                    switch (fieldType) {
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
                    iterator.remove();
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                    System.out.println(ignored.getMessage());
                }
                break;
            }
        }
        return contentValues;
    }
}