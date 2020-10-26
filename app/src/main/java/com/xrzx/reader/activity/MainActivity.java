package com.xrzx.reader.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xrzx.reader.R;
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

    private ViewPager amVPTitle;
    private ViewPager amVPMain;

    LinearLayout absLLBookShelfItem;
    ImageView absLLBookShelfItemImg;
    LinearLayout absLLBookSearchItem;
    ImageView absLLBookSearchItemImg;

    private FragmentPagerAdapter mAdpater;

    private List<Fragment> mainFragments;
    private List<Fragment> titleFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        absLLBookShelfItemImg = findViewById(R.id.am_lLBookShelfItemImg);
        absLLBookSearchItemImg = findViewById(R.id.am_lLBookSearchItemImg);

        absLLBookShelfItemImg.setBackgroundResource(R.drawable.book_shelf_select);
        absLLBookSearchItemImg.setBackgroundResource(R.drawable.search_default);

        absLLBookShelfItem = findViewById(R.id.am_lLBookShelfItem);
        absLLBookShelfItem.setOnClickListener(this);
        absLLBookSearchItem = findViewById(R.id.am_lLBookSearchItem);
        absLLBookSearchItem.setOnClickListener(this);


        titleFragments = new ArrayList<>();
        titleFragments.add(new BaseTitleFragment());
        amVPTitle = findViewById(R.id.am_vPTitle);
        amVPTitle.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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

        mainFragments = new ArrayList<>();
        mainFragments.add(new BookShelfFragment());
        mainFragments.add(new SearchFragment());
        amVPMain = findViewById(R.id.am_vPMain);
        amVPMain.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
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
        amVPMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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

    private void setPageView(int pageId) {
        switch (pageId) {
            case 0:
                ((BaseTitleFragment) titleFragments.get(0)).setTitle("书架");
                absLLBookSearchItemImg.setBackgroundResource(R.drawable.search_default);
                absLLBookShelfItemImg.setBackgroundResource(R.drawable.book_shelf_select);
                break;
            case 1:
                ((BaseTitleFragment) titleFragments.get(0)).setTitle("搜索");
                absLLBookShelfItemImg.setBackgroundResource(R.drawable.book_shelf_default);
                absLLBookSearchItemImg.setBackgroundResource(R.drawable.search_select);
                break;
            default:
                System.out.println("onClickAbsLLBookItem-default");
                break;
        }
        amVPMain.setCurrentItem(pageId);
    }
}