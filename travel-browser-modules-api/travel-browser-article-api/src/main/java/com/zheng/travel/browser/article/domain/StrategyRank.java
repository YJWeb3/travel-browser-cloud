package com.zheng.travel.browser.article.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 攻略统计表
 */
@Setter
@Getter
@TableName("strategy_rank")
public class StrategyRank {

    public static final int TYPE_ABROAD = 1;  //国外
    public static final int TYPE_CHINA = 2;   //国内
    public static final int TYPE_HOT = 3;     //热门

    private Long id;
    private Long destId;  //目的地id
    private String destName; //目的地名称
    private Long strategyId; //攻略id
    private String strategyTitle; //攻略标题
    private int type; //排行类型
    private Date statisTime; //归档统计时间
    private Long statisnum; //归档统计数
}