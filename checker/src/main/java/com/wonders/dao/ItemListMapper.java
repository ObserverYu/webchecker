package com.wonders.dao;


import com.wonders.spider.entity.ItemList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 事项类型 Mapper 接口
 * </p>
 *
 * @author YuChen
 * @since 2020-10-14
 */
public interface ItemListMapper{

    List<ItemList> selectAll();

    List<ItemList> selectByWeiBanList(List<String> weiban);

    List<ItemList> selectByWeiBanListAndCondition(@Param("weiban") List<String> weiban
            , @Param("apply") Integer apply, @Param("result") String result);

    void update(@Param("item") ItemList item);
}
