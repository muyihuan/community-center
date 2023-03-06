package com.github.interaction.comment.app.commet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import lombok.Data;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 批量查询多个评论宿主下的评论.
 *
 * @author yanghuan
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchTopicCommentsAndCountResult {

    /**
     * key   : topicType_topicId.
     * value : 评论列表.
     */
    private Map<String, CommentsAndCountBO> result;

    public BatchTopicCommentsAndCountResult() {
        setResult(new HashMap<>());;
    }

    /**
     * 添加评论宿主的评论列表.
     */
    public void setTopicCommentList(TopicTypeEnum topicType, Long topicId, CommentsAndCountBO commentsAndCountBO) {
        result.put(topicType + "_" + topicId, commentsAndCountBO);
    }

    /**
     * 获取评论宿主的评论列表.
     */
    public CommentsAndCountBO getTopicCommentList(TopicTypeEnum topicType, Long topicId) {
        if(MapUtils.isEmpty(result)) {
            return null;
        }

        return result.get(topicType + "_" + topicId);
    }
}
