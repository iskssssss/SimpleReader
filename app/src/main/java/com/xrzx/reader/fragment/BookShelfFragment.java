package com.xrzx.reader.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.xrzx.commonlibrary.database.dao.BookInfoDao;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.utils.DateUtils;
import com.xrzx.reader.GlobalData;
import com.xrzx.reader.R;
import com.xrzx.reader.activity.BookInfoActivity;
import com.xrzx.reader.activity.ReadActivity;
import com.xrzx.reader.view.adapter.BookShelfListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * @Description 书架
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class BookShelfFragment extends Fragment {
    private static final GlobalData GLOBAL_DATA = GlobalData.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_shelf, container, false);

        // 初始化书架
        ListView listView = view.findViewById(R.id.abs_lVBookShelf);
        final List<Book> bookShelfList = GLOBAL_DATA.getBookShelfList();
        GLOBAL_DATA.setChapterAdapter(new BookShelfListAdapter(view.getContext(), R.layout.book_shelf_book_item, bookShelfList));
        listView.setAdapter(GLOBAL_DATA.getChapterAdapter());

        // 获取书籍列表
        ArrayList<Book> books = BookInfoDao.findAllOrderBy("b_new_read_time desc");
        if (books.isEmpty()) {
            return view;
        }
        GLOBAL_DATA.setBookShelfList(books);

        // 书架书籍详细信息按钮监听
        GLOBAL_DATA.getChapterAdapter().setOnItemDetailsClickListener(position -> {
            final Book book = bookShelfList.get(position);
            GLOBAL_DATA.setCurrSelectBookAndPosition(book, position);
            Intent intent = new Intent(view.getContext(), BookInfoActivity.class);
            startActivity(intent);
        });

        // 书架书籍阅读监听
        listView.setOnItemClickListener((parent, v, position, id) -> {
            // 获取当前选择的书籍信息
            final Book book = bookShelfList.get(position);
            GLOBAL_DATA.setCurrSelectBookAndPosition(book, position);

            // 更新书籍阅读时间
            book.setbNewReadTime(DateUtils.getDateTime());
            BookInfoDao.updateBook(new Book(book.getbUniquelyIdentifies(), book.getbNewReadTime()));

            // 开启阅读界面
            Intent intent = new Intent(view.getContext(), ReadActivity.class);
            startActivity(intent);
        });
        return view;
    }
}