package com.xrzx.reader.book.http;

import android.annotation.SuppressLint;
import android.text.Html;

import com.xrzx.commonlibrary.callback.ResponseCallBack;
import com.xrzx.commonlibrary.callback.ResultCallBack;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.entity.Chapter;
import com.xrzx.commonlibrary.utils.http.HttpUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/12 11:15
 */
public class TianLaiCrawling implements BaseCrawling {
    private static final String HOME_URL = "https://www.23txt.com";
    private static final String CHAR_ENCODING = "UTF-8";
    private static final String CHAR_ENCODING_CHAPTERS = "GBK";

    @Override
    public void searchBooks(String searchKey, ArrayList<Book> bookList, ResultCallBack<List<Book>> callBack) {
        searchBooks(searchKey, 1, 1, true, bookList, callBack);
    }

    @SuppressLint("DefaultLocale")
    public void searchBooks(String searchKey, int page, int countPage, boolean getCountPage, ArrayList<Book> bookList, ResultCallBack<List<Book>> callBack) {
        HttpUtils.getHtmlStringByGet(HOME_URL + "/search.php" + String.format("?q=%s&p=%d", searchKey, page), CHAR_ENCODING, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                Document doc;
                Elements divList;
                try {
                    doc = Jsoup.parse(data);
                    Element bookMain = doc.getElementsByClass("result-list").first();
                    divList = bookMain.getElementsByClass("result-game-item-detail");
                    if (divList == null || divList.isEmpty()) {
                        callBack.onError(new Exception("获取失败"));
                        return;
                    }
                } catch (Exception e) {
                    callBack.onError(e);
                    return;
                }

                for (int i = 0; i < divList.size(); i++) {
                    try {
                        Element element = divList.get(i);
                        Element td = element.getElementsByClass("result-game-item-detail").first();

                        Element nameTag = td.getElementsByClass("result-game-item-title").first().getElementsByTag("a").first();
                        Elements info = td.getElementsByClass("result-game-item-info").first().getElementsByTag("p");

                        String name = nameTag.attr("title");
                        String bookUrl = nameTag.attr("href");
                        String introduction = td.getElementsByClass("result-game-item-desc").first().html();
                        String author = info.get(0).getElementsByTag("span").get(1).html();
                        String type = info.get(1).getElementsByTag("span").get(1).html();
                        String lastUpdateTime = info.get(2).getElementsByTag("span").get(1).html();
                        String lastUpdateChapter = info.get(3).getElementsByTag("a").get(0).html();

                        Book book = new Book(name, author, type, introduction, HOME_URL + bookUrl, HOME_URL + bookUrl, lastUpdateTime, lastUpdateChapter);
                        bookList.add(book);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                int cPage = countPage;
                if (getCountPage) {
                    Element pageMain = doc.getElementsByClass("search-result-page-main").first();
                    if (null != pageMain) {
                        final String href = pageMain.getElementsByTag("a").last().attr("href");
                        final String[] split = href.split("=");
                        cPage = Integer.parseInt(split[2]);
                        System.out.println(countPage);
                    }
                }
                int currPage = page;
                if (++currPage <= cPage) {
                    searchBooks(searchKey, currPage, cPage, false, bookList, callBack);
                    return;
                }
                callBack.onSuccess(bookList);
            }

            @Override
            public void onError(Exception e) {
                callBack.onError(e);
            }
        });
    }

    @Override
    public void getBookDetailsInfo(Book book, ResultCallBack<Book> callBack) {
        callBack.onSuccess(book);
    }

    @Override
    public void updateBookDetailsInfo(Book book, ResultCallBack<Book> callBack) {
        Book newBook = new Book();
        HttpUtils.getHtmlStringByGet(book.getbBookUrl(), CHAR_ENCODING_CHAPTERS, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                try {
                    Document doc = Jsoup.parse(data);
                    // 获取图书类型
//                    Element mainInfo = doc.getElementById("maininfo");
                    Element info = doc.getElementById("info");
//                    final String name = info.getElementsByTag("h1").html();
                    final Elements p = info.getElementsByTag("p");
//                    final String author = p.get(0).html().replace("作    者：","");
                    final String lastUpdateTime = p.get(2).html().replace("最后更新：", "");
                    final String lastUpdateChapter = p.get(3).getElementsByTag("a").first().html();
//                    final String intro = mainInfo.getElementById("intro").html();
                    newBook.setbLastUpdateTime(lastUpdateTime);
                    newBook.setbLastUpdateChapter(lastUpdateChapter);
                    callBack.onSuccess(newBook);
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

    @Override
    public void getBookChapters(Book book, ArrayList<Chapter> chapterList, ResultCallBack<ArrayList<Chapter>> callBack) {
        HttpUtils.getHtmlStringByGet(book.getbChapterUrl(), CHAR_ENCODING_CHAPTERS, new ResponseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                Elements ddItem;
                try {
                    Document doc = Jsoup.parse(data);
                    Element divList = doc.getElementById("list");
                    if (null == divList) {
                        callBack.onError(new Exception("获取失败"));
                        return;
                    }
                    Element dl = divList.getElementsByTag("dl").first();
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
                            chapterList.add(new Chapter(book.getbUniquelyIdentifies(), ++cNumber, title, HOME_URL + url));
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

    @Override
    public void getBookChapterContent(Chapter chapter, ResultCallBack<String> callBack) {
        HttpUtils.getHtmlStringByGet(chapter.getcUrl(), CHAR_ENCODING_CHAPTERS, new ResponseCallBack<String>() {
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
                            .replace("\n\n", "\n");
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
