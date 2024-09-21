package com.zheng.travel.browser.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 用于接收注册请求传递的参数
 */
@Getter
@Setter
@ApiModel(value = "注册请求", description = "封装注册时的用户信息")
public class RegisterRequest {

    @ApiModelProperty(name = "phone", notes = "手机号")
    private String phone;
    @ApiModelProperty(name = "nickname", notes = "昵称")
    private String nickname;
    @ApiModelProperty(name = "password", notes = "密码")
    private String password;
    @ApiModelProperty(name = "verifyCode", notes = "验证码")
    private String verifyCode;
}
