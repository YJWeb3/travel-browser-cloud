package com.zheng.travel.browser.article.qo;

import com.zheng.travel.browser.core.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DestinationQuery extends QueryObject {

    private Long parentId;
}
