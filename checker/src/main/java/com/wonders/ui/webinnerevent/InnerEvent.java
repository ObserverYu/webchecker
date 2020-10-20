package com.wonders.ui.webinnerevent;

/**
 * 爬虫代码触发的某些内部事件
 *
 * @author ObserverYu
 * @date 2020/10/19 16:16
 **/
public interface InnerEvent<T> {
    String getMessage();

    Integer getCode();

    T getData();
}
