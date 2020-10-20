package com.wonders.spider;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.melloware.jintellitype.JIntellitype;
import com.ruiyun.jvppeteer.core.Puppeteer;
import com.ruiyun.jvppeteer.core.browser.Browser;
import com.ruiyun.jvppeteer.core.page.Dialog;
import com.ruiyun.jvppeteer.core.page.ElementHandle;
import com.ruiyun.jvppeteer.core.page.Page;
import com.ruiyun.jvppeteer.options.LaunchOptions;
import com.ruiyun.jvppeteer.options.LaunchOptionsBuilder;
import com.ruiyun.jvppeteer.options.PageNavigateOptions;
import com.ruiyun.jvppeteer.options.Viewport;
import com.wonders.WebCheckerContext;
import com.wonders.dao.entity.ItemList;
import com.wonders.dao.service.DaoService;
import com.wonders.hotkey.ItemPageHotKeyListener;
import com.wonders.spider.handler.FailLoginBrowserHandler;
import com.wonders.spider.handler.SaveApplyUrlBrowserHandler;
import com.wonders.ui.WebCheckerUi;
import com.wonders.ui.webinnerevent.ChangeInfoInnerEvent;
import com.wonders.util.PageHandleUtil;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author YuChen
 * @date 2020/10/12 17:10
 **/

@Data
public class CorporatePageSpider implements PageSpider<ItemList> {

