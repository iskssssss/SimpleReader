package com.xrzx.reader.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.xrzx.reader.R;
import com.xrzx.reader.book.entity.Book;
import com.xrzx.reader.view.adapter.BookShelfListAdapter;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * @Description 书架
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class BookShelfFragment extends Fragment implements View.OnClickListener {

    private ListView listView;
    private ArrayList<Book> titleList = new ArrayList<>();
    private BookShelfListAdapter chapterAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_book_shelf, container, false);

        listView = view.findViewById(R.id.abs_lVBookShelf);
        chapterAdapter = new BookShelfListAdapter(view.getContext(), R.layout.book_shelf_book_item, titleList);
        listView.setAdapter(chapterAdapter);

        for (int i = 0; i < 10; i++) {
            Book book = new Book(
                    "三寸人间" + i, "耳根",
                    "修真小说",
                    "新书《一品江山》上传了，万事开头难，大家都去收藏、推荐啊！！！！！！新书《一品江山》上传了，万事开头难，大家都去收藏、推荐啊！！！！！！新书《一品江山》上传了，万事开头难，大家都去收藏、推荐啊！！！！！！新书《一品江山》上传了，万事开头难，大家都去收藏、推荐啊！！！！！！新书《一品江山》上传了，万事开头难，大家都去收藏、推荐啊！！！！！！新书《一品江山》上传了，万事开头难，大家都去收藏、推荐啊！！！！！！",
                    "http",
                    "http",
                    "10-19",
                    "万事开头难");
            titleList.add(book);
        }
        chapterAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onClick(View v) {

    }

}