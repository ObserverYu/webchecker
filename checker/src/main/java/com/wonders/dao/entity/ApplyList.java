package com.wonders.dao.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 事项类型
 * </p>
 *
 * @author YuChen
 * @since 2020-10-14
 */
@Data
@ToString
public class ApplyList implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String itemCode;

    private String itemType;

    private String itemName;

    private String qingxing;

    private String checker;

    /**
     * 未检查 检查失败 正常 异常
     */
    private String result;

    private String applyUrl;

    private String tencode;

    private String area;

    private String applyCode;

    private String weibanName;




}