    WebCheckerContext webCheckerContext;

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
        return build(null, null);
    }

    public static CorporatePageSpider build(String browserPath, WebCheckerContext webCheckerContext) throws IOException {
        CorporatePageSpider corporatePageSpider = new CorporatePageSpider();
        corporatePageSpider.setWebCheckerContext(webCheckerContext);
        corporatePageSpider.createBrowser(browserPath);
        // 设置浏览器的监听器
        corporatePageSpider.setMainLoginUrl("https://zwdtuser.sh.gov.cn/uc/login/login.jsp");
        corporatePageSpider.setPersonLoginUrl("https://zwdtuser.sh.gov.cn/uc/login/login.jsp");
        corporatePageSpider.setCorporateLoginUrl("https://zwdtuser.sh.gov.cn:7443/tsoauth/login.jsp");
        return corporatePageSpider;
    }

    @Override
    public Browser createBrowser(String browserPath) throws IOException{
        ArrayList<String> arrayList = new ArrayList<>();
        LaunchOptions options = new LaunchOptionsBuilder().withArgs(arrayList).withHeadless(false).build();
        arrayList.add("--no-sandbox");
        arrayList.add("--disable-setuid-sandbox");
        arrayList.add("--disable-popup-blocking");
        if (StrUtil.isNotBlank(browserPath)) {
            options.setExecutablePath(browserPath);
        }
        Viewport viewport = new Viewport();
        viewport.setHeight(1080);
        viewport.setWidth(1920);
        options.setViewport(viewport);
        Browser browser = Puppeteer.launch(options);
        initBrowserListener(browser);
        this.browser = browser;
        return browser;
    }

    private void initBrowserListener(Browser browser) {
        FailLoginBrowserHandler failLoginBrowserHandler = new FailLoginBrowserHandler(webCheckerContext);
        this.setFailLoginBrowserHandler(failLoginBrowserHandler);
        // 登录失败监听器
        browser.onTrgetcreated(failLoginBrowserHandler);
        // 立即办理打开的页面监听器
        SaveApplyUrlBrowserHandler saveApplyUrlBrowserHandler = new SaveApplyUrlBrowserHandler(webCheckerContext);
        this.saveApplyUrlBrowserHandler = saveApplyUrlBrowserHandler;
        browser.onTrgetcreated(saveApplyUrlBrowserHandler);
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
        PageHandleUtil.handleLoginPage(url, page);
        return page;
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ArrayList<String> arrayList = new ArrayList<>();
        LaunchOptions options = new LaunchOptionsBuilder().withArgs(arrayList).withHeadless(false).build();
        arrayList.add("--no-sandbox");
        arrayList.add("--disable-setuid-sandbox");
        arrayList.add("--disable-popup-blocking");
        Viewport viewport = new Viewport();
        viewport.setHeight(1080);
        viewport.setWidth(1920);
        options.setViewport(viewport);
        Browser browser = Puppeteer.launch(options);
        Thread.sleep(Long.parseLong("5000"));
        browser.newPage();
        System.out.println(browser.isConnected());
    }

    /**
     * 忽略弹窗
     *
     * @param
     * @return
     * @author YuChen
     * @date 2020/10/15 9:38
     */
    private void dismissPopup(Page page) {
        page.onDialg(Dialog::dismiss);
        page.onPopup(event -> {
            String message = event.getMessage();
            System.out.println(message);
        });
    }

    @Override
    public void start(List<ItemList> list) {
        WebCheckerUi ui = this.webCheckerContext.getUi();
        try {
            ui.postEvent(new ChangeInfoInnerEvent("正在进行首次登录"));
            // 首次登录
            Page login = browser.newPage();
            login.goTo("https://zwdtuser.sh.gov.cn:7443/tsoauth/login.jsp");
            PageHandleUtil.handleLoginPage("https://zwdtuser.sh.gov.cn:7443/tsoauth/login.jsp",login);
            Thread.sleep(3000);
            this.failLoginBrowserHandler.setFirstLogin(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DaoService<ItemList> itemListService = this.webCheckerContext.getDaoService();
        LinkedBlockingDeque<ItemList> updateList = itemListService.getUpdateList();
        PageNavigateOptions pageNavigateOptions = new PageNavigateOptions();
        List<String> wait = new ArrayList<>();
        wait.add("domcontentloaded");
        pageNavigateOptions.setWaitUntil(wait);
        pageNavigateOptions.setTimeout(0);
        // 设置快捷键
        ItemPageHotKeyListener applyPageHotkeyListener = listenKey(webCheckerContext);
        webCheckerContext.setHotkeyListener(applyPageHotkeyListener);
        for (ItemList itemList : itemListService.getList()) {
            try {
                Page page = browser.newPage();
                ui.postEvent(new ChangeInfoInnerEvent("正在扫描事项:" + itemList.getItemCode() + " " + itemList.getItemName()));
                page.goTo(itemList.getUrl(), pageNavigateOptions);
                webCheckerContext.setNowInfoPage(page);
                ui.postEvent(new ChangeInfoInnerEvent("正在检查是否有立即办理"));
                // 检查是否有立即办理按钮
                ElementHandle applyBtn = checkApplyButton(page, itemList);
                CountDownLatch countDownLatch = new CountDownLatch(1);
                if(applyBtn != null){
                    ui.postEvent(new ChangeInfoInnerEvent("有立即办理,打开"));
                    itemList.setApply(1);
                    // 点击立即办理按钮
                    clickApply(page,applyBtn);
                }else {
                    itemList.setApply(0);
                }
                listenUserKey(applyPageHotkeyListener,countDownLatch,itemList);
                countDownLatch.await();
                updateList.put(itemList);
                webCheckerContext.getNowApplyPage().close();
                webCheckerContext.getNowInfoPage().close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("打开页面时被打断");
            }

        }
        browser.close();
    }

    private void clickApply(Page page,ElementHandle apply) throws ExecutionException, InterruptedException {
        if(apply != null){
            WebCheckerUi ui = webCheckerContext.getUi();
            String content = page.content();
            // showGjModel -> 前往其他页面  gotoDealWith -> 直接跳转
            boolean showGjModel = content.contains("showGjModel");
            // 准备好了打开立即办理页面  让监听器开始工作
            SaveApplyUrlBrowserHandler saveApplyUrlBrowserHandler = this.getSaveApplyUrlBrowserHandler();
            saveApplyUrlBrowserHandler.setPrepareClickApplyBtn(true);
            ui.postEvent(new ChangeInfoInnerEvent("点击立即办理"));
            apply.click();
            if(showGjModel){
                ui.postEvent(new ChangeInfoInnerEvent("存在新弹窗,点击立即前往"));
                ElementHandle ljqwBt = page.waitForSelector("#bwptBtn");
                boolean intersectingViewport = ljqwBt.isIntersectingViewport();
                int tryTimes = 5;
                while (!intersectingViewport && tryTimes > 0){
                    Thread.sleep(200);
                    tryTimes --;
                }
                ljqwBt.click();
            }
        }
    }

    /**
    * 监听快捷键
    *
    * @param
    * @return
    * @author YuChen
    * @date 2020/10/16 15:23
    */
    private void listenUserKey(ItemPageHotKeyListener applyPageHotkeyListener , CountDownLatch countDownLatch, ItemList itemList) {
        applyPageHotkeyListener.setItemList(itemList);
        applyPageHotkeyListener.setCountDownLatch(countDownLatch);
    }

    private ElementHandle checkApplyButton(Page page,ItemList itemList) {
        itemList.setApply(0);
        List<ElementHandle> allOperation = page.$$(".operation");
        // 查找立即办理按钮
        if(CollectionUtil.isNotEmpty(allOperation)){
            for(ElementHandle oneDiv:allOperation){
                ElementHandle button = oneDiv.$("button");
                if(button != null && button.isIntersectingViewport()){
                    itemList.setApply(1);
                    return button;
                }
            }
        }
        return null;
    }

    private static ItemPageHotKeyListener listenKey(WebCheckerContext webCheckerContext) {
        JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_CONTROL, (int) '1');//crtl+1为快捷键
        JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_CONTROL, (int) '2');//crtl+2为快捷键
        //添加监听
        ItemPageHotKeyListener applyPageHotkeyListener = new ItemPageHotKeyListener();
        JIntellitype.getInstance().addHotKeyListener(applyPageHotkeyListener);
        applyPageHotkeyListener.setWebCheckerContext(webCheckerContext);
        return applyPageHotkeyListener;
    }
}

