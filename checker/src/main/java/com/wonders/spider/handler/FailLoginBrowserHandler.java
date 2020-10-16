package com.wonders.spider.handler;

import com.ruiyun.jvppeteer.core.page.Page;
import com.ruiyun.jvppeteer.core.page.Target;
import com.ruiyun.jvppeteer.events.EventHandler;
import com.wonders.spider.CorporatePageSpider;

/**
 * 
 *  
 * @author ObserverYu
 * @date 2020/10/15 10:21
 **/
 
public class FailLoginBrowserHandler implements EventHandler<Target> {

    private CorporatePageSpider corporatePageSpider;


    public FailLoginBrowserHandler(CorporatePageSpider corporatePageSpider) {
        this.corporatePageSpider = corporatePageSpider;
    }

    @Override
    public void onEvent(Target event) {
        String url = event.url();
        System.out.println("检测到新的标签打开,初始url:"+url);
        Page page = event.page();
        FailLoginPageHandler failLoginPageHandler = new FailLoginPageHandler(corporatePageSpider, page);
        // 页面的url变化监听器 解决登录失败问题
        page.onFramenavigated(failLoginPageHandler);
    }
}
