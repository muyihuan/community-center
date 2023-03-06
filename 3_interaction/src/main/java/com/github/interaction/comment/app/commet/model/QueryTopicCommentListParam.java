package com.github.interaction.comment.app.commet.model;

import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import lombok.Data;

/**
 * 评论宿主信息.
 *
 * @author yanghuan
 */
@Data
public class QueryTopicCommentListParam {

    /**
     * 议题业务类型
     */
    private TopicTypeEnum topicType;

    /**
     * 议题的业务唯一ID
     */
    private Long topicId;

    /**
     * 分页使用
     */
    private Long lastCommentId;

    /**
     * 获取数量，目前最多支持拿30个，需要拿更多和相应进行开发沟通
     */
    private Integer count;
}
