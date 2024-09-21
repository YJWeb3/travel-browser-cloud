package com.zheng.travel.browser.article.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 攻略分类： 从属于某个目的地
 */
@Setter
@Getter
@TableName("strategy_catalog")
public class StrategyCatalog implements Serializable {

    public static final int STATE_NORMAL = 0;  //显示
    public static final int STATE_DISABLE = 1;  //禁用

    @TableId(type= IdType.AUTO)
    private Long id;

    private Long destId;  //目的地
    private String destName;    //目的名称
    private Integer state = STATE_NORMAL;  //状态
    private String name;  //分类名
    private Integer seq; //排序

    @TableField(exist = false)
    private List<Strategy> strategies = new ArrayList<>();

}