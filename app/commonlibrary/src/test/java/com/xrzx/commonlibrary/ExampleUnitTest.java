package com.xrzx.commonlibrary;

import com.xrzx.commonlibrary.database.dao.ChapterInfoDao;
import com.xrzx.commonlibrary.database.dao.base.BaseDao;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.entity.Chapter;
import com.xrzx.commonlibrary.enums.ReadPageStyle;
import com.xrzx.commonlibrary.utils.ObjectUtils;
import com.xrzx.commonlibrary.utils.SQLiteUtils;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        ReadPageStyle readPageStyle = ReadPageStyle.READ_YD_K61;

        System.out.println(readPageStyle);

        final ReadPageStyle readPageStyle1 = ReadPageStyle.str2ReadPageStyle(readPageStyle.name());
        System.out.println(readPageStyle1.getColorRead());

    }
}