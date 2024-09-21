package com.zheng.travel.browser.article.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 游记
 */
@Setter
@Getter
public class TravelDto implements Serializable {

    private Long id;
    private Long destId;  //目的地
    private String destName;  //目的地
    private Long authorId;  //作者id
    private String title;  //标题
    private String summary;//概要
    private String coverUrl; //封面
    private Date travelTime; //旅游时间
    private Integer avgConsume; //人均消费
    private Integer day;  //旅游天数
    private Integer person;  //和谁旅游
    private Date createTime; //创建时间
    private Date releaseTime; //发布时间
    private Date lastUpdateTime; //最新更新时间内
    private Integer ispublic; //是否发布
    private Integer viewnum;  //点击/阅读数
    private Integer replynum; //回复数
    private Integer favornum;//收藏数
    private Integer sharenum;//分享数
    private Integer thumbsupnum;//点赞数
    private Integer state;//游记状态
}