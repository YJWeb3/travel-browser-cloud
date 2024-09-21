package com.zheng.travel.browser.comment.service;

import com.zheng.travel.browser.comment.domain.TravelComment;
import com.zheng.travel.browser.comment.qo.CommentQuery;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TravelCommentService {

    Page<TravelComment> page(CommentQuery qo);

    void save(TravelComment comment);

    List<TravelComment> findList(Long travelId);
}
