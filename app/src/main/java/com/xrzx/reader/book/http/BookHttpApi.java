package com.xrzx.reader.book.http;

import android.content.ContentValues;
import android.text.Html;

import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.entity.Chapter;
import com.xrzx.commonlibrary.callback.ResponseCallBack;
import com.xrzx.commonlibrary.callback.ResultCallBack;
import com.xrzx.commonlibrary.database.dao.BookInfoDao;
import com.xrzx.commonlibrary.utils.SQLiteUtils;
import com.xrzx.commonlibrary.utils.http.HttpUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * @Description 书籍爬取Api
 * @Author ks
 * @Date 2020/10/26 11:37
 */
 class BookHttpApi {
    private static final String XBIQUGE_HOME_URL = "http://www.xbiquge.la";
    private static final String CHAR_ENCODING = "utf-8";

    /**
     * 搜索图书信息
     *
     * @param searchDict 搜索字典
     * @param bookList   图书列表
     * @param callBack   回调
     */
    public static void searchBooks(final FormBody.Builder searchDict, final ArrayList<Book> bookList, final ResultCallBack<List<Book>> callBack) {
        HttpUtils.getHtmlStringByPost("http://www.xbiquge.la/modules/article/waps.php", searchDict, CHAR_ENCODING, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                Elements tr;
                try {
                    Document doc = Jsoup.parse(data);
                    Element divTable = doc.getElementsByClass("grid").first();
                    tr = divTable.getElementsByTag("tr");
                    if (tr == null || tr.isEmpty()) {
                        callBack.onError(new Exception("获取失败"));
                        return;
                    }
                } catch (Exception e) {
                    callBack.onError(e);
                    return;
                }
                for (int i = 1; i < tr.size(); i++) {
                    try {
                        Element element = tr.get(i);
                        Elements td = element.getElementsByTag("td");
                        Element nameTag = td.get(0).getElementsByTag("a").first();
                        String name = nameTag.html();
                        String author = td.get(2).html();
                        String lastUpdateChapter = td.get(1).getElementsByTag("a").first().html();
                        String lastUpdateTime = td.get(3).html();
                        Book book = new Book(name, author,
                                nameTag.attr("href"),
                                nameTag.attr("href"),
                                lastUpdateTime, lastUpdateChapter);
                        bookList.add(book);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                callBack.onSuccess(bookList);
            }

            @Override
            public void onError(Exception e) {
                callBack.onError(e);
            }
        });
    }

    /**
     * 获取书籍详细信息
     *
     * @param book     书籍信息
     * @param callBack 回调
     */
    public static void getBookDetailsInfo(final Book book, final ResultCallBack<Book> callBack) {
        Book newBook = book.isBookShelf() ? book.clone() : book;
        HttpUtils.getHtmlStringByGet(newBook.getbBookUrl(), CHAR_ENCODING, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                try {
                    Document doc = Jsoup.parse(data);
                    // 获取图书类型
                    Element conTop = doc.getElementsByClass("con_top").first();
                    String type = conTop.getElementsByTag("a").get(2).html();
                    newBook.setbType(type);
                    // 获取图书简介
                    String introduction = doc.getElementById("intro").getElementsByTag("p").get(1).html();
                    newBook.setbIntroduction(introduction);
                    // 获取书籍最后更新时间
                    String lastUpdateTime = doc.getElementById("info").getElementsByTag("p").get(2).html().replace("最后更新：", "");
                    newBook.setbLastUpdateTime(lastUpdateTime);
                    // 获取书籍最后更新章节名称
                    String lastUpdateChapter = doc.getElementById("info").getElementsByTag("p").get(3).getElementsByTag("a").first().html();
                    newBook.setbLastUpdateChapter(lastUpdateChapter);
                    // 设置书籍当前阅读章和页
                    newBook.setbCurrentReadChapterId(1);
                    newBook.setbCurrentReadChapterPage(1);
                    callBack.onSuccess(book);
                } catch (Exception e) {
                    callBack.onError(e);
                }
            }

            @Override
            public void onError(Exception e) {
                callBack.onError(e);
            }
        });
    }

    /**
     * 获取目录信息
     *
     * @param book     书籍
     * @param callBack 回调
     */
    public static void getBookChapters(final Book book, final ArrayList<Chapter> chapterList, final ResultCallBack<ArrayList<Chapter>> callBack) {
        HttpUtils.getHtmlStringByGet(book.getbChapterUrl(), CHAR_ENCODING, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                Elements ddItem;
                try {
                    Document doc = Jsoup.parse(data);
                    Element divList = doc.getElementById("list");
                    if (null == divList) {
                        callBack.onError(new Exception("获取失败"));
                    }
                    Element dl = divList.getElementsByTag("dl").get(0);
                    ddItem = dl.getElementsByTag("dd");
                    if (ddItem == null || ddItem.isEmpty()) {
                        callBack.onError(new Exception("获取失败。"));
                        return;
                    }
                } catch (Exception e) {
                    callBack.onError(e);
                    return;
                }
                int cNumber = 0;
                for (Element dd : ddItem) {
                    try {
                        Elements as = dd.getElementsByTag("a");
                        if (as.size() > 0) {
                            Element a = as.get(0);
                            String title = a.html();
                            String url = as.attr("href");
                            url = (url.charAt(0) != '/') ? "/" + url : url;
                            chapterList.add(new Chapter(book.getbUniquelyIdentifies(), ++cNumber, title, XBIQUGE_HOME_URL + url));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                callBack.onSuccess(chapterList);
            }

            @Override
            public void onError(Exception e) {
                callBack.onError(e);
            }
        });
    }

    /**
     * 获取章节正文
     *
     * @param chapter  章节
     * @param callBack 回调
     */
    public static void getBookChapterContent(final Chapter chapter, final ResultCallBack<String> callBack) {
        HttpUtils.getHtmlStringByGet(chapter.getcUrl(), CHAR_ENCODING, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                try {
                    Document doc = Jsoup.parse(data);
                    Element divContent = doc.getElementById("content");
                    if (null == divContent) {
                        callBack.onError(new Exception("获取失败"));
                    }
                    String content = Html.fromHtml(divContent.html(), Html.FROM_HTML_MODE_LEGACY).toString();
                    char c = 160;
                    String space = "" + c;
                    content = content
                            .replace(space, "  ")
                            .replace("    ", "  ")
                            .replace("    ", "  ")
                            .replace("\n\n", "\n")
                            .replace("亲,点击进去,给个好评呗,分数越高更新越快,据说给新笔趣阁打满分的最后都找到了漂亮的老婆哦!\n" +
                                    "手机站全新改版升级地址：http://m.xbiquge.la，数据和书签与电脑站同步，无广告清新阅读！", "");
                    callBack.onSuccess(content);
                } catch (Exception e) {
                    callBack.onError(e);
                }
            }

            @Override
            public void onError(Exception e) {
                callBack.onError(e);
            }
        });
    }
}