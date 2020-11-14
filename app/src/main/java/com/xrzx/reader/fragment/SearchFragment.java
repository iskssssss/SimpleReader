package com.xrzx.reader.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xrzx.commonlibrary.callback.ResultCallBack;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.utils.KeyboardUtils;
import com.xrzx.commonlibrary.utils.ThemeUtils;
import com.xrzx.reader.GlobalData;
import com.xrzx.reader.R;
import com.xrzx.reader.activity.BookInfoActivity;
import com.xrzx.reader.adapter.SearchBookAdapter;
import com.xrzx.reader.book.http.BaseCrawling;
import com.xrzx.reader.fragment.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * @Description 搜索
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class SearchFragment extends BaseFragment {

    final BaseCrawling crawlingApi = GlobalData.getInstance().getCrawlingApi();
    private EditText etSearchKeyWord;
    private ArrayList<Book> searchBooks;
    private RecyclerView lvBookShelf;
    private SearchBookAdapter searchBookAdapter;
    private int currSelectItemIndex = 0;

    /**
     * 是否在搜索中
     */
    boolean isSearch = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // 初始化搜索结果书架
         lvBookShelf = view.findViewById(R.id.fs_lv_book_shelf);
        lvBookShelf.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        searchBooks = new ArrayList<>();
        searchBookAdapter = new SearchBookAdapter(view.getContext(), R.layout.item_search_book, searchBooks);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lvBookShelf.setLayoutManager(manager);
        lvBookShelf.setAdapter(searchBookAdapter);

        // 搜索结果书架项点击监听
        searchBookAdapter.setOnItemClickListener((item, position) -> {
            currSelectItemIndex = position;
            Book book = searchBooks.get(position);
            toastShow("获取书籍信息中，请稍后..");
            // 判断当前选择的书籍是否在书架
            final Book searchBook = GLOBAL_DATA.searchBook(book);
            if (searchBook != null) {
                nextBookInfo(view, searchBook, GLOBAL_DATA.getPosition(searchBook));
                return;
            }
            CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.submit(() ->
                    crawlingApi.getBookDetailsInfo(book, new ResultCallBack<Book>() {
                        @Override
                        public void onSuccess(Book result) {
                            Log.w("书籍搜索", "《" + result.getbName() + "》查询成功");
                            book.setbCurrentReadChapterId(1);
                            book.setbCurrentReadChapterPage(1);
                            book.setBookShelf(false);
                            book.setChapterList(new ArrayList<>());
                            nextBookInfo(view, book, -1);
                        }

                        @Override
                        public void onError(Exception e) {
                            Message msg = Message.obtain();
                            msg.what = 2;
                            msg.obj = "查询失败，请重新尝试。";
                            handler.sendMessage(msg);
                            Log.e("书籍搜索", Objects.requireNonNull(e.getLocalizedMessage()));
                        }
                    }));
        });

        // 初始化搜索框
        etSearchKeyWord = view.findViewById(R.id.fs_et_search_key_word);
        etSearchKeyWord.setOnEditorActionListener((v, actionId, event) -> {
            final FragmentActivity activity = SearchFragment.this.getActivity();
            if (null != activity) {
                KeyboardUtils.hideKeyboard(activity);
            }
            onClickBtnSearch(etSearchKeyWord.getText().toString());
            return false;
        });
        return view;
    }

    @Override
    public void changeTheme(Resources.Theme theme) {
        try {
            ThemeUtils.applyTextColor(etSearchKeyWord, theme, R.attr.TextViewColor);
            searchBookAdapter.changeTheme(theme);
            lvBookShelf.smoothScrollToPosition(currSelectItemIndex);
            searchBookAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nextBookInfo(View view, Book book, int position) {
        toastShow("获取成功。");
        if (position == -1) {
            GLOBAL_DATA.setCurrSelectBook(book);
        } else {
            GLOBAL_DATA.setCurrSelectBookAndPosition(book, position);
        }
        Intent intent = new Intent(view.getContext(), BookInfoActivity.class);
        startActivity(intent);
    }

    /**
     * 搜索图书
     *
     * @param searchkey 关键字
     */
    private void onClickBtnSearch(String searchkey) {
        if (isSearch) {
            toastShow("正在搜索，请稍后。");
            return;
        }
        if ("".equals(searchkey)) {
            toastShow("请输入搜索关键字");
            return;
        }
        isSearch = true;
        searchBooks.clear();
        toastShow("搜索中...");
        CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.submit(() -> {
            crawlingApi.searchBooks(searchkey, searchBooks, new ResultCallBack<List<Book>>() {
                @Override
                public void onSuccess(List<Book> result) {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }

                @Override
                public void onError(Exception e) {
                    Message msg = Message.obtain();
                    msg.what = 2;
                    msg.obj = "搜索失败，请重新尝试。";
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            });
        });
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    searchBookAdapter.notifyDataSetChanged();
                    toastShow("搜索完成，共搜寻到" + searchBooks.size() + "条书籍。");
                    isSearch = false;
                    break;
                case 2:
                    toastShow((String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
}