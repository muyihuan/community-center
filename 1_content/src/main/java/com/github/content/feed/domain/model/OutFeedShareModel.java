package com.github.content.feed.domain.model;

import lombok.Data;

import java.util.Map;

/**
 * @author yanghuan
 */
@Data
public class OutFeedShareModel {

    /**
     * 弹出类型  @link ShareWindowType
     */
    String shareWindowType;

    /**
     * 标题
     */
    String title;

    /**
     * 描述
     */
    String desc;

    /**
     * feed扩展字段
     */
    private Map<String, Object> extMap;
}
