package com.zheng.travel.browser.comment.service.impl;

import com.zheng.travel.browser.auth.util.AuthenticationUtils;
import com.zheng.travel.browser.comment.domain.TravelComment;
import com.zheng.travel.browser.comment.qo.CommentQuery;
import com.zheng.travel.browser.comment.repository.TravelCommentRepository;
import com.zheng.travel.browser.comment.service.TravelCommentService;
import com.zheng.travel.browser.user.vo.LoginUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TravelCommentServiceImpl implements TravelCommentService {

    private final MongoTemplate mongoTemplate;
    private final TravelCommentRepository travelCommentRepository;

    public TravelCommentServiceImpl(MongoTemplate mongoTemplate, TravelCommentRepository travelCommentRepository) {
        this.mongoTemplate = mongoTemplate;
        this.travelCommentRepository = travelCommentRepository;
    }

    @Override
    public Page<TravelComment> page(CommentQuery qo) {
        // 1. 拼接查询条件
        Criteria criteria = Criteria.where("travelId").is(qo.getArticleId());
        // 2. 创建查询对象, 关联条件
        Query query = new Query();
        query.addCriteria(criteria);

        // 统计总数
        long total = mongoTemplate.count(query, TravelComment.class);
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
        List<TravelComment> list = mongoTemplate.find(query, TravelComment.class);
        return new PageImpl<>(list, request, total);
    }

    @Override
    public void save(TravelComment comment) {
        // 1. 获取用户信息
        LoginUser user = AuthenticationUtils.getUser();
        // 2. 补充前端没有传的参数
        comment.setUserId(user.getId());
        comment.setNickname(user.getNickname());
        comment.setCity(user.getCity());
        comment.setLevel(user.getLevel());
        comment.setHeadImgUrl(user.getHeadImgUrl());
        comment.setCreateTime(new Date());
        // 3. 设置类型
        if (comment.getRefComment() != null && StringUtils.hasLength(comment.getRefComment().getId())) {
            // 评论的评论
            comment.setType(TravelComment.TRAVLE_COMMENT_TYPE);
        } else {
            // 普通评论
            comment.setType(TravelComment.TRAVLE_COMMENT_TYPE_COMMENT);
        }
        // 4. 保存评论
        travelCommentRepository.save(comment);
    }

    @Override
    public List<TravelComment> findList(Long travelId) {
        // 1. 拼接查询条件
        // 2. 关联查询条件
        // 3. 查询返回结果
        Query query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "createTime"))
                .addCriteria(Criteria.where("travelId").is(travelId));

        List<TravelComment> comments = mongoTemplate.find(query, TravelComment.class);
        for (TravelComment comment : comments) {
            TravelComment refComment = comment.getRefComment();
            if (refComment != null && refComment.getId() != null) {
                Optional<TravelComment> refCommentOptional = travelCommentRepository.findById(refComment.getId());
                comment.setRefComment(refCommentOptional.orElse(null));
            }
        }
        return comments;
    }
}
