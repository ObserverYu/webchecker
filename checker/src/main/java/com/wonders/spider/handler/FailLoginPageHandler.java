package com.wonders.spider.handler;

import cn.hutool.core.util.StrUtil;
import com.ruiyun.jvppeteer.core.page.Frame;
import com.ruiyun.jvppeteer.core.page.Page;
import com.ruiyun.jvppeteer.events.EventHandler;
import com.wonders.spider.CorporatePageSpider;
import com.wonders.spider.PageHandleUtil;

/**
 * 登录失败处理
 *  
 * @author ObserverYu
 * @date 2020/10/15 10:15
 **/
 
public class FailLoginPageHandler implements EventHandler<Frame> {

    private CorporatePageSpider corporatePageSpider;

    private Page page;

    public FailLoginPageHandler(CorporatePageSpider corporatePageSpider, Page page) {
        this.corporatePageSpider = corporatePageSpider;
        this.page = page;
    }

    @Override
    public void onEvent(Frame event) {
        String url = event.url();
        if(StrUtil.isBlank(url)){
            return;
        }
        if(!url.equals(PageHandleUtil.personLoginUrl) && !url.equals(PageHandleUtil.corporateLoginUrl)){
            return;
        }
        PageHandleUtil.handleLoginPage(url,page);
    }
}
