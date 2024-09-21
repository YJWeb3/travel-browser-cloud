package com.zheng.travel.browser.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO 对象: Data Transfer Object
 * 负责完成内部微服务之间数据的传递, 在做远程调用时, 传递参数或结果就使用 DTO 对象
 */
@Getter
@Setter
public class UserInfoDTO implements Serializable {

    private Long id;
    private String nickname;  //昵称
    private String phone;  //手机
    private String email;  //邮箱
    private Integer gender; //性别
    private Integer level;  //用户级别
    private String city;  //所在城市
    private String headImgUrl; //头像
    private String info;  //个性签名
}
