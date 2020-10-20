package com.wonders.spider;

import com.ruiyun.jvppeteer.core.browser.Browser;

import java.io.IOException;
import java.util.List;

/**
 * @author ObserverYu
 * @date 2020/10/16 10:00
 **/
public interface PageSpider<T> {

    void start(List<T> itemLists);

    Browser createBrowser(String browserPath) throws IOException;

    Browser getBrowser();

}
