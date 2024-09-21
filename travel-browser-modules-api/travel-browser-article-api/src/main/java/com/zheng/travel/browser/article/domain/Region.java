package com.zheng.travel.browser.article.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 区域
 */
@Setter
@Getter
@TableName("region")
public class Region implements Serializable {
    public static final int STATE_HOT = 1;
    public static final int STATE_NORMAL = 0;

    @TableId(type= IdType.AUTO)
    private Long id;

    private String name;        //地区名
    private String sn;          //地区编码
    private String refIds;     //关联的id， 多个以，隔开

    private Integer ishot = STATE_NORMAL;         //是否为热点
    private Integer seq;   //序号
    private String info;  //简介


    public List<Long> parseRefIds(){
        List<Long> ids = new ArrayList<>();
        if(StringUtils.hasLength(refIds)){
            String[] split = refIds.split(",");
            if(split.length > 0){
                for (int i = 0;i <split.length; i++) {
                    ids.add(Long.parseLong(split[i]));
                }
            }
        }
       return ids;
    }
}