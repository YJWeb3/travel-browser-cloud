package com.zheng.travel.browser.user.domain;

import com.zheng.travel.browser.user.dto.UserInfoDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zheng.travel.browser.user.dto.UserInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

//@ApiModel(value="用户", description="平台注册用户模型")
@Setter
@Getter
@TableName("userinfo")
public class UserInfo implements Serializable {
    public static final int GENDER_SECRET = 0; //保密
    public static final int GENDER_MALE = 1;   //男
    public static final int GENDER_FEMALE = 2;  //女
    public static final int STATE_NORMAL = 0;  //正常
    public static final int STATE_DISABLE = 1;  //冻结

    @TableId(type = IdType.AUTO)
    private Long id;

    //@ApiModelProperty(value="昵称",name="nickName",dataType = "String",required = true)
    private String nickname;  //昵称
    private String phone;  //手机
    private String email;  //邮箱

    private String password; //密码
    private Integer gender = GENDER_SECRET; //性别
    private Integer level = 0;  //用户级别
    private String city;  //所在城市
    private String headImgUrl; //头像
    private String info;  //个性签名
    private Integer state = STATE_NORMAL; //状态

    public UserInfoDTO toDto() {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setCity(this.city);
        dto.setInfo(this.info);
        dto.setEmail(this.email);
        dto.setGender(this.gender);
        dto.setId(this.id);
        dto.setHeadImgUrl(this.headImgUrl);
        dto.setLevel(this.level);
        dto.setNickname(this.nickname);
        dto.setPhone(this.phone);
        return dto;
    }
}
