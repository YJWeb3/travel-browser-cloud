package com.zheng.travel.browser.redis.key;

import java.util.concurrent.TimeUnit;

/**
 * Redis Key 通用规范设计
 */
public interface KeyPrefix {

    /**
     * @return redis key 的前缀
     */
    String getPrefix();

    /**
     * @return 超时时间, -1 表示没有超时时间
     */
    default Long getTimeout() {
        return -1L;
    }

    /**
     * @return 如果有超时时间, 就必须要有单位, 如果没有可以不设置单位
     */
    default TimeUnit getUnit() {
        return null;
    }

    default String fullKey(String... suffix) {
        if (suffix == null) {
            return getPrefix();
        }

        StringBuilder sb = new StringBuilder(100);
        sb.append(getPrefix());

        for (String s : suffix) {
            sb.append(":").append(s);
        }

        return sb.toString();
    }
}
