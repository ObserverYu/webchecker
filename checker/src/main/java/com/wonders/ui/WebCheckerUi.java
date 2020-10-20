package com.wonders.ui;

import com.wonders.ui.webinnerevent.InnerEvent;

import javax.swing.*;

/**
 * ui
 *
 * @author ObserverYu
 * @date 2020/10/19 17:29
 **/
public interface WebCheckerUi {

    /**
    * 内部事件引发ui改变
    *
    * @param
    * @return
    * @author YuChen
    * @date 2020/10/19 17:30
    */
    void postEvent(InnerEvent event);

    JFrame getJframe();
}
