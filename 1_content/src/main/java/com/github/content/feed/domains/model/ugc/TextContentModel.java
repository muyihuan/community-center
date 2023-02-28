package com.github.content.feed.domains.model.ugc;

import lombok.Data;

import java.util.List;

/**
 * 文本内容模型
 * @author yanghuan
 */
@Data
public class TextContentModel {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 状态内容
     */
    private String content;

    /**
     * 状态类型
     */
    private Integer type;

    /**
     * 状态里面的@信息
     */
    private List<AtInfo> atInfo;
}
