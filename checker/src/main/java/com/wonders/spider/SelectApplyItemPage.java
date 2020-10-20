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
 
public class SelectApplyItemPage {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        List<String> weiban = new ArrayList<>();
        weiban.add("SHFGSH");
        weiban.add("SHGDSH");
        ItemListService itemListService = ItemListService.build(weiban);
        System.out.println(itemListService.getList());

        Browser browser = buidBrowser();
        LinkedBlockingDeque<ItemList> updateList = itemListService.getUpdateList();
        PageNavigateOptions pageNavigateOptions = new PageNavigateOptions();
        pageNavigateOptions.setTimeout(0);
        for(ItemList itemList : itemListService.getList()){
            Page page = browser.newPage();
            page.goTo(itemList.getUrl(),pageNavigateOptions);
            List<ElementHandle> allOperation = page.$$(".operation");
            itemList.setApply(0);
            // 查找立即办理按钮
            if(CollectionUtil.isNotEmpty(allOperation)){
                for(ElementHandle oneDiv:allOperation){
                    ElementHandle button = oneDiv.$("button");
                    if(button != null && button.isIntersectingViewport()){
                        itemList.setApply(1);
                    }
                }
            }
            updateList.put(itemList);
            page.close();
        }
        browser.close();
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
