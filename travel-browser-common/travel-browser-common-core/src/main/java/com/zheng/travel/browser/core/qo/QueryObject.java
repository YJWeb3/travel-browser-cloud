package com.zheng.travel.browser.core.qo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QueryObject {

    public QueryObject(Integer current, Integer size) {
        this.current = current;
        this.size = size;
    }

    private String keyword;
    private Integer current = 1;
    private Integer size = 10;

    public Integer getOffset() {
        return (current - 1) * size;
    }
}
