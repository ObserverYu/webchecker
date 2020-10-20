package com.wonders.dao.service;

import com.wonders.WebCheckerContext;
import com.wonders.dao.MySqlFactoryBuilder;
import com.wonders.dao.entity.ApplyList;
import com.wonders.dao.mapper.ApplyListMapper;
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
public class ApplyListService implements DaoService<ApplyList> {

    private ApplyListMapper applyListMapper;

    private List<ApplyList> list;

    private LinkedBlockingDeque<ApplyList> updateList;

    private volatile int count;

    private WebCheckerContext context;

    private Thread worker;

    public static ApplyListService build(List<String> weibanList){
        return build(weibanList,null,null);
    }

    public static ApplyListService build(List<String> weibanList, Integer apply, String result){
        return build(weibanList,apply,result,null);
    }

    public static ApplyListService build(List<String> weibanList, Integer apply, String result, WebCheckerContext context){
        // 持久层
        ApplyListService itemListService = new ApplyListService();
        itemListService.init(weibanList,apply,result);
        context.setDaoService(itemListService);
        return itemListService;
    }

    @Override
    public void init(Object ...param) {
        List<String> weibanList = null;
        Integer apply = null;
        String result = null;
        if(param != null && param.length == 3){
            weibanList = (List<String>)param[0];
            apply = (Integer)param[1];
            result = (String)param[2];
        }
        SqlSessionFactory build = MySqlFactoryBuilder.build();
        SqlSession sqlSession = build.openSession();
        ApplyListMapper mapper = sqlSession.getMapper(ApplyListMapper.class);
        this.setApplyListMapper(mapper);
        List<ApplyList> allWeibanItem = mapper.selectByIdRange(2567,2884);
        this.setList(allWeibanItem);
        this.setCount(allWeibanItem.size());
        LinkedBlockingDeque<ApplyList> updateList = new LinkedBlockingDeque<>();
        this.setUpdateList(updateList);
    }

    @Override
    public void updateOriginal(Object ...param) {
    }

    @Override
    public Thread startUpdate(){
        Thread thread = new Thread(() -> {
            try {
                while (count != 0) {
                    ApplyList take = this.updateList.take();
                    this.applyListMapper.update(take);
                    count--;
                }
            } catch (InterruptedException e) {
                ApplyList take;
                while ((take = this.updateList.poll()) != null) {
                    this.applyListMapper.update(take);
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
