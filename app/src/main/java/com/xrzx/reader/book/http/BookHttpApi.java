package com.xrzx.reader.book.http;

import android.text.Html;

import com.xrzx.reader.book.entity.Book;
import com.xrzx.reader.book.entity.Chapter;
import com.xrzx.reader.common.callback.ResponseCallBack;
import com.xrzx.reader.common.callback.ResultCallBack;
import com.xrzx.reader.common.http.utils.HttpUtils;

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
public class BookHttpApi {
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
                Elements tr = null;
                try {
                    Document doc = Jsoup.parse(data);
                    Element divTable = doc.getElementsByClass("grid").first();
                    tr = divTable.getElementsByTag("tr");
                    if (tr == null || tr.isEmpty()) {
                        callBack.onError(new Exception("获取失败"));
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
     *
     * 获取书籍详细信息
     * @param book 书籍信息
     * @param callBack 回调
     */
    public static void getBookDetailsInfo(final Book book, final ResultCallBack<Book> callBack) {
        HttpUtils.getHtmlStringByGet(book.getbBookUrl(), CHAR_ENCODING, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                Document doc = Jsoup.parse(data);
                Element con_top = doc.getElementsByClass("con_top").first();
                String type = con_top.getElementsByTag("a").get(2).html();
                String introduction = doc.getElementById("intro").getElementsByTag("p").get(1).html();
                String lastUpdateTime = doc.getElementById("info").getElementsByTag("p").get(2).html().replace("最后更新：", "");
                String lastUpdateChapter = doc.getElementById("info").getElementsByTag("p").get(3).getElementsByTag("a").first().html();
                book.setbType(type);
                book.setbIntroduction(introduction);
                book.setbLastUpdateTime(lastUpdateTime);
                book.setbLastUpdateChapter(lastUpdateChapter);
                callBack.onSuccess(book);
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
    public static void getBookChapters(final Book book, final ArrayList<Chapter> chapterList, final ResultCallBack<List<Chapter>> callBack) {
        HttpUtils.getHtmlStringByGet(book.getbChapterUrl(), CHAR_ENCODING, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                Elements ddItem = null;
                try {
                    Document doc = Jsoup.parse(data);
                    Element divList = doc.getElementById("list");
                    Element dl = divList.getElementsByTag("dl").get(0);
                    ddItem = dl.getElementsByTag("dd");
                    if (ddItem == null || ddItem.isEmpty()) {
                        callBack.onError(new Exception("获取失败。"));
                    }
                } catch (Exception e) {
                    callBack.onError(e);
                }
                for (Element dd : ddItem) {
                    try {
                        Elements as = dd.getElementsByTag("a");
                        if (as.size() > 0) {
                            Element a = as.get(0);
                            String title = a.html();
                            String url = as.attr("href");
                            char startChar = url.charAt(0);
                            if (startChar != '/') {
                                url = "/" + url;
                            }
                            chapterList.add(new Chapter(title, XBIQUGE_HOME_URL + url));
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
     * @param chapter 章节
     * @param callBack    回调
     */
    public static void getBookChapterContent(final Chapter chapter, final ResultCallBack<String> callBack) {
        HttpUtils.getHtmlStringByGet(chapter.getcUrl(), CHAR_ENCODING, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                try {
                    Document doc = Jsoup.parse(data);
                    Element divContent = doc.getElementById("content");
                    String content = Html.fromHtml(divContent.html()).toString();
                    char c = 160;
                    String spaec = "" + c;
                    content = content.replace(spaec, "  ");
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
