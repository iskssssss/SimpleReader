package com.xrzx.reader.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xrzx.commonlibrary.database.dao.ReadPageSettingDao;
import com.xrzx.commonlibrary.entity.ReadPageSettingLog;
import com.xrzx.commonlibrary.enums.ReadPageStyle;
import com.xrzx.commonlibrary.utils.ToastUtils;
import com.xrzx.reader.R;
import com.xrzx.commonlibrary.database.helper.CustomDatabaseHelper;
import com.xrzx.reader.fragment.BaseTitleFragment;
import com.xrzx.reader.fragment.BookShelfFragment;
import com.xrzx.reader.fragment.SearchFragment;
import com.xrzx.reader.view.custom.TitleViewPager;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

/**
 * @Description 主界面
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private TitleViewPager vpTitle;
    private ViewPager vpMain;

    private List<Fragment> mainFragments;
    private List<Fragment> titleFragments;

    private ImageView bookShelfItemImg;
    private ImageView bookSearchItemImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        // 数据库和提示框初始化
        CustomDatabaseHelper.getCustomDatabaseHelper(MainActivity.this);
        ToastUtils.setToast(MainActivity.this);

        // 底部菜单初始化
        bookShelfItemImg = findViewById(R.id.am_ll_book_shelf_item_img);
        bookShelfItemImg.setBackgroundResource(R.drawable.book_shelf_select);
        bookSearchItemImg = findViewById(R.id.am_ll_book_search_item_img);
        bookSearchItemImg.setBackgroundResource(R.drawable.search_default);

        // 底部按钮初始化
        findViewById(R.id.am_ll_book_shelf_item).setOnClickListener(this);
        findViewById(R.id.am_ll_book_search_item).setOnClickListener(this);

        // 顶部菜单初始化
        titleFragments = new ArrayList<>();
        titleFragments.add(new BaseTitleFragment(getString(R.string.main_tv_text_bookshelf_str)));
        titleFragments.add(new BaseTitleFragment(getString(R.string.main_tv_text_search)));
        vpTitle = findViewById(R.id.am_tvp_title);
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
        vpMain = findViewById(R.id.am_vp_main);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.am_ll_book_shelf_item:
                setPageView(0);
                break;
            case R.id.am_ll_book_search_item:
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
                    bookSearchItemImg.setBackgroundResource(R.drawable.search_default);
                    bookShelfItemImg.setBackgroundResource(R.drawable.book_shelf_select);
                    break;
                case 1:
                    bookShelfItemImg.setBackgroundResource(R.drawable.book_shelf_default);
                    bookSearchItemImg.setBackgroundResource(R.drawable.search_select);
                    break;
                default:
                    System.out.println("onClickAbsLLBookItem-default");
                    break;
            }
            vpTitle.setCurrentItem(pageId);
            vpMain.setCurrentItem(pageId);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show("程序发生错误，请尝试重启软件来解决。");
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