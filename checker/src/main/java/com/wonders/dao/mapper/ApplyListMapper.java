package com.wonders.dao.mapper;


import com.wonders.dao.entity.ApplyList;
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
public interface ApplyListMapper {

    List<ApplyList> selectAll();

    List<ApplyList> selectByIdRange(@Param("from") Integer idFrom, @Param("to") Integer idTo);

    void update(@Param("item") ApplyList item);
}
