package com.zheng.travel.browser.article.qo;

import com.zheng.travel.browser.article.vo.TravelRange;
import com.zheng.travel.browser.core.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TravelQuery extends QueryObject {

    private final List<String> ALLOW_ORDER_BY_COLUMNS = Arrays.asList("viewnum", "create_time");
    private final static Map<Integer, TravelRange> TRAVEL_TIME_MAP = new HashMap<>();
    private final static Map<Integer, TravelRange> COST_MAP = new HashMap<>();
    private final static Map<Integer, TravelRange> DAYS_MAP = new HashMap<>();

    static {
        // 初始化数据
        TRAVEL_TIME_MAP.put(1, new TravelRange(1, 2));
        TRAVEL_TIME_MAP.put(2, new TravelRange(3, 4));
        TRAVEL_TIME_MAP.put(3, new TravelRange(5, 6));
        TRAVEL_TIME_MAP.put(4, new TravelRange(7, 8));
        TRAVEL_TIME_MAP.put(5, new TravelRange(9, 10));
        TRAVEL_TIME_MAP.put(6, new TravelRange(11, 12));

        // 人均花费
        COST_MAP.put(1, new TravelRange(1, 999));
        COST_MAP.put(2, new TravelRange(1000, 5999));
        COST_MAP.put(3, new TravelRange(6000, 19999));
        COST_MAP.put(4, new TravelRange(20000, Integer.MAX_VALUE));

        // 出行天数
        DAYS_MAP.put(1, new TravelRange(1, 3));
        DAYS_MAP.put(2, new TravelRange(4, 7));
        DAYS_MAP.put(3, new TravelRange(8, 14));
        DAYS_MAP.put(4, new TravelRange(15, 3650));
    }

    private Long destId;
    private String orderBy;
    private TravelRange travelTimeRange;
    private TravelRange costRange;
    private TravelRange dayRange;

    public void setTravelTimeType(Integer travelTimeType) {
        this.travelTimeRange = TRAVEL_TIME_MAP.get(travelTimeType);
    }

    public void setConsumeType(Integer consumeType) {
        this.costRange = COST_MAP.get(consumeType);
    }

    public void setDayType(Integer dayType) {
        this.dayRange = DAYS_MAP.get(dayType);
    }

    public void setOrderBy(String orderBy) {
        if (ALLOW_ORDER_BY_COLUMNS.contains(orderBy)) {
            this.orderBy = orderBy;
        }
    }
}
