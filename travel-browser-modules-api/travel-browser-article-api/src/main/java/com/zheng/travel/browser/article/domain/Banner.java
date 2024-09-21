package com.zheng.travel.browser.article.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 游记推荐
 */
@Setter
@Getter
@TableName("banner")
public class Banner  implements Serializable {
    public static final int STATE_NORMAL = 0;   //正常
    public static final int STATE_DISABLE = 1;  //禁用

    public static final int TYPE_TRAVEL = 1;  //游记
    public static final int TYPE_STRATEGY = 2;  //攻略


    @TableId(type= IdType.AUTO)
    private Long id;

    private Long refid;  //关联id

    private String title;  //标题

    private String subtitle; //副标题

    private String coverUrl; //封面

    private Integer state = STATE_NORMAL; //状态

    private Integer seq; //排序

    private Integer type; //类型

}