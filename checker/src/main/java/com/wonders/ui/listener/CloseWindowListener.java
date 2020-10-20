package com.wonders.ui.listener;

import com.ruiyun.jvppeteer.core.browser.Browser;
import com.wonders.WebCheckerContext;
import com.wonders.dao.service.DaoService;
import com.wonders.spider.PageSpider;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 
 *  
 * @author ObserverYu
 * @date 2020/10/16 10:56
 **/
 
public class CloseWindowListener extends WindowAdapter {

    public WebCheckerContext webCheckerContext;


    public CloseWindowListener(WebCheckerContext webCheckerContext) {
        this.webCheckerContext = webCheckerContext;
    }

    /**
     * Invoked when the user attempts to close the window
     * from the window's system menu.
     *
     * @param e
     */
    @Override
    public void windowClosing(WindowEvent e) {
        PageSpider pageSpider = webCheckerContext.getPageSpider();
        if(pageSpider != null){
            Browser browser = pageSpider.getBrowser();
            if(browser != null){
                browser.close();
            }
        }
        DaoService daoService = webCheckerContext.getDaoService();
        if(daoService != null){
            Thread worker = daoService.getWorker();
            if(worker != null){
                worker.interrupt();
            }
        }
        System.exit(0);
    }


}
