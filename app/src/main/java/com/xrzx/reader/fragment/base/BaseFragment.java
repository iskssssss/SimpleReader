package com.xrzx.reader.fragment.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.xrzx.commonlibrary.utils.ThreadUtils;
import com.xrzx.reader.GlobalData;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/6 17:49
 */
public class BaseFragment extends Fragment {
    protected final static ThreadPoolExecutor CRAWLING_EXECUTOR_SERVICE_THREAD_POOL = ThreadUtils.getCrawlingExecutorServiceThreadPool();
    protected static final GlobalData GLOBAL_DATA = GlobalData.getInstance();
    protected static FragmentActivity mContext;
    protected static Toast mToast;

    @SuppressLint("ShowToast")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
    }

    /**
     * 显示提示信息
     *
     * @param text 提示内容
     */
    public static void toastShow(CharSequence text) {
        try {
            mToast.setText(text);
            mToast.show();
        } catch (Exception e) {
            Log.e("BaseFragment", Objects.requireNonNull(e.getLocalizedMessage()));
        }
    }
}
