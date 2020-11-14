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

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xrzx.commonlibrary.callback.ResultCallBack;
import com.xrzx.commonlibrary.database.dao.BookInfoDao;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.utils.DateUtils;
import com.xrzx.commonlibrary.utils.ToastUtils;
import com.xrzx.reader.GlobalData;
import com.xrzx.reader.R;
import com.xrzx.reader.activity.BookInfoActivity;
import com.xrzx.reader.activity.ReadActivity;
import com.xrzx.reader.book.http.BaseCrawling;
import com.xrzx.reader.fragment.base.BaseFragment;
import com.xrzx.reader.adapter.BookShelfListAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * @Description 书架
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class BookShelfFragment extends BaseFragment {
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                initView();
                ToastUtils.show("更新成功。");
            }
        }
    };
    final ArrayList<Book> bookShelfList = GLOBAL_DATA.getBookShelfList();
    private BaseCrawling crawlingApi;
    private RecyclerView lvBookShelf;
    private BookShelfListAdapter bookShelfListAdapter;

    /**
     * 更新书架
     */
    private void updateBookShelf() {
        final ArrayList<Book> bookShelfList = BookInfoDao.findAllOrderBy("b_new_read_time desc");
        GLOBAL_DATA.setBookShelfList(bookShelfList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_shelf, container, false);
        crawlingApi = GlobalData.getInstance().getCrawlingApi();

        // 获取书籍列表
        bookShelfList.clear();
        bookShelfList.addAll(BookInfoDao.findAllOrderBy("b_new_read_time desc"));
        ToastUtils.show("正在更新书籍信息，请稍后。");
        CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.submit(() -> {
            bookShelfList.forEach(book ->
                    crawlingApi.updateBookDetailsInfo(book, new ResultCallBack<Book>() {
                        @Override
                        public void onSuccess(Book result) {
                            if (!result.getbLastUpdateTime().equals(book.getbLastUpdateTime())) {
                                result.setbUniquelyIdentifies(book.getbUniquelyIdentifies());
                                if (book.getbUpdate() == 0) {
                                    result.setbUpdate(1);
                                    book.setbUpdate(1);
                                }
                                BookInfoDao.updateBook(result);
                                book.setbLastUpdateTime(result.getbLastUpdateTime());
                                book.setbLastUpdateChapter(result.getbLastUpdateChapter());
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("书籍更新", Objects.requireNonNull(e.getLocalizedMessage()));
                        }
                    }));
            Message msg = Message.obtain();
            msg.what = 1;
            handler.sendMessage(msg);
        });
        return view;
    }

    private void initView(){
        final View view = getView();
        // 初始化书架
        lvBookShelf = view.findViewById(R.id.fbs_lv_book_shelf);
        lvBookShelf.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        bookShelfListAdapter = new BookShelfListAdapter(view.getContext(), R.layout.item_book_shelf, bookShelfList);
        GLOBAL_DATA.setChapterAdapter(bookShelfListAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lvBookShelf.setLayoutManager(manager);
        lvBookShelf.setAdapter(GLOBAL_DATA.getChapterAdapter());

        // 书架书籍详细信息按钮监听
        GLOBAL_DATA.getChapterAdapter().setOnItemDetailsClickListener(position -> {
            final Book book = bookShelfList.get(position);
            GLOBAL_DATA.setCurrSelectBookAndPosition(book, position);
            Intent intent = new Intent(view.getContext(), BookInfoActivity.class);
            startActivity(intent);
        });
        // 书架书籍阅读监听
        GLOBAL_DATA.getChapterAdapter().setOnItemClickListener((item, position) -> {
            // 获取当前选择的书籍信息
            final Book book = bookShelfList.get(position);
            GLOBAL_DATA.setCurrSelectBookAndPosition(book, position);

            // 更新书籍阅读时间
            book.setbUpdate(0);
            book.setbNewReadTime(DateUtils.getDateTime());
            final Book book1 = new Book(book.getbUniquelyIdentifies(), book.getbNewReadTime());
            book1.setbUpdate(book.getbUpdate());
            BookInfoDao.updateBook(book1);

            // 开启阅读界面
            Intent intent = new Intent(view.getContext(), ReadActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void changeTheme(Resources.Theme theme) {
        try {
            bookShelfListAdapter.changeTheme(theme);
            lvBookShelf.smoothScrollToPosition(GLOBAL_DATA.getCurrSelectPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}