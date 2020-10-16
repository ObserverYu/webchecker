package com.wonders.util;

import cn.hutool.core.collection.CollectionUtil;
import com.ruiyun.jvppeteer.core.page.ElementHandle;
import com.ruiyun.jvppeteer.core.page.Page;
import com.ruiyun.jvppeteer.options.PageNavigateOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *  
 * @author ObserverYu
 * @date 2020/10/15 17:43
 **/
 
public class PageHandleUtil {

    public static String  personLoginUrl = "https://zwdtuser.sh.gov.cn/uc/login/login.jsp";

    public static String corporateLoginUrl = "https://zwdtuser.sh.gov.cn:7443/tsoauth/login.jsp";

    /**
    * 查找指南页面的立即办理按钮
    *
    * @param
    * @return
    * @author YuChen
    * @date 2020/10/15 17:49
    */
    public static ElementHandle selectApplyBt(Page page) {
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

    /**
    * 处理登录页面
    *
    * @param
    * @return
    * @author YuChen
    * @date 2020/10/15 17:48
    */
    public static void handleLoginPage(String url,Page page) {
        if(!url.startsWith(personLoginUrl) && !url.startsWith(corporateLoginUrl)){
            return;
        }
        PageNavigateOptions pageNavigateOptions = new PageNavigateOptions();
        List<String> wait = new ArrayList<>();
        wait.add("domcontentloaded");
        pageNavigateOptions.setWaitUntil(wait);
        try{
            if(personLoginUrl.startsWith(url)){
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
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
