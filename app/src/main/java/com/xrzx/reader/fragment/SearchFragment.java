package com.xrzx.reader.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xrzx.commonlibrary.database.dao.BookInfoDao;
import com.xrzx.commonlibrary.utils.ToastUtils;
import com.xrzx.reader.R;
import com.xrzx.reader.activity.BookInfoActivity;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.reader.book.http.BookHttpApi;
import com.xrzx.commonlibrary.callback.ResultCallBack;
import com.xrzx.commonlibrary.utils.KeyboardUtils;
import com.xrzx.reader.fragment.base.BaseFragment;
import com.xrzx.reader.view.adapter.SearchBookAdapter;
import com.xrzx.reader.view.base.BaseRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

import okhttp3.FormBody;

/**
 * @Description 搜索
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class SearchFragment extends BaseFragment {

    private EditText etSearchKeyWord;
    private ArrayList<Book> searchBooks;
    private SearchBookAdapter searchBookAdapter;

    /**
     * 是否在搜索中
     */
    boolean isSearch = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // 初始化搜索结果书架
        RecyclerView lvBookShelf = view.findViewById(R.id.fs_lv_book_shelf);
        lvBookShelf.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        searchBooks = new ArrayList<>();
        searchBookAdapter = new SearchBookAdapter(view.getContext(), R.layout.item_search_book, searchBooks);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lvBookShelf.setLayoutManager(manager);
        lvBookShelf.setAdapter(searchBookAdapter);

        // 搜索结果书架项点击监听
        searchBookAdapter.setOnItemClickListener((item, position) -> {
            Book book = searchBooks.get(position);
            toastShow("获取书籍信息中，请稍后..");
            // 判断当前选择的书籍是否在书架
            final Book searchBook = GLOBAL_DATA.searchBook(book);
            if (searchBook != null) {
                nextBookInfo(view, searchBook, GLOBAL_DATA.getPosition(searchBook));
                return;
            }
            CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.submit(() ->
                    BookHttpApi.getBookDetailsInfo(book, new ResultCallBack<Book>() {
                        @Override
                        public void onSuccess(Book result) {
                            Log.w("书籍搜索", "《" + result.getbName() + "》查询成功");
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
            /*boolean enter = actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction());*/
            final FragmentActivity activity = SearchFragment.this.getActivity();
            if (null != activity) {
                KeyboardUtils.hideKeyboard(activity);
            }
            /*if (enter) {
                onClickBtnSearch(etSearchKeyWord.getText().toString());
            }*/
            onClickBtnSearch(etSearchKeyWord.getText().toString());
            return false;
        });
        return view;
    }

    private void nextBookInfo(View view, Book book, int position) {
        GLOBAL_DATA.setCurrSelectBookAndPosition(book, position);
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
            searchBooks.clear();
        }
        if ("".equals(searchkey)) {
            toastShow("请输入搜索关键字");
            return;
        }
        isSearch = true;
        searchBooks.clear();
        toastShow("搜索中...");
        CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.submit(() -> {
            FormBody.Builder searchDict = new FormBody.Builder();
            searchDict.add("searchkey", searchkey);
            BookHttpApi.searchBooks(searchDict, searchBooks, new ResultCallBack<List<Book>>() {
                @Override
                public void onSuccess(List<Book> result) {
                    /*final Iterator<Book> iterator = result.iterator();
                    while (iterator.hasNext()) {
                        final Book book = iterator.next();
                        System.out.println("正在查询《" + book.getbName() + "》的详细信息");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.submit(() ->
                                BookHttpApi.getBookDetailsInfo(book, new ResultCallBack<Book>() {
                                    @Override
                                    public void onSuccess(Book result) {
                                        Log.w("书籍搜索", "《" + result.getbName() + "》查询成功");
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.e("书籍搜索", Objects.requireNonNull(e.getMessage()));
                                    }
                                }));
                    }*/
                    /*while (CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.getActiveCount() > 0) {
                        System.out.println("线程池中线程数目：" + CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.getPoolSize() + "，队列中执行的任务数目：" + CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.getActiveCount());
                    }*/
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
                    isSearch = false;
                    searchBookAdapter.notifyDataSetChanged();
                    toastShow("搜索完成，共搜寻到" + searchBooks.size() + "条书籍。");
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