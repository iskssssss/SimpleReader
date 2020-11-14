package com.xrzx.commonlibrary.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.xrzx.commonlibrary.callback.ResultCallBack;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.entity.Chapter;
import com.xrzx.commonlibrary.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author ks
 * @Date 2020/10/26 22:23
 */
public class ChapterContentTextView extends View {
    /**
     * 主内容画笔
     */
    private final Paint mainPaint;

    /**
     * 其他内容画笔
     */
    private final Paint otherPaint;

    public void setPaintColor(int color) {
        mainPaint.setColor(color);
        otherPaint.setColor(color);
        postInvalidate();
    }

    private Typeface typeface;

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        mainPaint.setTypeface(typeface);
        otherPaint.setTypeface(typeface);
        postInvalidate();
    }

    /**
     * 字体大小
     */
    private float fontSize = AndroidUtils.sp2px(getContext(), 16);

    public void setFontSize(int fontSize) {
        this.fontSize = AndroidUtils.sp2px(getContext(), (float) fontSize);
        this.contents.clear();
    }

    /**
     * 行间距
     */
    private float rowSpacing = AndroidUtils.sp2px(getContext(), 12);

    public void setRowSpacing(int rowSpacing) {
        this.rowSpacing = AndroidUtils.sp2px(getContext(), (float) rowSpacing);
        this.contents.clear();
    }

    /**
     * 页边距
     */
    private float pageSpacing = AndroidUtils.sp2px(getContext(), 24);

    public void setPageSpacing(int pageSpacing) {
        this.pageSpacing = AndroidUtils.sp2px(getContext(), (float) pageSpacing);
        this.contents.clear();
    }

    /**
     * 头部间距
     */
    private int topSpacing = AndroidUtils.sp2px(getContext(), 48);

    /**
     * 尾部间距
     */
    private int buttonSpacing = AndroidUtils.sp2px(getContext(), 48);

    /**
     * 文章标题
     */
    private Book book;
    /**
     * 文章内容
     */
    private Chapter chapter;
    /**
     * 文章分段内容
     */
    private final Map<Integer, ArrayList<String>> contents;
    /**
     * 容器宽度
     */
    private float width;
    /**
     * 容器高度
     */
    private float height;
    /**
     * 字高度
     */
    private float fontHeight;
    /**
     * 行打印字数
     */
    private int rowWordCount;
    /**
     * 列打印行数
     */
    private int colCount;
    /**
     * 字间距
     */
    private float wordSpacing;
    /**
     * 字宽度
     */
    private float fontWidth;
    /**
     * 总行数
     */
    private int totalRowCount;
    /**
     * 总页数
     */
    private int totalPages;

    public int getTotalPages() {
        return totalPages;
    }

    /**
     * 当前页数
     */
    private int currentPage = 1;

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * 是否开始绘制
     */
    private boolean drawContentInfo = false;

    public boolean isDrawContentInfo() {
        return drawContentInfo;
    }

    public void startDrawContentInfo() {
        this.drawContentInfo = true;
        invalidate();
    }

    public void stopDrawContentInfo() {
        this.drawContentInfo = false;
    }

    /**
     * 是否绘制加载信息
     */
    private boolean drawLoadingInfo = false;

    private String messageOne = "章节信息";

    public void setMessageOne(String messageOne) {
        this.messageOne = messageOne;
    }

    private String messageTwo = "加载中...";

    public void setMessageTwo(String messageTwo) {
        this.messageTwo = messageTwo;
    }

    public boolean isDrawLoadingInfo() {
        return drawLoadingInfo;
    }

    public void startDrawLoadingInfo() {
        startDrawLoadingInfo("章节信息");
    }

    public void startDrawLoadingInfo(String loadMessage) {
        setMessageOne(loadMessage);
        this.drawLoadingInfo = true;
        invalidate();
    }

    public void stopDrawLoadingInfo() {
        this.drawLoadingInfo = false;
    }

    /**
     * 是否从第一页开始绘制
     */
    private boolean headDraw = true;

    public ChapterContentTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        contents = new HashMap<>();
        mainPaint = new Paint();
        mainPaint.setAntiAlias(true);
        otherPaint = new Paint();
        otherPaint.setAntiAlias(true);
    }

    /**
     * 上一页
     *
     * @return 是否到首页
     */
    public boolean prevPage() {
        currentPage--;
        if (currentPage < 1) {
            currentPage = 1;
            return true;
        }
        invalidate();
        return false;
    }

    /**
     * 上一页
     *
     * @return 是否到尾页
     */
    public boolean nextPage() {
        currentPage++;
        if (currentPage > totalPages) {
            currentPage = totalPages;
            return true;
        }
        invalidate();
        return false;
    }

    /**
     * 设置书籍信息
     *
     * @param book 书籍
     */
    public void setTitle(Book book) {
        this.book = book;
    }

    /**
     * 设置内容
     *
     * @param chapter 文章内容
     */
    public void setChapterInfo(Book book, Chapter chapter) {
        setChapterInfo(book, chapter, 1, false, true);
    }

    /**
     * 设置内容(立刻绘制 并从头部开始绘制)
     *
     * @param chapter 文章内容
     */
    public void setChapterInfo(Book book, Chapter chapter, int currentPage) {
        setChapterInfo(book, chapter, currentPage, true, true);
    }

    /**
     * 设置内容
     *
     * @param chapter 文章内容
     */
    public void setChapterInfo(Book book, Chapter chapter, boolean invalidate) {
        setChapterInfo(book, chapter, 1, invalidate, true);
    }

    /**
     * 设置内容
     *
     * @param chapter 文章内容
     */
    public void setChapterInfo(Book book, Chapter chapter, int currentPage, boolean invalidate, boolean headDraw) {
        this.book = book;
        this.chapter = chapter;
        this.headDraw = headDraw;
        this.currentPage = currentPage;
        this.contents.clear();
        if (invalidate) {
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.drawLoadingInfo) {
            drawLoadingInfo(canvas);
            return;
        }
        if (this.drawContentInfo) {
            if (contents.isEmpty()) {
                try {
                    initParams();
                    contentStr2List();

                    currentPage = headDraw ? currentPage : totalPages;
                } catch (Exception e) {
                    return;
                }
            }
            drawChapterInfo(canvas);
            printContent(canvas);
        }
    }

    /**
     * 绘制加载信息
     *
     * @param canvas 画布
     */
    private void drawLoadingInfo(Canvas canvas) {
        float loadWidth = getWidth();
        float loadHeight = getHeight();

        int textSize = AndroidUtils.sp2px(getContext(), 16);
        setOtherPaintInfo(Paint.Align.CENTER, textSize, Typeface.NORMAL);

        canvas.drawText(messageOne,
                loadWidth / 2f,
                loadHeight / 2f,
                otherPaint);
        canvas.drawText(messageTwo,
                loadWidth / 2f,
                loadHeight / 2f + (textSize * 2f),
                otherPaint);
    }

    /**
     * 设置次要画笔参数
     *
     * @param align    位置
     * @param textSize 大小
     * @param style    其他样式(斜体，粗体)
     */
    private void setOtherPaintInfo(Paint.Align align, int textSize, int style) {
        otherPaint.setTypeface(Typeface.create(typeface, style));
        otherPaint.setTextAlign(align);
        otherPaint.setTextSize(textSize);
    }

    /**
     * 绘制章节头部和尾部信息
     *
     * @param canvas 画布
     */
    private void drawChapterInfo(Canvas canvas) {
        float loadHeight = getHeight();
        int textSize = AndroidUtils.sp2px(getContext(), 12);
        setOtherPaintInfo(Paint.Align.LEFT, textSize, Typeface.BOLD);

        canvas.drawText(currentPage == 1 ? book.getbName() : chapter.getcTitle(),
                pageSpacing,
                (topSpacing / 2f) + (textSize / 2f),
                otherPaint);

        canvas.drawText(currentPage + "/" + totalPages,
                pageSpacing,
                loadHeight - (buttonSpacing / 2f) + (textSize / 2f),
                otherPaint);
        drawBatteryAndTime(canvas);
    }

    int currentLevel;
    String dateTime;
    public void setBatteryAndTime(int currentLevel, String dateTime){
        this.currentLevel = currentLevel;
        this.dateTime = dateTime;
        postInvalidate();
    }

    private void  drawBatteryAndTime(Canvas canvas) {
        float loadHeight = getHeight();
        int textSize = AndroidUtils.sp2px(getContext(), 12);
        setOtherPaintInfo(Paint.Align.RIGHT, textSize, Typeface.BOLD);

        float x = width + pageSpacing - 6f;
        float y = loadHeight - (buttonSpacing / 2f) + (textSize / 2f) + 4f;
        float h = (float) textSize;
        float w = AndroidUtils.sp2px(getContext(), 32f);
        float bW = AndroidUtils.sp2px(getContext(), 3f);

        //绘制电池边框
        RectF re1 = new RectF(x, y - h, x - w, y);
        otherPaint.setStyle(Paint.Style.STROKE);
        otherPaint.setStrokeWidth(1.5f);
        canvas.drawRect(re1, otherPaint);

        // 绘制电池正极
        RectF re2 = new RectF(x + bW, (y - h) + (bW / 2), x, y - (bW / 2));
        otherPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(re2, otherPaint);

        // 绘制当前时间
        y -= 3f;
        otherPaint.setStrokeWidth(1f);
        otherPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(dateTime, x - (w + 4f), y, otherPaint);

        // 绘制电池容量
        x -= (w / 2);
        setOtherPaintInfo(Paint.Align.CENTER, textSize, Typeface.BOLD);
        canvas.drawText(currentLevel + "%", x, y, otherPaint);
    }

    /**
     * 将内容分段存储
     */
    public void contentStr2List() {
        String content = chapter.getcTitle() + "\n" + chapter.getcContent();
        String[] paragraphs = content.split("\n");
        for (int i = 0; i < paragraphs.length; i++) {
            String paragraph = paragraphs[i];
            int paragraphLen = paragraph.length();
            int row = (int) Math.ceil(((float) paragraphLen / rowWordCount));
            totalRowCount += row;
            ArrayList<String> rowContentList = new ArrayList<>();
            contents.put(i + 1, rowContentList);
            for (int r = 0; r < row; r++) {
                int printWordCount = rowWordCount;
                if (paragraphLen < rowWordCount) {
                    printWordCount = paragraphLen;
                }
                int beginIndex = (r * rowWordCount);
                int endIndex = beginIndex + printWordCount;
                final String str = paragraph.substring(beginIndex, endIndex);
                if ("".equals(str) || " ".equals(str)) {
                    --totalRowCount;
                    paragraphLen -= rowWordCount;
                    continue;
                }
                rowContentList.add(str);
                paragraphLen -= rowWordCount;
            }
        }
        totalPages = (int) Math.ceil(((float) totalRowCount / colCount));
    }

    /**
     * 初始化参数
     */
    public void initParams() {
        width = getWidth() - (pageSpacing * 2);
        height = getHeight() - topSpacing - buttonSpacing;
        mainPaint.setTextSize(fontSize);

        fontHeight = fontSize + rowSpacing;
        rowWordCount = (int) (width / fontSize);
        colCount = (int) (height / fontHeight);
        wordSpacing = (width - (rowWordCount * fontSize)) / (rowWordCount - 1);
        fontWidth = fontSize + wordSpacing;

        totalRowCount = 0;
        totalPages = 0;
    }

    /**
     * 打印内容
     *
     * @param canvas 画布
     */
    private void printContent(Canvas canvas) {
        //  打印开始段落              打印结束段落
        int startPrintParagraph = 1, endPrintParagraph = 0;
        //  打印开始段落行数
        int startPrintParagraphRow = 0;
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int endPrintRow = (currentPage >= totalPages) ? totalRowCount : (currentPage * colCount);
        int sum = 0;
        int key = 1;
        int pages = 1;
        do {
            ArrayList<String> rowContentList = contents.get(key);
            if (null == rowContentList) {
                continue;
            }
            sum += rowContentList.size();
            endPrintParagraph++;
            int cRowC = pages * colCount;
            if (sum > cRowC && endPrintRow != cRowC) {
                pages++;
                startPrintParagraphRow = rowContentList.size() - (sum - cRowC);
                startPrintParagraph = endPrintParagraph;
            }
            key++;
        } while (sum < endPrintRow);

        float x = pageSpacing, y = topSpacing + fontSize;
        int numberOfLinesPrinted = 0;

        int startKey = startPrintParagraph;
        int endKey = startPrintParagraph + endPrintParagraph;
        if (startPrintParagraph == 1) {
            endKey--;
        }

        // 打印文章
        for (int k = startKey; k <= endKey; k++, startPrintParagraphRow = 0) {
            ArrayList<String> rowContentList = contents.get(k);
            mainPaint.setTypeface(Typeface.create(typeface, (k == 1) ? Typeface.BOLD : Typeface.NORMAL));
            if (null == rowContentList) {
                continue;
            }
            for (int i = startPrintParagraphRow; i < rowContentList.size(); i++) {
                String rowContent = rowContentList.get(i);
                numberOfLinesPrinted++;
                if (numberOfLinesPrinted > colCount) {
                    break;
                }
                for (int i1 = 0; i1 < rowContent.length(); i1++) {
                    canvas.drawText(String.valueOf(rowContent.charAt(i1)), x, y, mainPaint);
                    x += fontWidth;
                }
                x = pageSpacing;
                y += fontHeight;
            }
            if (numberOfLinesPrinted > colCount) {
                break;
            }
            y += (((height - (colCount * fontHeight)) + rowSpacing) / (endPrintParagraph - startPrintParagraph + 1));
        }
    }

    /**
     * 回调
     */
    private ResultCallBack<Object> callBack;

    public void setCallBack(ResultCallBack<Object> objectResultCallBack) {
        this.callBack = objectResultCallBack;
    }
}
