package com.xrzx.reader.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.xrzx.commonlibrary.utils.ToastUtils;
import com.xrzx.reader.GlobalData;
import com.xrzx.reader.R;
import com.xrzx.reader.activity.BookInfoActivity;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.reader.book.http.BookHttpApi;
import com.xrzx.commonlibrary.callback.ResultCallBack;
import com.xrzx.commonlibrary.utils.KeyboardUtils;
import com.xrzx.commonlibrary.utils.ThreadUtils;
import com.xrzx.reader.view.adapter.SearchBookAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.annotation.Nullable;

import okhttp3.FormBody;

/**
 * @Description 搜索
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class SearchFragment extends Fragment {
    private final static ExecutorService CRAWLING_EXECUTOR_SERVICE_THREAD_POOL = ThreadUtils.getCrawlingExecutorServiceThreadPool();

    private static final GlobalData GLOBAL_DATA = GlobalData.getInstance();
    private EditText etSearchByBookName;
    private ArrayList<Book> books;
    private SearchBookAdapter chapterAdapter;

    /**
     * 是否在搜索中
     */
    boolean isSearch = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // 初始化搜索结果书架
        ListView listView = view.findViewById(R.id.listView);
        books = new ArrayList<>();
        chapterAdapter = new SearchBookAdapter(view.getContext(), R.layout.search_book_item, books);
        listView.setAdapter(chapterAdapter);

        // 搜索结果书架项点击监听
        listView.setOnItemClickListener((parent, v, position, id) -> CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.execute(() -> {
            Book book = books.get(position);

            final Book searchBook = GLOBAL_DATA.searchBook(book);
            if (searchBook != null) {
                GLOBAL_DATA.setCurrSelectBookAndPosition(searchBook, GLOBAL_DATA.getPosition(searchBook));
                Intent intent = new Intent(view.getContext(), BookInfoActivity.class);
                startActivity(intent);
                return;
            }

            BookHttpApi.getBookDetailsInfo(book, new ResultCallBack<Book>() {
                @Override
                public void onSuccess(Book result) {
                    result.setBookShelf(false);
                    GLOBAL_DATA.setCurrSelectBookAndPosition(result, -1);
                    Intent intent = new Intent(view.getContext(), BookInfoActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    ToastUtils.show("选择失败，请重新尝试。");
                }
            });
        }));

        // 初始化搜索框
        etSearchByBookName = view.findViewById(R.id.eTSearchByBookName);
        etSearchByBookName.setOnEditorActionListener((v, actionId, event) -> {
            boolean enter = actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction());
            if (enter) {
                final FragmentActivity activity = this.getActivity();
                if (null != activity) {
                    KeyboardUtils.hideKeyboard(activity);
                }
                onClickBtnSearch(etSearchByBookName.getText().toString());
            }
            return false;
        });
        return view;
    }

    /**
     * 搜索图书
     *
     * @param searchkey 关键字
     */
    private void onClickBtnSearch(String searchkey) {
        if (isSearch) {
            ToastUtils.show("正在搜索，请稍后。");
            books.clear();
        }
        if ("".equals(searchkey)) {
            ToastUtils.show("请输入搜索关键字");
            return;
        }
        isSearch = true;
        books.clear();
        ToastUtils.show("搜索中...");
        CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.submit(() -> {
            FormBody.Builder searchDict = new FormBody.Builder();
            searchDict.add("searchkey", searchkey);
            BookHttpApi.searchBooks(searchDict, books, new ResultCallBack<List<Book>>() {
                @Override
                public void onSuccess(List<Book> result) {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    ToastUtils.show("搜索失败，请重新尝试。");
                }
            });
        });
    }

    @Override
    public void onDestroy() {
        books.clear();
        chapterAdapter.clear();
        books = null;
        handler = null;
        chapterAdapter = null;
        super.onDestroy();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                isSearch = false;
                chapterAdapter.notifyDataSetChanged();
                ToastUtils.show("搜索完成，共搜寻到" + books.size() + "条书籍。");
            }
        }
    };
}