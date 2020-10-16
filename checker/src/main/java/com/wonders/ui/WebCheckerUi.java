package com.wonders.ui;

import com.wonders.WebCheckerContext;
import com.wonders.ui.listener.ChoseBrowserListener;
import com.wonders.ui.listener.CloseWindowListener;
import com.wonders.ui.listener.StartCheckListener;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author YuChen
 * @date 2020/10/12 9:43
 **/

@Data
public class WebCheckerUi {

    JFrame showFrame;

    JLabel statusLabel;

    JTextField urlInput;

    JTextField browserInput;

    JPanel inputJp;

    JPanel statusJp;

    JButton start;

    JButton choseBrowser;

    WebCheckerContext webCheckerContext;

    public static WebCheckerUi build(WebCheckerContext context){
        WebCheckerUi webCheckerUi = new WebCheckerUi();
        webCheckerUi.setWebCheckerContext(context);
        context.setUi(webCheckerUi);
        webCheckerUi.initUi();
        return webCheckerUi;
    }

    public void initUi() {
        // 创建窗体
        createFrame();
        // 浏览器路径输入行
        createBrowserPathInput();
        // 用户信息输入行 状态显示行
        createUserInput();
        // 关闭窗口
        CloseWindowListener closeWindowListener = new CloseWindowListener(getWebCheckerContext());
        showFrame.addWindowListener(closeWindowListener);
        //showFrame.pack();
        showFrame.setVisible(true);
        // 窗口总是在前
        showFrame.setAlwaysOnTop(true);
    }

    private void createUserInput() {
        Container contentPane = showFrame.getContentPane();
        // 状态显示行
        statusJp = new JPanel();
        urlInput = new JTextField("请输入委办局编号,多个以英文逗号隔开",40);
        urlInput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                urlInput.setText(null);
            }
        });
        statusJp.add(urlInput);
        start = new JButton("开始");
        StartCheckListener startCheckListener = new StartCheckListener(getWebCheckerContext());
        start.addActionListener(startCheckListener);
        statusLabel = new JLabel("未开始",JLabel.CENTER);
        statusJp.add(start);
        statusJp.add(statusLabel);
        contentPane.add(statusJp,BorderLayout.CENTER);
    }

    private void createBrowserPathInput() {
        // 浏览器路径输入行
        Container contentPane = showFrame.getContentPane();
        inputJp = new JPanel();
        browserInput = new JTextField("浏览器路径",40);
        //browserInput.setEnabled(false);
        choseBrowser = new JButton("浏览");
        choseBrowser.addActionListener(new ChoseBrowserListener(webCheckerContext));
        inputJp.add(browserInput);
        inputJp.add(choseBrowser);
        contentPane.add(inputJp,BorderLayout.NORTH);
    }

    private void createFrame() {
        // 创建窗体
        showFrame = new JFrame("搞快点");
        showFrame.setSize(530,150);
        Container contentPane = showFrame.getContentPane();
        contentPane.setLayout(new BorderLayout());
    }

    /**
    * 修改状态信息
    *
    * @param
    * @return
    * @author YuChen
    * @date 2020/10/16 14:18
    */
    public void changeStatusInfo(String info){
        statusLabel.setText(info);
    }

}
