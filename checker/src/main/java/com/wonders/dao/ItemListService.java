package com.wonders.dao;

import com.wonders.spider.entity.ItemList;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 
 *  
 * @author ObserverYu
 * @date 2020/10/15 9:47
 **/
 
public class ItemListService {

    private ItemListMapper itemListMapper;

    private List<ItemList> list;

    private LinkedBlockingDeque<ItemList> updateList;

    private volatile int count;

    public static ItemListService build(List<String> weibanList){
        return build(weibanList,null,null);
    }

    public static ItemListService build(List<String> weibanList,Integer apply, String result){
        // 持久层
        ItemListService itemListService = new ItemListService();
        SqlSessionFactory build = MySqlFactoryBuilder.build();
        SqlSession sqlSession = build.openSession();
        ItemListMapper mapper = sqlSession.getMapper(ItemListMapper.class);
        itemListService.setItemListMapper(mapper);
        List<ItemList> allWeibanItem = mapper.selectByWeiBanListAndCondition(weibanList,apply,result);
        itemListService.setList(allWeibanItem);
        itemListService.setCount(allWeibanItem.size());
        LinkedBlockingDeque<ItemList> updateList = new LinkedBlockingDeque<>();
        itemListService.setUpdateList(updateList);
        itemListService.startUpdate();
        return itemListService;
    }

    private void startUpdate(){
        new Thread(()->{
            try {
                while (count != 0){
                    ItemList take = this.updateList.take();
                    this.itemListMapper.update(take);
                    count --;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public ItemListMapper getItemListMapper() {
        return itemListMapper;
    }

    public void setItemListMapper(ItemListMapper itemListMapper) {
        this.itemListMapper = itemListMapper;
    }

    public List<ItemList> getList() {
        return list;
    }

    public void setList(List<ItemList> list) {
        this.list = list;
    }

    public LinkedBlockingDeque<ItemList> getUpdateList() {
        return updateList;
    }

    public void setUpdateList(LinkedBlockingDeque<ItemList> updateList) {
        this.updateList = updateList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
