package com.wonders.ui.listener;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * 页面确认事件
 *  
 * @author YuChen
 * @date 2020/10/12 10:16
 **/
 
public class ConfirmWebEventHandler implements KeyEventPostProcessor {

    @Override
    public boolean postProcessKeyEvent(KeyEvent e) {

        int keyCode = e.getKeyCode();
        switch (keyCode){
            case KeyEvent.VK_1:
                webPass();
                return true;
            case KeyEvent.VK_2:
                webFail();
                return true;
            case KeyEvent.VK_3:
                webFailAndEditDesc();
                return true;
            default:
                return false;
        }
    }

    private void webFailAndEditDesc() {

    }

    private void webFail() {

    }

    private void webPass() {
    }
}
