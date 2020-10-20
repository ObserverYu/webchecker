package com.wonders.spider.handler;

import com.ruiyun.jvppeteer.core.page.Page;
import com.ruiyun.jvppeteer.core.page.Target;
import com.ruiyun.jvppeteer.events.EventHandler;
import com.wonders.WebCheckerContext;
import com.wonders.hotkey.ItemPageHotKeyListener;
import com.wonders.spider.CorporatePageSpider;
import com.wonders.ui.webinnerevent.ChangeInfoInnerEvent;
import lombok.Data;

/**
 * 
 *  
 * @author ObserverYu
 * @date 2020/10/15 10:21
 **/

@Data
public class SaveApplyUrlBrowserHandler implements EventHandler<Target> {

    private CorporatePageSpider corporatePageSpider;

    private WebCheckerContext webCheckerContext;
    
    // 是否已经准备点击立即办理按钮
    private volatile boolean prepareClickApplyBtn = false;

    private ItemPageHotKeyListener applyPageHotkeyListener;


    public SaveApplyUrlBrowserHandler(CorporatePageSpider corporatePageSpider) {
        this.corporatePageSpider = corporatePageSpider;
    }

    public SaveApplyUrlBrowserHandler(WebCheckerContext webCheckerContext) {
        this.webCheckerContext = webCheckerContext;
    }

    @Override
    public void onEvent(Target event) {
        if(prepareClickApplyBtn){
            webCheckerContext.getUi().postEvent(new ChangeInfoInnerEvent("立即办理页面已经打开,请判断页面是否异常,按快捷键确认"));
            String url = event.url();
            System.out.println("检测到新的立即办理标签打开,初始url:"+url);
            Page page = event.page();
            SaveApplyUrlPageHandler saveApplyUrlPageHandler
                    = new SaveApplyUrlPageHandler(webCheckerContext,page,this);
            // 页面的url变化监听器 解决登录失败问题
            page.onFramenavigated(saveApplyUrlPageHandler);
            webCheckerContext.setNowApplyPage(page);
        }
    }
}
