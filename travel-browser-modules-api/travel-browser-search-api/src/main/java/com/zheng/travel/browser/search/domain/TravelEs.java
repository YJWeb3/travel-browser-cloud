package com.zheng.travel.browser.search.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * 游记搜索对象
 */
@Getter
@Setter
@Document(indexName = TravelEs.INDEX_NAME)
public class TravelEs implements Serializable {
    public static final String INDEX_NAME = "travel";
    //@Field 每个文档的字段配置（类型、是否分词、是否存储、分词器 ）
    @Id
    @Field(type = FieldType.Long)
    private Long id;  //游记id
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", type = FieldType.Text)
    private String title;  //游记标题
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", type = FieldType.Text)
    private String summary; //游记简介
}