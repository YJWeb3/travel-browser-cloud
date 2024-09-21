package com.zheng.travel.browser.comment.service;

import com.zheng.travel.browser.comment.domain.StrategyComment;
import com.zheng.travel.browser.comment.qo.CommentQuery;
import org.springframework.data.domain.Page;

public interface StrategyCommentService {

    Page<StrategyComment> page(CommentQuery qo);

    void save(StrategyComment comment);

    void doLike(String cid);

    void replyNumIncr(Long strategyId);
}
