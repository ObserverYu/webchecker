package com.wonders.ui.listener;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.wonders.WebCheckerContext;
import com.wonders.dao.service.DaoService;
import com.wonders.dao.service.ItemListService;
import com.wonders.ex.StartException;
import com.wonders.spider.CorporatePageSpider;
import com.wonders.spider.PageSpider;
import com.wonders.ui.WebCheckerUiImp;
import com.wonders.ui.webinnerevent.ChangeInfoInnerEvent;
import com.wonders.util.UiUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

/**
 * 开始扫描
 *  
 * @author YuChen
 * @date 2020/10/12 14:09
 **/
 
public class StartCheckListener implements ActionListener {

    WebCheckerContext webCheckerContext;

    WebCheckerUiImp ui;

    public StartCheckListener(WebCheckerContext webCheckerContext, WebCheckerUiImp ui) {
        this.webCheckerContext = webCheckerContext;
        this.ui = ui;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.showConfirmDialog(
                webCheckerContext.getUi().getJframe(),
                "为保证黑盒测试的效果, 立即办理页面是否合法必须由人工判断, 快捷键:ctrl+1 正常, ctrl+2 异常",
                "提示",
                JOptionPane.OK_CANCEL_OPTION
        );

        if(JOptionPane.OK_OPTION == result){
            try{
                checkWeibanAndInitDao();
                webCheckerContext.getUi().postEvent(new ChangeInfoInnerEvent("事项数据初始化成功"));
                checkAndInitBrowser();
                webCheckerContext.getUi().postEvent(new ChangeInfoInnerEvent("浏览器初始化成功"));
                Thread workThread = new Thread(()->{
                    try {
                        work();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                });
                workThread.setDaemon(true);
                workThread.start();
            }catch (Exception ex){
                ex.printStackTrace();
                System.out.println("任务初始化失败");
            }
        }
    }

    private void work() throws InterruptedException {
        webCheckerContext.getUi().postEvent(new ChangeInfoInnerEvent("5秒后开始扫描任务,当前配置\n" +
                "刷盘方式:同步\n" +
                "数据储存:mysql;\n" +
                "自动登录:开启"));
        Thread.sleep(1000);
        webCheckerContext.getUi().postEvent(new ChangeInfoInnerEvent("4秒后开始扫描任务,当前配置\n" +
                "刷盘方式:同步\n" +
                "数据储存:mysql;\n" +
                "自动登录:开启"));
        Thread.sleep(1000);
        webCheckerContext.getUi().postEvent(new ChangeInfoInnerEvent("3秒后开始扫描任务,当前配置\n" +
                "刷盘方式:同步\n" +
                "数据储存:mysql;\n" +
                "自动登录:开启"));
        Thread.sleep(1000);
        webCheckerContext.getUi().postEvent(new ChangeInfoInnerEvent("2秒后开始扫描任务,当前配置\n" +
                "刷盘方式:同步\n" +
                "数据储存:mysql;\n" +
                "自动登录:开启"));
        Thread.sleep(1000);
        webCheckerContext.getUi().postEvent(new ChangeInfoInnerEvent("1秒后开始扫描任务,当前配置\n" +
                "刷盘方式:同步\n" +
                "数据储存:mysql;\n" +
                "自动登录:开启"));
        Thread.sleep(1000);
        PageSpider pageSpider = webCheckerContext.getPageSpider();
        pageSpider.start(webCheckerContext.getDaoService().getList());
        // 开始刷盘
        webCheckerContext.getDaoService().startUpdate();
    }

    private void checkWeibanAndInitDao() {
        JTextField urlInput = ui.getUrlInput();
        String weiban = urlInput.getText();
        if(StrUtil.isBlank(weiban)){
            UiUtil.showInfoMessage(ui.getShowFrame(),"请输入委办局编号,多个以英文逗号隔开");
            throw new StartException("委办局编号为空");
        }
        List<String> weibanList = Arrays.asList(weiban.split(","));
        // todo 暂时默认用数据库作为数据源
        DaoService daoService = webCheckerContext.getDaoService();
        if(webCheckerContext.getDaoService() == null){
            daoService = ItemListService.build(weibanList, null, null, webCheckerContext);
        }else {
            daoService.updateOriginal(weibanList,null,null);
        }
        webCheckerContext.setDaoService(daoService);
        if(CollectionUtil.isEmpty(daoService.getList())){
            UiUtil.showWarningMessage(ui.getShowFrame(),"无法查询到事项列表,请检查委办局编号输入是否正确");
            throw new StartException("无法查询到事项列表");
        }
    }

    private void checkAndInitBrowser() {
        JTextField browserInput = ui.getBrowserInput();
        String browserPath = browserInput.getText();
        if(StrUtil.isBlank(browserPath) || "浏览器路径".equals(browserPath)){
            UiUtil.showInfoMessage(ui.getShowFrame(),"请选择chrome内核浏览器");
            throw new StartException("未选择浏览器");
        }
        try {
            PageSpider pageSpider = webCheckerContext.getPageSpider();
            if(pageSpider == null){
                pageSpider = CorporatePageSpider.build(browserPath,webCheckerContext);
            }else {
                pageSpider.createBrowser(browserPath);
            }
            webCheckerContext.setPageSpider(pageSpider);
        }catch (Exception e){
            e.printStackTrace();
            UiUtil.showErrorMessage(ui.getShowFrame(),"浏览器初始化失败,请选择chrome内核浏览器");
            throw new StartException("浏览器初始化失败");
        }
    }
}
