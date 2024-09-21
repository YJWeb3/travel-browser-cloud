package com.zheng.travel.browser.comment.qo;

import com.zheng.travel.browser.core.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentQuery extends QueryObject {

    private Long articleId;
}
