package com.zheng.travel.browser.search.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * 目的地搜索对象
 */
@Getter
@Setter
@Document(indexName = DestinationEs.INDEX_NAME)
public class DestinationEs implements Serializable {
    public static final String INDEX_NAME = "destination";

    @Id
    //@Field 每个文档的字段配置（类型、是否分词、是否存储、分词器 ）
    @Field(type = FieldType.Long)
    private Long id;  //目的地id

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", type = FieldType.Text)
    private String info;
}