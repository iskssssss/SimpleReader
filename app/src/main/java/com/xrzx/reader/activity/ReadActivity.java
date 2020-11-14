package com.xrzx.reader.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.xrzx.commonlibrary.database.dao.ChapterInfoDao;
import com.xrzx.commonlibrary.database.dao.ReadPageSettingDao;
import com.xrzx.commonlibrary.entity.TypefaceEntity;
import com.xrzx.commonlibrary.enums.ReadPageStyle;
import com.xrzx.commonlibrary.utils.AndroidUtils;
import com.xrzx.commonlibrary.utils.DateUtils;
import com.xrzx.reader.GlobalData;
import com.xrzx.reader.book.http.BaseCrawling;
import com.xrzx.reader.dialog.ReadChaptersDialog;
import com.xrzx.reader.dialog.ReadSettingDialog;
import com.xrzx.reader.R;
import com.xrzx.reader.dialog.ReadContentSettingDialog;
import com.xrzx.reader.activity.base.BaseActivity;
import com.xrzx.commonlibrary.entity.Chapter;
import com.xrzx.commonlibrary.callback.ResultCallBack;
import com.xrzx.commonlibrary.enums.TouchMoveDirection;
import com.xrzx.commonlibrary.utils.ToastUtils;
import com.xrzx.reader.dialog.ReadTypefaceSettingDialog;
import com.xrzx.commonlibrary.view.base.BaseRecyclerView;
import com.xrzx.commonlibrary.view.custom.ChapterContentTextView;

import org.jetbrains.annotations.NotNull;

