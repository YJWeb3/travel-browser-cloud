package com.zheng.travel.browser.search.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * 攻略搜索对象
 */
@Getter
@Setter
@Document(indexName = StrategyEs.INDEX_NAME)
public class StrategyEs implements Serializable {

    public static final String INDEX_NAME = "strategy";

    //@Field 每个文档的字段配置（类型、是否分词、是否存储、分词器 ）
    @Id
    @Field(type = FieldType.Long)
    private Long id;  //攻略id
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", type = FieldType.Text)
    private String title;  //攻略标题
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", type = FieldType.Text)
    private String subTitle;  //攻略标题
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", type = FieldType.Text)
    private String summary; //攻略简介
}