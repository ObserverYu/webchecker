package com.wonders.spider.handler;

import com.ruiyun.jvppeteer.core.page.Frame;
import com.ruiyun.jvppeteer.core.page.Page;
import com.ruiyun.jvppeteer.events.EventHandler;
import com.wonders.WebCheckerContext;
import com.wonders.spider.entity.ItemList;

import java.util.concurrent.CountDownLatch;

/**
 * 
 *  
 * @author ObserverYu
 * @date 2020/10/15 10:21
 **/
 
public class SaveApplyUrlPageHandler implements EventHandler<Frame> {

    private WebCheckerContext webCheckerContext;

    private CountDownLatch countDownLatch;

    private ItemList itemList;

    private Page page;

    private SaveApplyUrlBrowserHandler saveApplyUrlBrowserHandler;

    private ApplyPageHotkeyListener applyPageHotkeyListener;

    public SaveApplyUrlPageHandler(WebCheckerContext webCheckerContext,Page page
            ,SaveApplyUrlBrowserHandler saveApplyUrlBrowserHandler) {
        this.webCheckerContext = webCheckerContext;
        this.page = page;
        this.saveApplyUrlBrowserHandler = saveApplyUrlBrowserHandler;
    }

    @Override
    public void onEvent(Frame event) {
        String url = event.url();
        System.out.println("监听到页面url改变:"+url);
    }
}
