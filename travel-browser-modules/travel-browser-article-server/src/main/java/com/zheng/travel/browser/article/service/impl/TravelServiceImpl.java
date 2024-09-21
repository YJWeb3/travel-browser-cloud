package com.zheng.travel.browser.article.service.impl;

import com.zheng.travel.browser.article.domain.Travel;
import com.zheng.travel.browser.article.domain.TravelContent;
import com.zheng.travel.browser.article.feign.UserInfoFeignService;
import com.zheng.travel.browser.article.mapper.TravelContentMapper;
import com.zheng.travel.browser.article.mapper.TravelMapper;
import com.zheng.travel.browser.article.qo.TravelQuery;
import com.zheng.travel.browser.article.service.TravelService;
import com.zheng.travel.browser.article.vo.TravelRange;
import com.zheng.travel.browser.auth.util.AuthenticationUtils;
import com.zheng.travel.browser.core.utils.R;
import com.zheng.travel.browser.user.dto.UserInfoDTO;
import com.zheng.travel.browser.user.vo.LoginUser;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel> implements TravelService {

    private final UserInfoFeignService userInfoFeignService;
    private final ThreadPoolExecutor businessThreadPoolExecutor;
    private final TravelContentMapper travelContentMapper;

    public TravelServiceImpl(UserInfoFeignService userInfoFeignService, ThreadPoolExecutor businessThreadPoolExecutor, TravelContentMapper travelContentMapper) {
        this.userInfoFeignService = userInfoFeignService;
        this.businessThreadPoolExecutor = businessThreadPoolExecutor;
        this.travelContentMapper = travelContentMapper;
    }

    @Override
    public Travel getById(Serializable id) {
        Travel travel = super.getById(id);
        if (travel == null) {
            return null;
        }
        TravelContent content = travelContentMapper.selectById(id);
        travel.setContent(content);

        R<UserInfoDTO> result = userInfoFeignService.getById(travel.getAuthorId());
        UserInfoDTO dto = result.checkAndGet();
        travel.setAuthor(dto);
        return travel;
    }

    @Override
    public Page<Travel> pageList(TravelQuery query) {
        QueryWrapper<Travel> wrapper = Wrappers.<Travel>query()
                .eq(query.getDestId() != null, "dest_id", query.getDestId());

        if (query.getTravelTimeRange() != null) {
            TravelRange range = query.getTravelTimeRange();
            wrapper.between("MONTH(travel_time)", range.getMin(), range.getMax());
        }

        if (query.getCostRange() != null) {
            TravelRange range = query.getCostRange();
            wrapper.between("avg_consume", range.getMin(), range.getMax());
        }

        if (query.getDayRange() != null) {
            TravelRange range = query.getDayRange();
            wrapper.between("day", range.getMin(), range.getMax());
        }

        // 排序
        wrapper.orderByDesc(query.getOrderBy());

        LoginUser user = AuthenticationUtils.getUser();
        if (user == null) {
            // 游客: 只能看公开且已发布的游记
            wrapper.eq("ispublic", Travel.ISPUBLIC_YES)
                    .eq("state", Travel.STATE_RELEASE);
        } else {
            // 用户: 可以查看公开已发布和自己所有的游记
            // (author_id = #{user.id} or (ispublic = 1 and state = 2))
            wrapper.and(w -> {
                w.eq("author_id", user.getId())
                        .or(ww -> {
                            ww.eq("ispublic", Travel.ISPUBLIC_YES)
                                    .eq("state", Travel.STATE_RELEASE);
                        });
            });
        }

        Page<Travel> page = super.page(new Page<>(query.getCurrent(), query.getSize()), wrapper);
        List<Travel> records = page.getRecords();

        // 创建计数器, 等待子线程都执行完成
        CountDownLatch latch = new CountDownLatch(records.size());

        for (Travel travel : records) {
            businessThreadPoolExecutor.execute(() -> {
                try {
                    // 查找游记的作者
                    R<UserInfoDTO> result = userInfoFeignService.getById(travel.getAuthorId());
                    if (result.getCode() != R.CODE_SUCCESS) {
                        log.warn("[游记服务] 查询用户作者失败, 返回数据异常: {}", JSON.toJSONString(result));
                        // 数量-1
                        latch.countDown();
                        return;
                    }

                    travel.setAuthor(result.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 数量-1
                    latch.countDown();
                }
            });
        }

        // 返回前等待计数器数值减到0, 也就表示所有子线程都执行结束
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return page;
    }
}
