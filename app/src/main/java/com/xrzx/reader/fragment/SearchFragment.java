package com.xrzx.reader.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.xrzx.reader.R;
import com.xrzx.reader.activity.BookInfoActivity;
import com.xrzx.reader.book.entity.Book;
import com.xrzx.reader.book.http.BookHttpApi;
import com.xrzx.reader.common.callback.ResultCallBack;
import com.xrzx.reader.common.utils.KeyboardUtils;
import com.xrzx.reader.view.adapter.SearchBookAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Nullable;

import okhttp3.FormBody;

/**
 * @Description 搜索
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class SearchFragment extends Fragment implements View.OnClickListener {
    private final static ExecutorService executorService = Executors.newFixedThreadPool(1);

    private EditText eTSearchByBookName;
    private Button btnSearch;

    private ListView listView;
    private ArrayList<Book> titleList = new ArrayList<>();
    private SearchBookAdapter chapterAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);

        eTSearchByBookName = view.findViewById(R.id.eTSearchByBookName);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        listView = view.findViewById(R.id.listView);
        chapterAdapter = new SearchBookAdapter(view.getContext(), R.layout.search_book_item, titleList);
        listView.setAdapter(chapterAdapter);
        listView.setOnItemClickListener((parent, v, position, id) -> executorService.execute(() -> {
            Book book = titleList.get(position);
            BookHttpApi.getBookDetailsInfo(book, new ResultCallBack<Book>() {
                @Override
                public void onSuccess(Book result) {
                    Intent intent = new Intent(view.getContext(), BookInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("book", book);
                    startActivity(intent);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }));
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                onClickBtnSearch(v);
                break;
            default:
                //TODO ...
                break;
        }
    }

    private void onClickBtnSearch(View v) {
        String searchkey = eTSearchByBookName.getText().toString();
        if ("".equals(searchkey)) {
            return;
        }
        titleList.clear();
        FormBody.Builder searchDict = new FormBody.Builder();
        searchDict.add("searchkey", searchkey);
        BookHttpApi.searchBooks(searchDict, titleList, new ResultCallBack<List<Book>>() {
            @Override
            public void onSuccess(List<Book> result) {
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        KeyboardUtils.hideKeyboard(this.getActivity());
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    chapterAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    System.out.println("ss");
                    break;
                default:
                    System.out.println("default");
                    break;
            }
        }
    };
}