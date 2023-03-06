package com.github.interaction.comment.domain.model;

import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import org.apache.commons.collections.MapUtils;

import java.util.List;
import java.util.Map;

/**
 * 评论宿主 => 评论列表 关系.
 *
 * @author yanghuan.
 */
public class TopicCommentListMap {

    /**
     * key   : topicType_topicId.
     * value : 评论列表.
     */
    private Map<String, List<CommentModel>> result;

    public void setResult(Map<String, List<CommentModel>> result) {
        this.result = result;
    }

    public Map<String, List<CommentModel>> getResult() {
        return result;
    }

    /**
     * 获取评论宿主的评论列表
     */
    public List<CommentModel> getTopicCommentList(TopicTypeEnum topicType, Long topicId) {
        if(MapUtils.isEmpty(result)) {
            return null;
        }

        return result.get(topicType + "_" + topicId);
    }
}
