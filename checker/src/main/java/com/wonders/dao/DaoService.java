package com.wonders.dao;

import com.wonders.spider.entity.ItemList;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author ObserverYu
 * @date 2020/10/16 10:21
 **/
public interface DaoService {

    void init(List<String> weibanList,Integer apply, String result);

    void updateOriginal(List<String> weibanList,Integer apply, String result);

    List<ItemList> getList();

    LinkedBlockingDeque<ItemList> getUpdateList();

    Thread startUpdate();

    Thread getWorker();

}
