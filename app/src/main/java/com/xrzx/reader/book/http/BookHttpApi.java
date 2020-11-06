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
        HttpUtils.getHtmlStringByGet(book.getbBookUrl(), CHAR_ENCODING, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                try {
                    Document doc = Jsoup.parse(data);
                    String lastUpdateTime = doc.getElementById("info").getElementsByTag("p").get(2).html().replace("最后更新：", "");
                    String lastUpdateChapter = doc.getElementById("info").getElementsByTag("p").get(3).getElementsByTag("a").first().html();

                    List<Book> books = BookInfoDao.findSelection("b_name = ? and b_author = ?", book.getbName(), book.getbAuthor());
                    if (!books.isEmpty()) {
                        final Book book1 = books.get(0);
                        book.setbId(book1.getbId());
                        book.setbUniquelyIdentifies(book1.getbUniquelyIdentifies());
                        book.setbType(book1.getbType());
                        book.setbIntroduction(book1.getbIntroduction());
                        book.setbCurrentReadChapterId(book1.getbCurrentReadChapterId());
                        book.setbCurrentReadChapterPage(book1.getbCurrentReadChapterPage());
                        book.setBookShelf(true);
                        /*if (!book1.getbLastUpdateChapter().equals(lastUpdateChapter)) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("b_last_update_chapter", lastUpdateChapter);
                            contentValues.put("b_last_update_time", lastUpdateTime);
                            BookInfoDao.update(contentValues, "b_uniquely_identifies = ?", book1.getbUniquelyIdentifies());
                        }*/
                        callBack.onSuccess(book);
                        return;
                    }
                    Element conTop = doc.getElementsByClass("con_top").first();
                    String type = conTop.getElementsByTag("a").get(2).html();
                    String introduction = doc.getElementById("intro").getElementsByTag("p").get(1).html();
                    book.setbType(type);
                    book.setbIntroduction(introduction);
                    book.setbUniquelyIdentifies(BookInfoDao.getUniquelyIdentifies());
                    book.setbLastUpdateTime(lastUpdateTime);
                    book.setbLastUpdateChapter(lastUpdateChapter);
                    book.setbCurrentReadChapterId(1);
                    book.setbCurrentReadChapterPage(1);
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
                Elements ddItem = null;
                try {
                    Document doc = Jsoup.parse(data);
                    Element divList = doc.getElementById("list");
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
                            char startChar = url.charAt(0);
                            if (startChar != '/') {
                                url = "/" + url;
                            }
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
                    String content = Html.fromHtml(divContent.html(), Html.FROM_HTML_MODE_LEGACY).toString();
                    char c1 = 160, c2 = 32;
                    String spaec1 = "" + c1;
                    content = content
                            .replace(spaec1, "  ")
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