/**
 * @Description 阅读页
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class ReadActivity extends BaseActivity {



    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initBookInfo(currentBook.getbCurrentReadChapterPage(), true);
                    break;
                case 2:
                    initBookInfo(1, ((boolean) msg.obj));
                    break;
                case 3:
                    if ((boolean) msg.obj) {
                        currentBook.increaseCurrentReadChapterId();
                    } else {
                        ToastUtils.show("当前已是最新章节.");
                    }
                    contentTextView.stopDrawLoadingInfo();
                    contentTextView.startDrawContentInfo();
                    break;
                case 4:
                    ToastUtils.show((String) msg.obj);
                    break;
                default:
                    System.out.println("handleMessage.default");
                    break;
            }
        }
    };

    private ChapterContentTextView contentTextView;

    private ReadSettingDialog readSettingDialog;
    private ReadContentSettingDialog readContentSettingDialog;
    private ReadTypefaceSettingDialog readTypefaceSettingDialog;
    private ReadChaptersDialog readChaptersDialog;
    private boolean refreshBatteryAndTime = true;
    BatteryManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (GLOBAL_DATA.readPageSettingLog.gAtNight()){
            setTheme(R.style.AppThemeNight);
        } else {
            setTheme(R.style.AppTheme);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_read);

        initView();
        setCurrentBook();

        // 设置章节列表、设置、阅读设置对话框
        readChaptersDialog = new ReadChaptersDialog(this, currentBook, currentChapterList, onItemClickListener);
        readSettingDialog = new ReadSettingDialog(this, dialogReadSettingOnClickListener);
        readContentSettingDialog = new ReadContentSettingDialog(this, readContentSettingDialogClick, readContentSettingDialogSeekBarChange, readContentSettingDialogCheckedChange);
        readTypefaceSettingDialog = new ReadTypefaceSettingDialog(this, onTypefaceItemClickListener);
        // 设置阅读样式
        // 字体
        readTypefaceSettingDialog.setTypefaceId(GLOBAL_DATA.readPageSettingLog.getTypeface());
        // 亮度 随系统
        readContentSettingDialog.setLuminanceSystem(GLOBAL_DATA.readPageSettingLog.gLuminanceSystem());
        // 亮度
        readContentSettingDialog.setSbLuminance(GLOBAL_DATA.readPageSettingLog.getLuminance());
        // 设置当前亮度
        setBrightness(getWindow(), GLOBAL_DATA.readPageSettingLog.gLuminanceSystem() ? WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE : (float) GLOBAL_DATA.readPageSettingLog.getLuminance());
        // 设置字体大小
        setContentFontSize(GLOBAL_DATA.readPageSettingLog.getFontSize());

        // 设置页面样式
        setReadPageStyle(GLOBAL_DATA.readPageSettingLog.gAtNight());

        // 设置阅读视图样式
        contentTextView.setFontSize(GLOBAL_DATA.readPageSettingLog.getFontSize());
        contentTextView.setRowSpacing(GLOBAL_DATA.readPageSettingLog.getRowSpacing());
        contentTextView.setPageSpacing(GLOBAL_DATA.readPageSettingLog.getPageSpacing());
        setTypeface(readTypefaceSettingDialog.getCurrTypefaces());

        // 初始化目录信息
        initCatalogInfo();

        // 判断当前书籍是否是在书架 在则将书籍放置第一栏
        if (currentBook.isBookShelf()) {
            OTHER_EXECUTOR_SERVICE_THREAD_POOL.submit(() -> {
                try {
                    Thread.sleep(200);
                    GLOBAL_DATA.putBookFirst();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    /**
     * 初始化控件
     */
    private void initView() {
        contentTextView = findViewById(R.id.ar_tv_content);

        manager = (BatteryManager) this.getSystemService(BATTERY_SERVICE);
        contentTextView.setBatteryAndTime(manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY),
                DateUtils.getDateTime("HH:mm"));
        // 处理用户翻页
        contentTextView.setCallBack(new ResultCallBack<Object>() {
            @Override
            public void onSuccess(Object result) {
            }

            @Override
            public void onError(Exception e) {

            }
        });
        OTHER_EXECUTOR_SERVICE_THREAD_POOL.submit(refreshBatteryAndTimeRunnable);
    }

    /**
     * 刷新阅读时的时间和电量
     */
    Runnable refreshBatteryAndTimeRunnable = () -> {
        while (refreshBatteryAndTime) {
            try {
                if (contentTextView.isDrawContentInfo()) {
                    contentTextView.setBatteryAndTime(manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY),
                            DateUtils.getDateTime("HH:mm"));
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    };


    /**
     * 设置正文字体大小
     *
     * @param fontSize 字体大小
     */
    public void setContentFontSize(int fontSize) {
        readContentSettingDialog.setFontSizeText(GLOBAL_DATA.readPageSettingLog.getFontSize());
        contentTextView.setFontSize(fontSize);
        contentTextView.invalidate();
    }

    /**
     * 设置阅读界面的样式
     *
     */
    private void setReadPageStyle(boolean atNight) {
        ReadPageStyle readPageStyle;
        int color;
        if (atNight) {
            readPageStyle = ReadPageStyle.READ_YD_AtNight;
            color = getColor(R.color.colorReadFontColorWhite);
        } else {
            readPageStyle = GLOBAL_DATA.readPageSettingLog.getReadPageStyle();
            color = getColor(R.color.colorReadFontColorBlack);
        }

        contentTextView.setPaintColor(color);
        readSettingDialog.setFontColor(color, atNight);
        readChaptersDialog.setFontColor(color, atNight);
        readContentSettingDialog.setFontColor(color, atNight);
        readTypefaceSettingDialog.setFontColor(color, atNight);

        setHorizontalScreen();
//        readContentSettingDialog.updateIco(atNight);

        ReadActivity.this.findViewById(R.id.ar_ll_main).setBackgroundResource(readPageStyle.getColorRead());
        readSettingDialog.setReadPageSetting(readPageStyle);
        readChaptersDialog.setReadPageSetting(readPageStyle);
        readContentSettingDialog.setReadPageSetting(readPageStyle);
        readTypefaceSettingDialog.setReadPageSetting(readPageStyle);
    }

    /**
     * 设置对话框按钮监听器
     */
    @SuppressLint("NonConstantResourceId")
    private final View.OnClickListener dialogReadSettingOnClickListener = v -> {
        switch (v.getId()) {
            case R.id.drs_ll_setting_item:
                readSettingDialog.dismiss();
                readContentSettingDialog.show();
                break;
            case R.id.drs_ll_catalog_item:
                readSettingDialog.dismiss();
                readChaptersDialog.show(height);
                break;
            /*case R.id.drs_ll_download_item:
                ToastUtils.show("开始下载.");
                OTHER_EXECUTOR_SERVICE_THREAD_POOL.submit(() -> {
                    for (Chapter chapter : currentChapterList) {
                        if (null != chapter.getcContent() && !"".equals(chapter.getcContent())) {
                            Log.e("缓存", "《" + chapter.getcTitle() + "》已下载。");
                            continue;
                        }
                        Log.e("缓存", "开始下载：" + chapter.getcTitle());
                        getContentAndUpdate(chapter);
                    }
//                    ChapterInfoDao.updateChapterContent(currentChapterList);
                    final Message message = getMessage(4);
                    message.obj = "下载成功。";
                    handler.sendMessage(message);
                });
                break;*/
            case R.id.drs_ll_night_item:
                GLOBAL_DATA.readPageSettingLog.updateAtNight(!GLOBAL_DATA.readPageSettingLog.gAtNight());
                setReadPageStyle(GLOBAL_DATA.readPageSettingLog.gAtNight());

                if (GLOBAL_DATA.readPageSettingLog.gAtNight()){
                    setTheme(R.style.AppThemeNight);
                } else {
                    setTheme(R.style.AppTheme);
                }
                readSettingDialog.changeTheme(getTheme());
                readContentSettingDialog.changeTheme(getTheme());
                readTypefaceSettingDialog.changeTheme(getTheme());
                break;
            case R.id.drs_ll_back:
                ReadActivity.this.finish();
                break;
            case R.id.drs_ll_more:
                Intent intent = new Intent(this, BookInfoActivity.class);
                startActivity(intent);
                break;
            default:
                ToastUtils.show("功能未开发，请尽请期待。");
                break;
        }
    };

    /**
     * 章节列表选择监听器
     */
    AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
        currentBook.setbCurrentReadChapterId(position + 1);
        readChaptersDialog.setItemSelectChapterIndex(position + 1);
        setChapterContentInfo(true);
        updateReadingRecord(currentBook.getbCurrentReadChapterId(), 1);
        readChaptersDialog.dismiss();
    };

    /**
     * 阅读界面按钮点击监听器
     */
    @SuppressLint("NonConstantResourceId")
    View.OnClickListener readContentSettingDialogClick = v -> {
        switch (v.getId()) {
            case R.id.drcs_read_btn_k61:
            case R.id.drcs_read_btn_k62:
            case R.id.drcs_read_btn_k63:
            case R.id.drcs_read_btn_k64:
            case R.id.drcs_read_btn_k65:
            case R.id.drcs_read_btn_k66:
                final ReadPageStyle readPageStyle = readContentSettingDialog.getReadPageStyle(v.getId());
                GLOBAL_DATA.readPageSettingLog.updateReadPageStyle(readPageStyle);
                setReadPageStyle(false);
                break;
            case R.id.drcs_read_btn_at_night:
                GLOBAL_DATA.readPageSettingLog.updateAtNight(true);
                setReadPageStyle(true);
                break;
            case R.id.drcs_tv_font_size_increase:
                setContentFontSize(GLOBAL_DATA.readPageSettingLog.increaseFontSize());
                break;
            case R.id.drcs_tv_font_size_less:
                setContentFontSize(GLOBAL_DATA.readPageSettingLog.lessFontSize());
                break;
            case R.id.drcs_tv_font_typeface:
                readContentSettingDialog.dismiss();
                readTypefaceSettingDialog.show();
                break;
            case R.id.drcs_ll_horizontal_screen_item:
                isWhirling = true;
                GLOBAL_DATA.readPageSettingLog.updateHorizontalScreen(!GLOBAL_DATA.readPageSettingLog.gHorizontalScreen());
                setHorizontalScreen();
                break;
            default:
                ToastUtils.show("功能未开发，请尽请期待。");
                break;
        }
    };

    /**
     * 横竖屏切换
     */
    private void setHorizontalScreen() {
        width = AndroidUtils.getWidth(this);
        height = AndroidUtils.getHeight(this);
        final boolean horizontalScreen = GLOBAL_DATA.readPageSettingLog.gHorizontalScreen();
        readContentSettingDialog.setHorizontalScreenIco(horizontalScreen, GLOBAL_DATA.readPageSettingLog.gAtNight());
        if (!isWhirling) {
            return;
        }
        if (horizontalScreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        isWhirling = false;
    }

    /**
     * 亮度调节器监听器
     */
    SeekBar.OnSeekBarChangeListener readContentSettingDialogSeekBarChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            GLOBAL_DATA.readPageSettingLog.setLuminance(progress);
            if (GLOBAL_DATA.readPageSettingLog.gLuminanceSystem()) {
                return;
            }
            setBrightness(getWindow(), (float) progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            ReadPageSettingDao.update("luminance", String.valueOf(GLOBAL_DATA.readPageSettingLog.getLuminance()));
        }
    };

    /**
     * 亮度 随系统 监听器
     */
    CompoundButton.OnCheckedChangeListener readContentSettingDialogCheckedChange = (buttonView, isChecked) -> {
        GLOBAL_DATA.readPageSettingLog.sLuminanceSystem(isChecked);
        setBrightness(getWindow(), GLOBAL_DATA.readPageSettingLog.gLuminanceSystem() ? WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE : (float) GLOBAL_DATA.readPageSettingLog.getLuminance());

    };

    /**
     * 字体选择
     */
    BaseRecyclerView.OnItemClickListener onTypefaceItemClickListener = (item, position) -> {
        final TypefaceEntity typefaceEntity = (TypefaceEntity) item;
        readTypefaceSettingDialog.setCurrTypefaces(typefaceEntity);
        GLOBAL_DATA.readPageSettingLog.updateTypeface(typefaceEntity.getId());
        setTypeface(typefaceEntity);
    };

    /**
     * 设置字体样式
     * @param typefaceEntity 字体信息
     */
    private void setTypeface(TypefaceEntity typefaceEntity){
        Typeface typeface = typefaceEntity.getId() == 0 ? Typeface.SANS_SERIF : getResources().getFont(typefaceEntity.getResourceNumber());
        contentTextView.setTypeface(typeface);
    }


    /**
     * 初始化图书正文信息
     */
    private void initBookInfo(int currentPage, boolean headDraw) {
        final Chapter chapter = currentChapterList.get(currentBook.currentReadChapterIdIndex());
        readChaptersDialog.notifyDataSetChanged();
        readSettingDialog.setChapterName(chapter.getcTitle());
        contentTextView.stopDrawLoadingInfo();
        contentTextView.setChapterInfo(currentBook, chapter, currentPage, true, headDraw);
        contentTextView.startDrawContentInfo();
    }

    /**
     * 初始化书籍目录信息
     */
    private void initCatalogInfo() {
        contentTextView.startDrawLoadingInfo();
        // 书籍目录如不为空则直接设置目录信息
        if (currentChapterList.isEmpty()) {
            // 从本地读取书籍目录
            ChapterInfoDao.readChapters(currentBook, currentChapterList);
            if (currentChapterList.isEmpty()) {
                getChapters(currentBook, currentChapterList, true, handler, getMessage(1));
                return;
            }
        }
        final Chapter chapter = currentChapterList.get(currentBook.currentReadChapterIdIndex());
        if (null == chapter.getcContent()) {
            getChapterContent(handler, getMessage(1), true, true);
            return;
        }
        initBookInfo(currentBook.getbCurrentReadChapterPage(), true);
    }

    /**
     * 设置章节信息
     *
     * @param headDraw 是否从头部开始绘制
     */
    private void setChapterContentInfo(boolean headDraw) {
        contentTextView.stopDrawContentInfo();
        contentTextView.startDrawLoadingInfo();
        final Chapter chapter = currentChapterList.get(currentBook.currentReadChapterIdIndex());
        readChaptersDialog.setItemSelectChapterIndex(currentBook.getbCurrentReadChapterId());
        readSettingDialog.setChapterName(chapter.getcTitle());
        getChapterContent(handler, getMessage(2), headDraw, true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldEventX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                switch (touchMoveDirection(event.getX(), event.getY())) {
                    case LEFT:
                        nextPage();
                        break;
                    case RIGHT:
                        prevPage();
                        break;
                    case MIDDLE:
                        readSettingDialog.show();
                        break;
                    default:
                        System.out.println("default");
                        break;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 上一章
     */
    public void prevChapter() {
        if (contentTextView.isDrawLoadingInfo()) {
            ToastUtils.show("正在加载章节...");
            return;
        }
        if (currentBook.lessCurrentReadChapterId()) {
            ToastUtils.show("当前已是第一页.");
            return;
        }
        setChapterContentInfo(true);
        updateReadingRecord(currentBook.getbCurrentReadChapterId(), 1);
    }

    /**
     * 上一页
     */
    private void prevPage() {
        if (contentTextView.isDrawLoadingInfo()) {
            ToastUtils.show("正在加载章节...");
            return;
        }
        if (!contentTextView.prevPage()) {
            updateReadingRecord(-1, contentTextView.getCurrentPage());
            return;
        }
        if (currentBook.lessCurrentReadChapterId()) {
            ToastUtils.show("当前已是第一页.");
            return;
        }
        setChapterContentInfo(false);
        updateReadingRecord(currentBook.getbCurrentReadChapterId(), 1);
    }

    @Override
    protected void onDestroy() {
        isWhirling = true;

        refreshBatteryAndTime = false;
        readSettingDialog.finish();
        readContentSettingDialog.finish();
        readTypefaceSettingDialog.finish();
        readChaptersDialog.finish();

        readSettingDialog = null;
        readContentSettingDialog = null;
        readTypefaceSettingDialog = null;
        readChaptersDialog = null;
        super.onDestroy();
    }

    /**
     * 下一章
     */
    public void nextChapter() {
        if (contentTextView.isDrawLoadingInfo()) {
            ToastUtils.show("正在加载章节...");
            return;
        }

        if (currentBook.increaseCurrentReadChapterId()) {
            contentTextView.stopDrawContentInfo();
            contentTextView.startDrawLoadingInfo("正在获取最新章节...");
            getChapters(currentBook, currentChapterList, true, handler, getMessage(3));
            return;
        }

        if (null != currentChapterList.get(currentBook.currentReadChapterIdIndex()).getcContent()) {
            initBookInfo(currentBook.getbCurrentReadChapterPage(), true);
            updateReadingRecord(currentBook.getbCurrentReadChapterId(), 1);
            return;
        }

        setChapterContentInfo(true);
        updateReadingRecord(currentBook.getbCurrentReadChapterId(), 1);
    }

    /**
     * 下一页
     */
    protected void nextPage() {
        if (contentTextView.isDrawLoadingInfo()) {
            ToastUtils.show("正在加载章节...");
            return;
        }
        if (!contentTextView.nextPage()) {
            updateReadingRecord(-1, contentTextView.getCurrentPage());
            return;
        }
        if (currentBook.increaseCurrentReadChapterId()) {
            contentTextView.stopDrawContentInfo();
            contentTextView.startDrawLoadingInfo("正在获取最新章节...");
            getChapters(currentBook, currentChapterList, true, handler, getMessage(3));
            return;
        }
        setChapterContentInfo(true);
        updateReadingRecord(currentBook.getbCurrentReadChapterId(), 1);
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_CHAPTER_CODE && resultCode == SELECT_CHAPTER_CODE) {
            contentTextView.stopDrawContentInfo();
            contentTextView.startDrawLoadingInfo();
            getChapterContent(handler, getMessage(1), true, true);
        }
    }*/

    private float oldEventX;

    /**
     * 获取触碰位置
     *
     * @param x x
     * @param y y
     * @return 位置
     */
    public TouchMoveDirection touchMoveDirection(float x, float y) {
        float diff = 1e-6f;
        float height21 = height / 2.0f;
        float height31 = height21 / 2.0f;
        float width21 = width / 2.0f;
        float width31 = width21 / 2.0f;

        if (Math.abs(x - oldEventX) < diff) {
            boolean mid = (x >= width21 - width31 && x <= width21 + width31) &&
                    (y >= height21 - height31 && y <= height21 + height31);
            if (mid) {
                return TouchMoveDirection.MIDDLE;
            }
            if (oldEventX > width21) {
                return TouchMoveDirection.LEFT;
            }
        } else if (x - oldEventX < 0.0) {
            return TouchMoveDirection.LEFT;
        }
        return TouchMoveDirection.RIGHT;
    }
}