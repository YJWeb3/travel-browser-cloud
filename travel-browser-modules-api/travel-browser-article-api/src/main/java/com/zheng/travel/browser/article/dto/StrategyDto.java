package com.zheng.travel.browser.article.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 攻略
 */
@Setter
@Getter
public class StrategyDto implements Serializable {

    private Long id;
    private Long destId;  //关联的目的地
    private String destName;
    private Long themeId; //关联主题
    private String themeName;
    private Long catalogId;  //关联的分类
    private String catalogName;
    private String title;  //标题
    private String subTitle; //副标题
    private String summary;  //内容摘要
    private String coverUrl;  //封面
    private Date createTime;  //创建时间
    private Integer isabroad;  //是否是国外
    private Integer viewnum;  //点击数
    private Integer replynum;  //攻略评论数
    private Integer favornum; //收藏数
    private Integer sharenum; //分享数
    private Integer thumbsupnum; //点赞个数
    private Integer state;  //状态
}