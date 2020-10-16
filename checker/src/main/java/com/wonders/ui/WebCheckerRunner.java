package com.wonders.ui;

import com.wonders.ui.listener.ChoseBrowserListener;
import com.wonders.ui.listener.ConfirmWebEventHandler;
import com.wonders.ui.listener.StartCheckListener;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author YuChen
 * @date 2020/10/12 9:43
 **/

@Data
public class WebCheckerRunner {

    public static WebCheckerRunner context;

    public static KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    public static ConfirmWebEventHandler confirmWebEventHandler = new ConfirmWebEventHandler();


    JFrame showFrame;

    JLabel statusLabel;

    JTextField urlInput;

    JTextField browserInput;

    JPanel inputJp;

    JPanel statusJp;

    JButton start;

    JButton choseBrowser;


    public void run() {
        // 创建窗体
        showFrame = new JFrame("搞快点");
        showFrame.setSize(530,150);
        Container contentPane = showFrame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        // url输入行
        inputJp = new JPanel();
        browserInput = new JTextField("浏览器路径",40);
        //browserInput.setEnabled(false);
        choseBrowser = new JButton("浏览");
        choseBrowser.addActionListener(new ChoseBrowserListener());
        inputJp.add(browserInput);
        inputJp.add(choseBrowser);
        contentPane.add(inputJp,BorderLayout.NORTH);

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
        StartCheckListener startCheckListener = new StartCheckListener();
        start.addActionListener(startCheckListener);
        statusLabel = new JLabel("未开始",JLabel.CENTER);
        statusJp.add(start);
        statusJp.add(statusLabel);
        contentPane.add(statusJp,BorderLayout.CENTER);

        showFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        //showFrame.pack();
        showFrame.setVisible(true);
        showFrame.setAlwaysOnTop(true);
    }

    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
        WebCheckerRunner.context = new WebCheckerRunner();
        context.run();
    }

}
