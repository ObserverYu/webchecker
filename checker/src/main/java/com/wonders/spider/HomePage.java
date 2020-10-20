package com.wonders.spider;

import cn.hutool.core.collection.CollectionUtil;
import com.ruiyun.jvppeteer.core.Puppeteer;
import com.ruiyun.jvppeteer.core.browser.Browser;
import com.ruiyun.jvppeteer.core.page.ElementHandle;
import com.ruiyun.jvppeteer.core.page.Page;
import com.ruiyun.jvppeteer.options.LaunchOptions;
import com.ruiyun.jvppeteer.options.LaunchOptionsBuilder;
import com.ruiyun.jvppeteer.options.PageNavigateOptions;
import com.ruiyun.jvppeteer.options.Viewport;
import com.wonders.dao.service.ItemListService;
import com.wonders.dao.entity.ItemList;

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

public class HomePage {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        List<String> weiban = new ArrayList<>();
        weiban.add("SHFGSH");
        weiban.add("SHGDSH");
        ItemListService itemListService = ItemListService.build(weiban, 1, "未检查");
        System.out.println(itemListService.getList());
        Browser browser = buidBrowser();
        LinkedBlockingDeque<ItemList> updateList = itemListService.getUpdateList();
        PageNavigateOptions pageNavigateOptions = new PageNavigateOptions();
        pageNavigateOptions.setTimeout(0);

        Page page = browser.newPage();
        page.goTo("https://zwdtuser.sh.gov.cn/uc/login/login.jsp");
        // 点击法人登录
        ElementHandle header = page.$("#login-hearder");
        ElementHandle a = header.$(".corporate");
        a.click();
        Thread.sleep(5000);
        ElementHandle btn = page.waitForSelector(".btn-confirm");
        boolean intersectingViewport = btn != null && btn.isIntersectingViewport();
        // 关闭key提示弹窗
        if (intersectingViewport) {
            btn.click();
        }
        Thread.sleep(5000);
        // 点击法人一证通登录
        page.click("#but-tab");
        Thread.sleep(5000);
        // 输入密码
        ElementHandle t = page.$("#login_showPwd");
        t.type("12345678");
        Thread.sleep(5000);
        // 点击登录
        page.click("#loginbtn");
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


    private static Browser buidBrowser() throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        LaunchOptions options = new LaunchOptionsBuilder().withArgs(arrayList).withHeadless(false).build();
        arrayList.add("--no-sandbox");
        arrayList.add("--disable-setuid-sandbox");
        arrayList.add("--disable-popup-blocking");
        Viewport viewport = new Viewport();
        viewport.setHeight(1080);
        viewport.setWidth(1920);
        options.setViewport(viewport);
        return Puppeteer.launch(options);
    }



}
