package com.zheng.travel.browser.article.mapper;

import com.zheng.travel.browser.article.domain.Destination;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DestinationMapper extends BaseMapper<Destination> {

    List<Destination> selectHotListByRid(@Param("rid") Long rid, @Param("ids") List<Long> ids);
}
