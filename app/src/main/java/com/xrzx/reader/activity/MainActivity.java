package com.xrzx.reader.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xrzx.commonlibrary.database.dao.base.BaseDao;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.entity.Chapter;
import com.xrzx.commonlibrary.utils.ToastUtils;
import com.xrzx.reader.R;
import com.xrzx.commonlibrary.database.helper.CustomDatabaseHelper;
import com.xrzx.reader.fragment.BaseTitleFragment;
import com.xrzx.reader.fragment.BookShelfFragment;
import com.xrzx.reader.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

/**
 * @Description 主界面
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager vpMain;
    private ImageView llBookShelfItemImg;
    private ImageView llBookSearchItemImg;
    private List<Fragment> mainFragments;
    private List<Fragment> titleFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 数据库和提示框初始化
        CustomDatabaseHelper.getCustomDatabaseHelper(MainActivity.this);
        ToastUtils.setToast(MainActivity.this);

        // 底部菜单初始化
        llBookShelfItemImg = findViewById(R.id.am_lLBookShelfItemImg);
        llBookShelfItemImg.setBackgroundResource(R.drawable.book_shelf_select);
        llBookSearchItemImg = findViewById(R.id.am_lLBookSearchItemImg);
        llBookSearchItemImg.setBackgroundResource(R.drawable.search_default);

        // 底部按钮初始化
        LinearLayout llBookShelfItem = findViewById(R.id.am_lLBookShelfItem);
        llBookShelfItem.setOnClickListener(this);
        LinearLayout llBookSearchItem = findViewById(R.id.am_lLBookSearchItem);
        llBookSearchItem.setOnClickListener(this);

        // 顶部菜单初始化
        titleFragments = new ArrayList<>();
        titleFragments.add(new BaseTitleFragment());
        ViewPager vpTitle = findViewById(R.id.am_vPTitle);
        vpTitle.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Override
            public int getCount() {
                return titleFragments.size();
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return titleFragments.get(position);
            }
        });

        // 中间容器初始化
        mainFragments = new ArrayList<>();
        mainFragments.add(new BookShelfFragment());
        mainFragments.add(new SearchFragment());
        vpMain = findViewById(R.id.am_vPMain);
        vpMain.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setPageView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        vpMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Override
            public int getCount() {
                return mainFragments.size();
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mainFragments.get(position);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.am_lLBookShelfItem:
                setPageView(0);
                break;
            case R.id.am_lLBookSearchItem:
                setPageView(1);
                break;
            default:
                System.out.println("onClickAbsLLBookItem-default");
                break;
        }
    }

    /**
     * 底部选择按钮切换
     *
     * @param pageId 当前选择的按钮
     */
    private void setPageView(int pageId) {
        try {
            switch (pageId) {
                case 0:
                    ((BaseTitleFragment) titleFragments.get(0)).setTitle(getString(R.string.bookshelf_str));
                    llBookSearchItemImg.setBackgroundResource(R.drawable.search_default);
                    llBookShelfItemImg.setBackgroundResource(R.drawable.book_shelf_select);
                    break;
                case 1:
                    ((BaseTitleFragment) titleFragments.get(0)).setTitle(getString(R.string.search_str));
                    llBookShelfItemImg.setBackgroundResource(R.drawable.book_shelf_default);
                    llBookSearchItemImg.setBackgroundResource(R.drawable.search_select);
                    break;
                default:
                    System.out.println("onClickAbsLLBookItem-default");
                    break;
            }
            vpMain.setCurrentItem(pageId);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show("发生错误...");
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.out.println("返回");
        }
        return super.onKeyDown(keyCode, event);
    }
}