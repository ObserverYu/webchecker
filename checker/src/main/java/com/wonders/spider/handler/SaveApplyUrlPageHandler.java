package com.wonders.spider.handler;

import com.ruiyun.jvppeteer.core.page.Frame;
import com.ruiyun.jvppeteer.events.EventHandler;
import com.wonders.spider.CorporatePageSpider;

/**
 * 
 *  
 * @author ObserverYu
 * @date 2020/10/15 10:21
 **/
 
public class SaveApplyUrlPageHandler implements EventHandler<Frame> {

    private CorporatePageSpider corporatePageSpider;

    public SaveApplyUrlPageHandler(CorporatePageSpider corporatePageSpider) {
        this.corporatePageSpider = corporatePageSpider;
    }


    @Override
    public void onEvent(Frame event) {

    }
}
