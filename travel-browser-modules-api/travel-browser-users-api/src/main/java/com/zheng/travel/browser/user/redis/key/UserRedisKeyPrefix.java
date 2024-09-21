package com.zheng.travel.browser.user.redis.key;

import com.zheng.travel.browser.redis.key.BaseKeyPrefix;

import java.util.concurrent.TimeUnit;

public class UserRedisKeyPrefix extends BaseKeyPrefix {

    public static final UserRedisKeyPrefix STRATEGIES_STAT_DATA_MAP =
            new UserRedisKeyPrefix("STRATEGIES:STAT:DATA");
    public static final UserRedisKeyPrefix USER_REGISTER_VERIFY_CODE_STRING =
            new UserRedisKeyPrefix("USERS:REGISTER:VERIFY_CODE", 30L, TimeUnit.MINUTES);
    public static final UserRedisKeyPrefix USER_LOGIN_INFO_STRING =
            new UserRedisKeyPrefix("USERS:LOGIN:INFO");

    public UserRedisKeyPrefix(String prefix) {
        super(prefix);
    }

    public UserRedisKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
        super(prefix, timeout, unit);
    }
}
