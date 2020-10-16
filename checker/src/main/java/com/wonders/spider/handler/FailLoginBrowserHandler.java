package com.wonders.spider.handler;

import com.ruiyun.jvppeteer.core.page.Page;
import com.ruiyun.jvppeteer.core.page.Target;
import com.ruiyun.jvppeteer.events.EventHandler;
import com.wonders.WebCheckerContext;
import lombok.Data;

/**
 * 
 *  
 * @author ObserverYu
 * @date 2020/10/15 10:21
 **/

@Data
public class FailLoginBrowserHandler implements EventHandler<Target> {

    private WebCheckerContext webCheckerContext;

    // 是否已经准备点击立即办理按钮
    private volatile boolean firstLogin = true;


    public FailLoginBrowserHandler(WebCheckerContext webCheckerContext) {
        this.webCheckerContext = webCheckerContext;
    }

    @Override
    public void onEvent(Target event) {
        if(!firstLogin){
            String url = event.url();
            System.out.println("检测到新的标签打开,初始url:"+url);
            Page page = event.page();
            // 页面的url变化监听器 解决登录失败问题
            FailLoginPageHandler failLoginPageHandler = new FailLoginPageHandler(webCheckerContext, page);
            page.onFramenavigated(failLoginPageHandler);
        }
    }
}
