package com.zheng.travel.browser.search.parser;

/**
 * ES 搜索类型解析器
 * 函数式接口: 这个接口必须有一个抽象方法, 并且只能有一个抽象方法
 * 使用 lambda 实现函数是接口, 这个 lambda 就可以理解为这个函数式接口的实现类 === 匿名内部类
 */
@FunctionalInterface
public interface ElasticsearchTypeParser<T> {

    T parse(Class<T> clazz, String id);
}
