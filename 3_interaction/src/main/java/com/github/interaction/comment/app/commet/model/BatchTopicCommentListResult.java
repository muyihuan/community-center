package com.github.interaction.comment.app.commet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import lombok.Data;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量查询多个评论宿主下的评论.
 *
 * @author yanghuan
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchTopicCommentListResult {

    /**
     * key   : topicType_topicId.
     * value : 评论列表.
     */
    private Map<String, List<CommentBO>> result;

    public BatchTopicCommentListResult() {
        setResult(new HashMap<>());;
    }

    /**
     * 添加评论宿主的评论列表.
     */
    public void setTopicCommentList(TopicTypeEnum topicType, Long topicId, List<CommentBO> commentBOList) {
        result.put(topicType + "_" + topicId, commentBOList);
    }

    /**
     * 获取评论宿主的评论列表.
     */
    public List<CommentBO> getTopicCommentList(TopicTypeEnum topicType, Long topicId) {
        if(MapUtils.isEmpty(result)) {
            return null;
        }

        return result.get(topicType + "_" + topicId);
    }
}
