package com.zheng.travel.browser.search.vo;

import com.zheng.travel.browser.article.dto.DestinationDto;
import com.zheng.travel.browser.article.dto.StrategyDto;
import com.zheng.travel.browser.article.dto.TravelDto;
import com.zheng.travel.browser.user.dto.UserInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchResult {

    private List<DestinationDto> dests = new ArrayList<>();
    private List<StrategyDto> strategies = new ArrayList<>();
    private List<TravelDto> travels = new ArrayList<>();
    private List<UserInfoDTO> users = new ArrayList<>();
    private Long total = 0L;
}
