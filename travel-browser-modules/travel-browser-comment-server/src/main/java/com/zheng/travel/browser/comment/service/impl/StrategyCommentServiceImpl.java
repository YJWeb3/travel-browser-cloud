package com.zheng.travel.browser.comment.service.impl;

import com.zheng.travel.browser.auth.util.AuthenticationUtils;
import com.zheng.travel.browser.comment.domain.StrategyComment;
import com.zheng.travel.browser.comment.redis.key.CommentRedisKeyPrefix;
import com.zheng.travel.browser.comment.qo.CommentQuery;
import com.zheng.travel.browser.comment.repository.StrategyCommentRepository;
import com.zheng.travel.browser.comment.service.StrategyCommentService;
import com.zheng.travel.browser.redis.utils.RedisCache;
import com.zheng.travel.browser.user.vo.LoginUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StrategyCommentServiceImpl implements StrategyCommentService {

    private final RedisCache redisCache;
    private final MongoTemplate mongoTemplate;
    private final StrategyCommentRepository strategyCommentRepository;

    public StrategyCommentServiceImpl(MongoTemplate mongoTemplate, StrategyCommentRepository strategyCommentRepository, RedisCache redisCache) {
        this.mongoTemplate = mongoTemplate;
        this.strategyCommentRepository = strategyCommentRepository;
        this.redisCache = redisCache;
    }

    @Override
    public Page<StrategyComment> page(CommentQuery qo) {
        // 1. 拼接查询条件
        Criteria criteria = Criteria.where("strategyId").is(qo.getArticleId());
        // 2. 创建查询对象, 关联条件
        Query query = new Query();
        query.addCriteria(criteria);

        // 统计总数
        long total = mongoTemplate.count(query, StrategyComment.class);
        if (total == 0) {
            return Page.empty();
        }

        // 3. 设置分页参数
        // 计算分页参数
        PageRequest request = PageRequest.of(qo.getCurrent() - 1, qo.getSize());
        query.skip(request.getOffset()).limit(request.getPageSize());
        // 4. 按照时间排序
        query.with(Sort.by(Sort.Direction.DESC, "createTime"));
        // 5. 分页查询
        List<StrategyComment> list = mongoTemplate.find(query, StrategyComment.class);
        return new PageImpl<>(list, request, total);
    }

    @Override
    public void save(StrategyComment comment) {
        // 获取当前登录的用户
        LoginUser user = AuthenticationUtils.getUser();
        comment.setUserId(user.getId());
        comment.setNickname(user.getNickname());
        comment.setCity(user.getCity());
        comment.setLevel(user.getLevel());
        comment.setHeadImgUrl(user.getHeadImgUrl());
        comment.setCreateTime(new Date());

        // 保存到 mongodb
        strategyCommentRepository.save(comment);
    }

    @Override
    public void doLike(String cid) {
        // 1. 基于 cid 查询评论对象
        Optional<StrategyComment> optional = strategyCommentRepository.findById(cid);
        if (optional.isPresent()) {
            StrategyComment strategyComment = optional.get();
            // 2. 获取当前登录的用户对象
            LoginUser user = AuthenticationUtils.getUser();
            // 3. 判断当前用户是否已经点过赞
            if (strategyComment.getThumbuplist().contains(user.getId())) {
                // 4. 如果点过赞: 点赞数-1, 将用户 id 从集合中删除
                strategyComment.setThumbupnum(strategyComment.getThumbupnum() - 1);
                List<Long> thumbuplist = strategyComment.getThumbuplist();
                thumbuplist.remove(user.getId());
            } else {
                // 5. 如果没点赞: 点赞数+1, 将用户 id 加入集合
                strategyComment.setThumbupnum(strategyComment.getThumbupnum() + 1);
                List<Long> thumbuplist = strategyComment.getThumbuplist();
                thumbuplist.add(user.getId());
            }

            // 6. 重新将对象保存到 mongodb
            strategyCommentRepository.save(strategyComment);
        }
    }

    @Override
    public void replyNumIncr(Long strategyId) {
        redisCache.hashIncrement(CommentRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP,
                "replynum", 1, strategyId + "");
    }
}
