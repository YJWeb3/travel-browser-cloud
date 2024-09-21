package com.zheng.travel.browser.search.service.impl;

import com.zheng.travel.browser.core.qo.QueryObject;
import com.zheng.travel.browser.search.parser.ElasticsearchTypeParser;
import com.zheng.travel.browser.search.service.ElasticsearchService;
import com.zheng.travel.browser.search.utils.BeanUtils;
import com.alibaba.fastjson2.JSON;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    public static final Logger log = LoggerFactory.getLogger(ElasticsearchServiceImpl.class);

    @Autowired
    private ElasticsearchRestTemplate template;

    @Override
    public void save(Object entity) {
        template.save(entity);
    }

    @Override
    public void save(Iterable<?> iterable) {
        template.save(iterable);
    }

    @Override
    public void deleteById(String id, Class<?> clazz) {
        template.delete(id, clazz);
    }

    @Override
    public <T> Page<T> searchWithHighlight(Class<?> esclz, Class<T> dtoclz, QueryObject qo, ElasticsearchTypeParser<T> parser, String... fields) {
        //高亮显示
        /*"query":{
            "multi_match": {
                "query": "广州",
                "fields": ["title","subTitle","summary"]
            }
        },*/
        MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(qo.getKeyword(), fields);
        HighlightBuilder highlightBuilder = new HighlightBuilder(); // 生成高亮查询器
        for (String field : fields) {
            highlightBuilder.field(field);// 高亮查询字段
        }
        highlightBuilder.requireFieldMatch(false); // 如果要多个字段高亮,这项要为false
        highlightBuilder.preTags("<span style='color:red'>"); // 高亮设置
        highlightBuilder.postTags("</span>");
        highlightBuilder.fragmentSize(800000); // 最大高亮分片数
        highlightBuilder.numOfFragments(0); // 从第一个分片获取高亮片段
        /**
         "from": 0,
         "size":3,
         */
        Pageable pageable = PageRequest.of(qo.getCurrent() - 1, qo.getSize(),
                Sort.Direction.ASC, "_id");// 设置分页参数

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder) // match查询
                .withPageable(pageable)
                .withHighlightBuilder(highlightBuilder) // 设置高亮
                .build();

        // 高亮查询, 得到命中的数据
        SearchHits<?> searchHits = template.search(searchQuery, esclz);

        // 最终返回的 list 对象
        List<T> list = new ArrayList<>();
        for (SearchHit<?> searchHit : searchHits) { // 获取搜索到的数据
            // 具体解析操作: 交给外部调用者去实现
            T target = parser.parse(dtoclz, searchHit.getId());

            // 处理高亮
            Map<String, String> map = highlightFieldsCopy(searchHit.getHighlightFields(), fields);

            //1：spring 框架中 BeanUtils 类，如果是map集合是无法进行属性复制
            //   copyProperties(源， 目标)
            //2: apache  BeanUtils 类 可以进map集合属性复制
            //   copyProperties(目标， 源)
            try {
                T highlight = JSON.parseObject(JSON.toJSONString(map), dtoclz);
                BeanUtils.copyProperties(highlight, target);
            } catch (Exception e) {
                log.warn("[高亮搜索] 拷贝属性失败", e);
            }
            list.add(target);
        }

        return new PageImpl<>(list, pageable, searchHits.getTotalHits());
    }

    //fields: title subTitle summary
    private Map<String, String> highlightFieldsCopy(Map<String, List<String>> map, String... fields) {
        Map<String, String> mm = new HashMap<>();
        //title:  "有娃必看，<span style='color:red;'>广州</span>长隆野生动物园全攻略"
        //subTitle: "<span style='color:red;'>广州</span>长隆野生动物园"
        //summary: "如果要说动物园，楼主强烈推荐带娃去<span style='color:red;'>广州</span>长隆野生动物园
        //title subTitle summary
        for (String field : fields) {
            List<String> hfs = map.get(field);
            if (hfs != null && !hfs.isEmpty()) {
                //获取高亮显示字段值, 因为是一个数组, 所有使用string拼接

                StringBuilder sb = new StringBuilder();
                for (String hf : hfs) {
                    sb.append(hf);
                }
                mm.put(field, sb.toString());  //使用map对象将所有能替换字段先缓存, 后续统一替换
            }
        }
        return mm;
    }
}
