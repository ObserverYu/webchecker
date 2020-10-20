package com.wonders.dao.service;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author ObserverYu
 * @date 2020/10/16 10:21
 **/
public interface DaoService<T> {

    void init(Object ...param);

    void updateOriginal(Object ...param);

    List<T> getList();

    LinkedBlockingDeque<T> getUpdateList();

    Thread startUpdate();

    Thread getWorker();

}
