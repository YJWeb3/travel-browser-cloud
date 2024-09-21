package com.zheng.travel.browser.comment.controller;

import com.zheng.travel.browser.auth.anno.RequireLogin;
import com.zheng.travel.browser.comment.domain.StrategyComment;
import com.zheng.travel.browser.comment.qo.CommentQuery;
import com.zheng.travel.browser.comment.service.StrategyCommentService;
import com.zheng.travel.browser.core.utils.R;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/strategies/comments")
@RestController
public class StrategyCommentController {

    private final StrategyCommentService strategyCommentService;

    public StrategyCommentController(StrategyCommentService strategyCommentService) {
        this.strategyCommentService = strategyCommentService;
    }

    @GetMapping("/query")
    public R<Page<StrategyComment>> query(CommentQuery qo) {
        return R.ok(strategyCommentService.page(qo));
    }

    @RequireLogin
    @PostMapping("/likes")
    public R<?> likes(String cid) {
        strategyCommentService.doLike(cid);
        return R.ok();
    }

    @RequireLogin
    @PostMapping("/save")
    public R<?> save(StrategyComment comment) {
        strategyCommentService.save(comment);
        // 评论数+1
        strategyCommentService.replyNumIncr(comment.getStrategyId());
        return R.ok();
    }
}
