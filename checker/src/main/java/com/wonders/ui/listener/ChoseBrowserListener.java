package com.wonders.ui.listener;


import com.wonders.WebCheckerContext;
import com.wonders.ui.WebCheckerUiImp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 选择浏览器
 *  
 * @author YuChen
 * @date 2020/10/12 14:09
 **/
 
public class ChoseBrowserListener implements ActionListener {

    private WebCheckerContext webCheckerContext;

    private WebCheckerUiImp ui;

    public ChoseBrowserListener(WebCheckerContext webCheckerContext, WebCheckerUiImp webCheckerUiImp) {
        this.webCheckerContext = webCheckerContext;
        this.ui = webCheckerUiImp;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fDialog = new JFileChooser();
        //设置文件选择框的标题
        fDialog.setDialogTitle("请选择浏览器");
        //弹出选择框
        int returnVal = fDialog.showOpenDialog(null);
        // 如果是选择了文件
        if(JFileChooser.APPROVE_OPTION == returnVal){
            JTextField bowserInput = this.ui.getBrowserInput();
            bowserInput.setText(fDialog.getSelectedFile().getPath());
        }
    }
}
