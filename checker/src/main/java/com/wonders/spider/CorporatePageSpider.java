package com.wonders.spider;

import cn.hutool.core.util.StrUtil;
import com.ruiyun.jvppeteer.core.Puppeteer;
import com.ruiyun.jvppeteer.core.browser.Browser;
import com.ruiyun.jvppeteer.core.page.Dialog;
import com.ruiyun.jvppeteer.core.page.Page;
import com.ruiyun.jvppeteer.options.LaunchOptions;
import com.ruiyun.jvppeteer.options.LaunchOptionsBuilder;
import com.ruiyun.jvppeteer.options.Viewport;
import com.wonders.spider.handler.FailLoginBrowserHandler;
import com.wonders.spider.handler.SaveApplyUrlBrowserHandler;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * @author YuChen
 * @date 2020/10/12 17:10
 **/

@Data
public class CorporatePageSpider implements PageSpider {

    private Browser browser;

    private String mainLoginUrl;

    private String corporateLoginUrl;

    private String personLoginUrl;

    // 同步刷盘 异步暂不实现
    private boolean syncronized = true;

    private String password;

    private FailLoginBrowserHandler failLoginBrowserHandler;

    private SaveApplyUrlBrowserHandler saveApplyUrlBrowserHandler;

    public static CorporatePageSpider build() throws IOException {
        return build(null);
    }

    public static CorporatePageSpider build(String browserPath) throws IOException {
        CorporatePageSpider corporatePageSpider = new CorporatePageSpider();
        ArrayList<String> arrayList = new ArrayList<>();
        LaunchOptions options = new LaunchOptionsBuilder().withArgs(arrayList).withHeadless(false).build();
        arrayList.add("--no-sandbox");
        arrayList.add("--disable-setuid-sandbox");
        arrayList.add("--disable-popup-blocking");
        if(StrUtil.isNotBlank(browserPath)){
            options.setExecutablePath(browserPath);
        }
        Viewport viewport = new Viewport();
        viewport.setHeight(1080);
        viewport.setWidth(1920);
        options.setViewport(viewport);
        Browser browser = Puppeteer.launch(options);
        // 设置浏览器的监听器
        corporatePageSpider.setBrowser(browser);
        corporatePageSpider.setMainLoginUrl("https://zwdtuser.sh.gov.cn/uc/login/login.jsp");
        corporatePageSpider.setPersonLoginUrl("https://zwdtuser.sh.gov.cn/uc/login/login.jsp");
        corporatePageSpider.setCorporateLoginUrl("https://zwdtuser.sh.gov.cn:7443/tsoauth/login.jsp");
        setListenerForBrowser(corporatePageSpider);
        return corporatePageSpider;
    }

    private static void setListenerForBrowser(CorporatePageSpider corporatePageSpider) {
        Browser browser = corporatePageSpider.getBrowser();
        FailLoginBrowserHandler failLoginBrowserHandler = new FailLoginBrowserHandler(corporatePageSpider);
        corporatePageSpider.setFailLoginBrowserHandler(failLoginBrowserHandler);
        // 登录失败监听器
        browser.onTrgetcreated(failLoginBrowserHandler);
        // 立即办理打开的页面监听器
        SaveApplyUrlBrowserHandler saveApplyUrlBrowserHandler = new SaveApplyUrlBrowserHandler(corporatePageSpider);
        corporatePageSpider.setSaveApplyUrlBrowserHandler(saveApplyUrlBrowserHandler);
    }

    public Page handleOneItemPage(String url) throws ExecutionException, InterruptedException {
        Page page = browser.newPage();
        page.goTo(url);

        Thread.sleep(3000);
        return page;
    }

    /**
    * 法人登录
    *
    * @param
    * @return
    * @author YuChen
    * @date 2020/10/15 9:36
    */
    public Page loginCorporateByKey(String password, String url) throws ExecutionException, InterruptedException {
        Page page = browser.newPage();
        dismissPopup(page);
        page.goTo(url);
        Thread.sleep(2000);
        this.password = password;
        this.corporateLoginUrl = url;
        PageHandleUtil.handleLoginPage(url,page);
        return page;
    }

    /**
    * 忽略弹窗
    *
    * @param
    * @return
    * @author YuChen
    * @date 2020/10/15 9:38
    */
    private void dismissPopup(Page page){
        page.onDialg(Dialog::dismiss);
        page.onPopup(event -> {
            String message = event.getMessage();
            System.out.println(message);
        });
    }

}

