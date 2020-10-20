package com.wonders.spider;

import com.melloware.jintellitype.JIntellitype;
import com.ruiyun.jvppeteer.core.Puppeteer;
import com.ruiyun.jvppeteer.core.browser.Browser;
import com.ruiyun.jvppeteer.core.page.Page;
import com.ruiyun.jvppeteer.options.LaunchOptions;
import com.ruiyun.jvppeteer.options.LaunchOptionsBuilder;
import com.ruiyun.jvppeteer.options.PageNavigateOptions;
import com.ruiyun.jvppeteer.options.Viewport;
import com.wonders.dao.service.ItemListService;
import com.wonders.dao.entity.ItemList;
import com.wonders.hotkey.ItemPageHotKeyListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 自动打开立即办理的页面
 *
 * @author ObserverYu
 * @date 2020/10/15 10:28
 **/

public class OpenApplyItemPage {

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
        //Scanner scanner = new Scanner(System.in);
        ItemPageHotKeyListener applyPageHotkeyListener = listenKey();
        for (ItemList itemList : itemListService.getList()) {
            Page page = browser.newPage();
            System.out.println("打开:"+itemList.toString());
            page.goTo(itemList.getUrl(), pageNavigateOptions);
            CountDownLatch countDownLatch = new CountDownLatch(1);
            applyPageHotkeyListener.setItemList(itemList);
            applyPageHotkeyListener.setCountDownLatch(countDownLatch);
            countDownLatch.await();
            updateList.put(itemList);
            page.close();
        }
        browser.close();
    }

    private static ItemPageHotKeyListener listenKey() {
        JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_CONTROL, (int) '1');//crtl+1为快捷键
        JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_CONTROL, (int) '2');//crtl+2为快捷键
        //添加监听
        ItemPageHotKeyListener applyPageHotkeyListener = new ItemPageHotKeyListener();
        JIntellitype.getInstance().addHotKeyListener(applyPageHotkeyListener);
        return applyPageHotkeyListener;
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
