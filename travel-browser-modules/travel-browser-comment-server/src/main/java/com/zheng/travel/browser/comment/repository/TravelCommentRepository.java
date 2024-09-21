package com.zheng.travel.browser.comment.repository;

import com.zheng.travel.browser.comment.domain.TravelComment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TravelCommentRepository extends MongoRepository<TravelComment, String> {
}
