package com.zheng.travel.browser.article.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 目的地(行政地区：国家/省份/城市)
 */
@Setter
@Getter
public class DestinationDto implements Serializable {
    private Long id;
    private String name;        //名称
    private String english;  //英文名
    private Long parentId; //上级目的地
    private String parentName;  //上级目的名
    private String info;    //简介
    private String coverUrl;
}