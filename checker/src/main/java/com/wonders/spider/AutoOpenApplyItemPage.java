package com.wonders.spider;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.ruiyun.jvppeteer.core.Puppeteer;
import com.ruiyun.jvppeteer.core.browser.Browser;
import com.ruiyun.jvppeteer.core.page.ElementHandle;
import com.ruiyun.jvppeteer.core.page.Page;
import com.ruiyun.jvppeteer.events.Events;
import com.ruiyun.jvppeteer.options.LaunchOptions;
import com.ruiyun.jvppeteer.options.LaunchOptionsBuilder;
import com.ruiyun.jvppeteer.options.PageNavigateOptions;
import com.ruiyun.jvppeteer.options.Viewport;
import com.wonders.dao.ItemListService;
import com.wonders.spider.entity.ItemList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 查找有立即办理的页面
 *
 * @author ObserverYu
 * @date 2020/10/15 10:28
 **/

public class AutoOpenApplyItemPage {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        List<String> weiban = new ArrayList<>();
        weiban.add("SHFGSH");
        weiban.add("SHGDSH");
        ItemListService itemListService = ItemListService.build(weiban, 1, "未检查");
        System.out.println(itemListService.getList());
        Browser browser = buidBrowser();
        LinkedBlockingDeque<ItemList> updateList = itemListService.getUpdateList();
        // 等待策略
        PageNavigateOptions pageNavigateOptions = new PageNavigateOptions();
        List<String> wait = new ArrayList<>();
        wait.add("domcontentloaded");
        pageNavigateOptions.setWaitUntil(wait);
        pageNavigateOptions.setTimeout(0);

        Page page = browser.newPage();

        browser.onTrgetcreated(handler -> {

            Page page1 = handler.page();
            if("https://zwdtuser.sh.gov.cn/uc/login/login.jsp".equals(handler.url())){
                handleLoginPage(handler.url(),page1);
            }
            page1.onFramenavigated(handler1 ->{
                String url = handler1.url();
                if("https://zwdtuser.sh.gov.cn/uc/login/login.jsp".equals(url)){
                    handleLoginPage(url,page1);
                }
            });
        });
        // 有立即前往
        //page.goTo("http://zwdt.sh.gov.cn/govPortals/bsfw/item/258ca423-9a15-4527-87e1-af20ba72894c");
        // 没有立即前往
        page.goTo("http://zwdt.sh.gov.cn/govPortals/bsfw/item/16cb0bc8-7803-4fb9-983f-0b0ee7f001e2");
        ElementHandle apply = selectApplyBt(page);
        String content = page.content();
        boolean gotoDealWith = content.contains("gotoDealWith");
        boolean showGjModel = content.contains("showGjModel");
        System.out.println("gotoDealWith:"+gotoDealWith);
        System.out.println("showGjModel:"+showGjModel);
        ElementHandle ljqwBt = page.$("#bwptBtn");
        boolean intersectingViewport = ljqwBt.isIntersectingViewport();
        System.out.println(intersectingViewport);
    }

    private static void handleLoginPage(String url,Page page) {
        PageNavigateOptions pageNavigateOptions = new PageNavigateOptions();
        List<String> wait = new ArrayList<>();
        wait.add("domcontentloaded");
        pageNavigateOptions.setWaitUntil(wait);
        try{
            page.onPopup(popup -> {
                String message = popup.getMessage();
                System.out.println("弹窗事件popup:"+message);
            });
            page.onDialg(dialg ->{
                System.out.println("弹窗事件dialg:");
                dialg.dismiss();
            });
            page.on(Events.PAGE_DOMContentLoaded.getName(), (loaded)->{
                System.out.println("页面加载完毕");
            });
            if("https://zwdtuser.sh.gov.cn/uc/login/login.jsp".equals(url)){
                // 点击法人登录
                ElementHandle header = page.waitForSelector("#login-hearder");
                ElementHandle a = header.$(".corporate");
                a.click();
                page.waitForNavigation(pageNavigateOptions);
            }
            Thread.sleep(3000);
            // 关闭弹窗
            ElementHandle btn = page.waitForSelector(".btn-confirm");
            boolean intersectingViewport = btn != null && btn.isIntersectingViewport();
            System.out.println("检测到弹窗:"+intersectingViewport);

            // 关闭key提示弹窗
            if (intersectingViewport) {
                btn.click();
            }
            Thread.sleep(2000);
            ElementHandle byKey = page.waitForSelector("#but-tab");
            byKey.click();
            // 输入密码
            ElementHandle input = page.waitForSelector("#login_showPwd");
            input.type("12345678");
            // 点击登录
            ElementHandle login = page.waitForSelector("#loginbtn");
            login.click();
            page.waitForNavigation(pageNavigateOptions);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static ElementHandle selectApplyBt(Page page) {
        ElementHandle apply1 = page.$(".red-bg");
        List<ElementHandle> allOperation = page.$$(".operation");
        // 查找立即办理按钮
        if(CollectionUtil.isNotEmpty(allOperation)){
            for(ElementHandle oneDiv:allOperation){
                ElementHandle button = oneDiv.$("button");
                if(button != null && button.isIntersectingViewport()){
                    return button;
                }
            }
        }
        return null;
    }


    private static Browser buidBrowser(String browserPath) throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        LaunchOptions options = new LaunchOptionsBuilder().withArgs(arrayList).withHeadless(false).build();
        if(StrUtil.isNotBlank(browserPath)){
            options.setExecutablePath(browserPath);
        }
        arrayList.add("--no-sandbox");
        arrayList.add("--disable-setuid-sandbox");
        arrayList.add("--disable-popup-blocking");
        Viewport viewport = new Viewport();
        viewport.setHeight(1080);
        viewport.setWidth(1920);
        options.setViewport(viewport);
        return Puppeteer.launch(options);
    }

    private static Browser buidBrowser() throws IOException {
        return buidBrowser(null);
    }

}
