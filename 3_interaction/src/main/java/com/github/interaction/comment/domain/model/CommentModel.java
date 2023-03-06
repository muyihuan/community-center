package com.github.interaction.comment.domain.model;

import com.github.interaction.comment.app.commet.enums.CommentVisibilityEnum;
import com.github.interaction.comment.app.commet.enums.ContentTypeEnum;
import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import com.github.interaction.comment.domain.enums.CommentStateEnum;
import com.github.interaction.comment.domain.repo.model.CommentDO;
import lombok.Data;

import java.util.Date;

/**
 * 评论模型.
 *
 * @author yanghuan
 */
@Data
public class CommentModel {

    /***********************************************/
    /********************基础属性*********************/
    /***********************************************/


    /**
     * 评论的唯一标识.
     */
    private Long commentId;

    /**
     * 评论的作者.
     */
    private String commentUid;

    /**
     * 内容类型：文本、图片、语音.
     */
    private ContentTypeEnum contentType;

    /**
     * 用户填写的内容.
     */
    private ContentInfo content;

    /**
     * 创建时间.
     */
    private Date createTime;


    /***********************************************/
    /******************评论树关系属性******************/
    /***********************************************/


    /**
     * 评论宿主的类型.
     */
    private TopicTypeEnum topicType;

    /**
     * 评论宿主的ID.
     */
    private Long topicId;

    /**
     * 被回复的评论ID.
     */
    private Long repliedCommentId;

    /**
     * 主评论的ID.
     */
    private Long rootCommentId;


    /***********************************************/
    /*******************可见性属性********************/
    /***********************************************/


    /**
     * 0:正常，1:不可用(删除).
     */
    private CommentStateEnum status;

    /**
     * 可见范围：0:全部人可见，1:仅自己可见.
     */
    private CommentVisibilityEnum visibility;


    /***********************************************/
    /********************扩展属性*********************/
    /***********************************************/


    /**
     * 热度 热度越高越受欢迎 整数增长(点赞、回复量等...或组合).
     */
    private Long heatScore;

    /**
     * 额外信息 例如：用户昵称头像、评论皮肤等.
     */
    private ExtraInfo extraInfo;

    public TextInfo getTextInfo() {
        if(content == null) {
            return null;
        }

        return content.getTextInfo();
    }

    public ImageInfo getImageInfo() {
        if(content == null) {
            return null;
        }

        return content.getImageInfo();
    }

    public AudioInfo getAudioInfo() {
        if(content == null) {
            return null;
        }

        return content.getAudioInfo();
    }

    public static CommentModel transformCommentDO(CommentDO commentDO) {
        if(commentDO == null) {
            return null;
        }

        CommentModel commentModel = new CommentModel();
        commentModel.setCommentId(commentDO.getCommentId());
        commentModel.setCommentUid(commentDO.getCommentUid());
        commentModel.setContentType(ContentTypeEnum.getByCode(commentDO.getContentType()));
//        commentModel.setContent(.fromJson(commentDO.getContent(), ContentInfo.class));
        commentModel.setCreateTime(commentDO.getCreateTime());
        commentModel.setTopicType(TopicTypeEnum.getByCode(commentDO.getTopicBizType()));
        commentModel.setTopicId(commentDO.getTopicBizId());
        commentModel.setRepliedCommentId(commentDO.getRepliedCommentId());
        commentModel.setRootCommentId(commentDO.getRootCommentId());
        commentModel.setStatus(CommentStateEnum.getByCode(commentDO.getStatus()));
        commentModel.setVisibility(CommentVisibilityEnum.getByCode(commentDO.getVisibility()));
        commentModel.setHeatScore(commentDO.getHeatScore());
//        commentModel.setExtraInfo(.fromJson(commentDO.getExtraInfo(), ExtraInfo.class));
        return commentModel;
    }
}
