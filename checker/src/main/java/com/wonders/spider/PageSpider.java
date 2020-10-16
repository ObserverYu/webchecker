package com.wonders.spider;

import com.ruiyun.jvppeteer.core.browser.Browser;
import com.wonders.spider.entity.ItemList;

import java.io.IOException;
import java.util.List;

/**
 * @author ObserverYu
 * @date 2020/10/16 10:00
 **/
public interface PageSpider {

    void start(List<ItemList> itemLists);

    Browser createBrowser(String browserPath) throws IOException;

    Browser getBrowser();

}
