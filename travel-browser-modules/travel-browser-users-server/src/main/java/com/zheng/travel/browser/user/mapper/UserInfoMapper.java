package com.zheng.travel.browser.user.mapper;

import com.zheng.travel.browser.user.domain.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper extends BaseMapper<UserInfo> {

    List<Long> selectFavorStrategyIdList(Long userId);

    void deleteFavorStrategy(@Param("userId") Long userId, @Param("strategyId") Long strategyId);

    void insertFavorStrategy(@Param("userId") Long userId, @Param("strategyId") Long strategyId);
}
