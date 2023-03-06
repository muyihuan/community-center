package com.github.interaction.comment.domain.repo.query;

import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 查询条件.
 *
 * @author yanghuan
 */
@Data
public class QueryCondition {

    /**
     * 查询类型.
     */
    private QueryType queryType;

    /**
     * 评论宿主.
     */
    private TopicTypeEnum topicType;

    /**
     * 评论宿主ID.
     */
    private Long topicId;

    /**
     * 评论作者uid列表.
     */
    private List<String> commentUidList;

    /**
     * 分页使用.
     */
    private Long lastId;

    /**
     * 获取数量，目前最多支持拿30个，需要拿更多和相应进行开发沟通.
     */
    private Integer count;
}
