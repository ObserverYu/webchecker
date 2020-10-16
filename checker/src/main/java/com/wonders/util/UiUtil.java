package com.wonders.util;

import javax.swing.*;

/**
 * 
 *  
 * @author ObserverYu
 * @date 2020/10/15 17:43
 **/
 
public class UiUtil {

    public static void showWarningMessage(JFrame jFrame, String message){
        JOptionPane.showMessageDialog(
                jFrame,
                message,
                "提示",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void showErrorMessage(JFrame jFrame, String message){
        JOptionPane.showMessageDialog(
                jFrame,
                message,
                "错误",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void showInfoMessage(JFrame jFrame, String message){
        JOptionPane.showMessageDialog(
                jFrame,
                message,
                "警告",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
