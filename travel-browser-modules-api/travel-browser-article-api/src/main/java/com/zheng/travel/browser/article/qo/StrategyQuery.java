package com.zheng.travel.browser.article.qo;

import com.zheng.travel.browser.core.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class StrategyQuery extends QueryObject {

    private static final List<String> ORDER_BY_COLUMNS = Arrays.asList("viewnum", "create_time");

    private Long themeId;
    private Long destId;
    private Long refid;
    private Integer type;
    private String orderBy;

    public void setOrderBy(String orderBy) {
        if (!StringUtils.isEmpty(orderBy) && ORDER_BY_COLUMNS.contains(orderBy)) {
            this.orderBy = orderBy;
        }
    }
}
