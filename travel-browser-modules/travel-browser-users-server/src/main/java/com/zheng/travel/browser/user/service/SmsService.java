package com.zheng.travel.browser.user.service;

public interface SmsService {

    /**
     * 注册发送短信功能
     *
     * @param phone 手机号
     */
    void registerSmsSend(String phone);
}
