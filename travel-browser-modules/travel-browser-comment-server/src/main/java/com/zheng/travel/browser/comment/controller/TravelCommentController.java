package com.zheng.travel.browser.comment.controller;

import com.zheng.travel.browser.auth.anno.RequireLogin;
import com.zheng.travel.browser.comment.domain.TravelComment;
import com.zheng.travel.browser.comment.service.TravelCommentService;
import com.zheng.travel.browser.core.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/travels/comments")
@RestController
public class TravelCommentController {

    private final TravelCommentService travelCommentService;

    public TravelCommentController(TravelCommentService travelCommentService) {
        this.travelCommentService = travelCommentService;
    }

    @GetMapping("/query")
    public R<List<TravelComment>> query(Long travelId) {
        return R.ok(travelCommentService.findList(travelId));
    }

    @RequireLogin
    @PostMapping("/save")
    public R<?> save(TravelComment comment) {
        travelCommentService.save(comment);
        return R.ok();
    }
}
