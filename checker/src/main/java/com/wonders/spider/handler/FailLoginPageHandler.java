package com.wonders.spider.handler;

import cn.hutool.core.util.StrUtil;
import com.ruiyun.jvppeteer.core.page.Frame;
import com.ruiyun.jvppeteer.core.page.Page;
import com.ruiyun.jvppeteer.events.EventHandler;
import com.wonders.WebCheckerContext;
import com.wonders.spider.CorporatePageSpider;
import com.wonders.util.PageHandleUtil;

/**
 * 登录失败处理
 *  
 * @author ObserverYu
 * @date 2020/10/15 10:15
 **/
 
public class FailLoginPageHandler implements EventHandler<Frame> {

    private CorporatePageSpider corporatePageSpider;

    private Page page;

    private WebCheckerContext webCheckerContext;

    public FailLoginPageHandler(CorporatePageSpider corporatePageSpider, Page page) {
        this.corporatePageSpider = corporatePageSpider;
        this.page = page;
    }

    public FailLoginPageHandler(WebCheckerContext webCheckerContext,Page page) {
        this.page = page;
        this.webCheckerContext = webCheckerContext;
    }

    @Override
    public void onEvent(Frame event) {
        String url = event.url();
        if(StrUtil.isBlank(url)){
            return;
        }
        if(!url.startsWith(PageHandleUtil.personLoginUrl) && !url.startsWith(PageHandleUtil.corporateLoginUrl)){
            return;
        }
        webCheckerContext.getUi().changeStatusInfo("检测到登录页面,开始自动登录流程");
        PageHandleUtil.handleLoginPage(url,page);
        webCheckerContext.getUi().changeStatusInfo("立即办理页面已经打开,请判断页面是否异常,按快捷键确认");
    }
}
