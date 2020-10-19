package com.wonders.dao;

import com.wonders.WebCheckerContext;
import com.wonders.spider.entity.ItemList;
import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *  以mysql作为存储
 *  
 * @author ObserverYu
 * @date 2020/10/15 9:47
 **/

@Data
public class ItemListNewService implements DaoService {

    private ItemListMapper itemListMapper;

    private List<ItemList> list;

    private LinkedBlockingDeque<ItemList> updateList;

    private volatile int count;

    private WebCheckerContext context;

    private Thread worker;

    public static ItemListNewService build(List<String> weibanList){
        return build(weibanList,null,null);
    }

    public static ItemListNewService build(List<String> weibanList, Integer apply, String result){
        return build(weibanList,apply,result,null);
    }

    public static ItemListNewService build(List<String> weibanList, Integer apply, String result, WebCheckerContext context){
        // 持久层
        ItemListNewService itemListService = new ItemListNewService();
        itemListService.init(weibanList,apply,result);
        context.setDaoService(itemListService);
        return itemListService;
    }

    @Override
    public void init(List<String> weibanList, Integer apply, String result) {
        SqlSessionFactory build = MySqlFactoryBuilder.build();
        SqlSession sqlSession = build.openSession();
        ItemListMapper mapper = sqlSession.getMapper(ItemListMapper.class);
        this.setItemListMapper(mapper);
        List<ItemList> allWeibanItem = mapper.selectByIdFanwei(2567,2884);
        this.setList(allWeibanItem);
        this.setCount(allWeibanItem.size());
        LinkedBlockingDeque<ItemList> updateList = new LinkedBlockingDeque<>();
        this.setUpdateList(updateList);
    }

    @Override
    public void updateOriginal(List<String> weibanList, Integer apply, String result) {
        List<ItemList> allWeibanItem = getItemListMapper().selectByWeiBanListAndCondition(weibanList,apply,result);
        this.setList(allWeibanItem);
        this.setCount(allWeibanItem.size());
        updateList.clear();
    }

    @Override
    public Thread startUpdate(){
        Thread thread = new Thread(() -> {
            try {
                while (count != 0) {
                    ItemList take = this.updateList.take();
                    this.itemListMapper.update(take);
                    count--;
                }
            } catch (InterruptedException e) {
                ItemList take;
                while ((take = this.updateList.poll()) != null) {
                    this.itemListMapper.update(take);
                }
                System.out.println("程序终止");
            }
        });
        thread.setDaemon(true);
        thread.start();
        return thread;
    }

    @Override
    public Thread getWorker() {
        return worker;
    }
}
