package com.wonders;

import com.ruiyun.jvppeteer.core.page.Page;
import com.wonders.dao.DaoService;
import com.wonders.spider.PageSpider;
import com.wonders.spider.handler.ApplyPageHotkeyListener;
import com.wonders.ui.WebCheckerUi;
import lombok.Data;

/**
 * @author YuChen
 * @date 2020/10/12 9:43
 **/

@Data
public class WebCheckerContext {

    private WebCheckerUi ui;

    private PageSpider pageSpider;

    private DaoService daoService;

    // 当前正打开的立即处理页面
    private Page nowApplyPage;

    // 当前正打开的办事指南页面
    private Page nowInfoPage;

    private ApplyPageHotkeyListener applyPageHotkeyListener;

}
