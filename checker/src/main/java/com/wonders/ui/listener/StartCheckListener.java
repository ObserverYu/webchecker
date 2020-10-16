package com.wonders.ui.listener;


import cn.hutool.core.util.StrUtil;
import com.wonders.spider.CorporatePageSpider;
import com.wonders.ui.WebCheckerRunner;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 开始扫描
 *  
 * @author YuChen
 * @date 2020/10/12 14:09
 **/
 
public class StartCheckListener implements ActionListener {
    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("程序开始");
        WebCheckerRunner context = WebCheckerRunner.context;
        checkAndInitBrowser(context);
    }

    private void checkAndInitBrowser(WebCheckerRunner context) {
        JTextField browserInput = context.getBrowserInput();
        String browserPath = browserInput.getText();
        if(StrUtil.isBlank(browserPath) || "浏览器路径".equals(browserPath)){
            JOptionPane.showMessageDialog(
                    context.getShowFrame(),
                    "请选择chrome内核浏览器",
                    "提示",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
        try {
            CorporatePageSpider build = CorporatePageSpider.build(browserPath);
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    context.getShowFrame(),
                    "浏览器初始化失败,请选择chrome内核浏览器",
                    "提示",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
