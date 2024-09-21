package com.zheng.travel.browser.comment.repository;

import com.zheng.travel.browser.comment.domain.StrategyComment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StrategyCommentRepository extends MongoRepository<StrategyComment, String> {
}
