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
 * 目的地(行政地区：国家/省份/城市)
 */
@Setter
@Getter
@TableName("destination")
public class Destination implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id; //主键
    private String name;        //名称
    private String english;  //英文名
    private Long parentId; //上级目的地
    private String parentName;  //上级目的名
    private String info;    //简介
    private String coverUrl;
    @TableField(exist = false)
    private List<Destination> children = new ArrayList<>(); //子目的地
}