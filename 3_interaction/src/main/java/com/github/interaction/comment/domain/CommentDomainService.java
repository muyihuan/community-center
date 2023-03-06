package com.github.interaction.comment.domain;

import com.github.infrastructure.json.JsonMapper;
import com.github.interaction.comment.app.commet.enums.CommentVisibilityEnum;
import com.github.interaction.comment.app.commet.enums.ContentTypeEnum;
import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import com.github.interaction.comment.app.commet.model.*;
import com.github.interaction.comment.domain.enums.CommentStateEnum;
import com.github.interaction.comment.domain.model.ExtraInfo;
import com.github.interaction.comment.domain.model.*;
import com.github.interaction.comment.domain.repo.CommentRepository;
import com.github.interaction.comment.domain.repo.TableIndexGenerator;
import com.github.interaction.comment.domain.repo.model.CommentDO;
import com.github.interaction.comment.domain.repo.query.QueryType;
import com.github.interaction.comment.exception.CommentException;
import com.github.interaction.comment.infra.IdGenerator;
import com.github.interaction.comment.infra.IpFacade;
import com.github.interaction.comment.infra.UserFacade;
import com.github.interaction.comment.infra.model.UserInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评论领域能力.
 * 
 * @author yanghuan
 */
@Service
public class CommentDomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentDomainService.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentCounterService commentCounterService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private IpFacade ipFacade;

    private static final String KEY_COMMENT_LIFECYCLE_CHANGE = "comment:life:change:";

    /**
     * 创建评论.
     * 
     * @param createComment 评论信息.
     * @return 评论唯一ID.
     */
    public Long createComment(AbstractCommentCreateBO createComment) {
        Long commentId = generateCommentId(createComment);

        CommentDO commentDO = new CommentDO();
        commentDO.setCommentId(commentId);
        commentDO.setCommentUid(createComment.getUid());
        commentDO.setContentType(createComment.getContentType().getType());

        ContentInfo contentInfo = new ContentInfo();
        if(StringUtils.isNotEmpty(createComment.getTextContent())) {
            TextInfo textInfo = new TextInfo();
            textInfo.setText(createComment.getTextContent());
            contentInfo.setTextInfo(textInfo);
        }

        if(createComment.getContentType() == ContentTypeEnum.TEXT) {
        }
        else if(createComment.getContentType() == ContentTypeEnum.IMAGE) {
            ImageCommentCreateBO imageComment = (ImageCommentCreateBO) createComment;
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setImageLargeUrl(imageComment.getImageLargeUrl());
            imageInfo.setHeight(imageComment.getHeight());
            imageInfo.setWidth(imageComment.getWidth());
            contentInfo.setImageInfo(imageInfo);
        }
        else if(createComment.getContentType() == ContentTypeEnum.AUDIO) {
            AudioCommentCreateBO audioComment = (AudioCommentCreateBO) createComment;
            AudioInfo audioInfo = new AudioInfo();
            audioInfo.setAudioUrl(audioComment.getAudioUrl());
            audioInfo.setAudioLength(audioComment.getAudioLength());
            contentInfo.setAudioInfo(audioInfo);
        }
        else {
            throw new CommentException();
        }

        commentDO.setContent(JsonMapper.toJson(contentInfo));
        commentDO.setTopicBizType(createComment.getTopicType().getType());
        commentDO.setTopicBizId(createComment.getTopicId());
        commentDO.setRepliedCommentId(createComment.getRepliedCommentId());
        commentDO.setRootCommentId(null);
        commentDO.setHeatScore(0L);
        commentDO.setStatus(CommentStateEnum.NORMAL.getCode());
        commentDO.setVisibility(createComment.getVisibility().getCode());

        ExtraInfo extraInfo = new ExtraInfo();
        {
            UserInfo userInfo = userFacade.getUserInfo(createComment.getUid());
            if(userInfo != null) {
                com.github.interaction.comment.domain.model.UserInfo authorInfo = new com.github.interaction.comment.domain.model.UserInfo();
                authorInfo.setUid(createComment.getUid());
                authorInfo.setUserName(userInfo.getUsername());
                authorInfo.setUserIcon(userInfo.getIcon());
                authorInfo.setGender(userInfo.getGender());
                extraInfo.setAuthorInfo(authorInfo);
            }

            if(createComment.getRepliedCommentId() != null && createComment.getRepliedCommentId() > 0) {
                CommentDO repliedComment = commentRepository.getComment(createComment.getRepliedCommentId());
                String extraInfoJson = repliedComment.getExtraInfo();
                if(StringUtils.isNotBlank(extraInfoJson)) {
                    ExtraInfo repliedCommentExtraInfo = JsonMapper.fromJson(extraInfoJson, ExtraInfo.class);
                    com.github.interaction.comment.domain.model.UserInfo repliedAuthorInfo = repliedCommentExtraInfo.getAuthorInfo();
                    extraInfo.setRepliedInfo(repliedAuthorInfo);
                }
            }

            SystemParam systemParam = createComment.getSystemParam();
            if(systemParam != null && StringUtils.isNotEmpty(systemParam.getUserIp())) {
                extraInfo.setIpHome(ipFacade.getIpHome(systemParam.getUserIp()));
            }
            else {
                if(BooleanUtils.isTrue(createComment.getIsSystemComment())) {
                    // 系统发布 并且 没有ip = 说明是死账号或者马甲号
                    extraInfo.setIpHome("北京");
                }
            }
        }
        commentDO.setExtraInfo(JsonMapper.toJson(extraInfo));

        // lock
        try {
            commentRepository.saveComment(commentDO);

            // 更新计数
            {
                if(createComment.getVisibility() == CommentVisibilityEnum.COMMENT_SELF_VIEW) {
                    commentCounterService.incrUserSelfCommentCount(createComment.getUid(), createComment.getTopicType(), createComment.getTopicId());
                }
                else {
                    commentCounterService.incrCommentCount(createComment.getTopicType(), createComment.getTopicId());
                }
            }

            LOGGER.info("act=createComment end commentId={}", commentId);
        }
        finally {
            // unlock
        }

        return commentId;
    }

    /**
     * 查询评论.
     *
     * @param commentId 评论ID.
     * @return 评论信息.
     */
    public CommentModel getComment(Long commentId) {
        if(commentId == null) {
            throw new CommentException();
        }

        CommentDO commentDO = commentRepository.getComment(commentId);
        if(commentDO == null) {
            return null;
        }

        return CommentModel.transformCommentDO(commentDO);
    }

    /**
     * 批量通过评论宿主查询评论列表.
     *
     * @param paramList 查询参数.
     * @return 评论列表.
     */
    public TopicCommentListMap batchQueryCommentTreeByTopic(List<QueryTopicCommentListParam> paramList, QueryType queryType) {
        TopicCommentListMap result = new TopicCommentListMap();

        Map<String, List<CommentDO>> queryCommentsResult = commentRepository.batchQueryCommentTreeByTopic(paramList, queryType);
        if(MapUtils.isEmpty(queryCommentsResult)) {
            return result;
        }

        Map<String, List<CommentModel>> topicCommentsMap = new HashMap<>(queryCommentsResult.size());
        for(Map.Entry<String, List<CommentDO>> entry : queryCommentsResult.entrySet()) {
            List<CommentDO> commentDOList = entry.getValue();
            if(CollectionUtils.isEmpty(commentDOList)) {
                topicCommentsMap.put(entry.getKey(), Collections.emptyList());
            }
            else {
                topicCommentsMap.put(entry.getKey(), commentDOList.stream().map(CommentModel::transformCommentDO).collect(Collectors.toList()));
            }
        }

        result.setResult(topicCommentsMap);
        return result;
    }

    /**
     * 通过评论宿主和评论作者列表查询评论列表.
     */
    public List<CommentModel> queryCommentTreeByTopicAndUsers(TopicTypeEnum topicType, Long topicId, Integer count, List<String> uidList) {
        List<CommentDO> commentDOList = commentRepository.queryCommentTreeByTopicAndUsers(topicType, topicId, count, uidList);
        if(CollectionUtils.isEmpty(commentDOList)) {
            return Collections.emptyList();
        }

        return commentDOList.stream().map(CommentModel::transformCommentDO).collect(Collectors.toList());
    }

    /**
     * 按热度查询评论列表.
     *
     * @param topicType 宿主类型.
     * @param topicId 宿主ID.
     * @param page 页.
     * @param pageSize 页数量.
     * @return 评论列表.
     */
    public List<CommentModel> queryCommentTreeByHeat(TopicTypeEnum topicType, Long topicId, Integer page, Integer pageSize) {
        if(topicType == null || topicId == null || page == null || pageSize == null) {
            throw new CommentException();
        }

        Integer offset = (page - 1) * pageSize;
        List<CommentDO> commentDOList = commentRepository.getTopicCommentByHeat(topicType.getType(), topicId, 0L, offset, pageSize);
        if(CollectionUtils.isEmpty(commentDOList)) {
            return Collections.emptyList();
        }

        return commentDOList.stream().map(CommentModel::transformCommentDO).collect(Collectors.toList());
    }

    /**
     * 删除评论.
     *
     * @param commentId 评论ID.
     */
    public void deleteComment(Long commentId) {
        if(commentId == null) {
            throw new CommentException();
        }

        // lock
        try {
            CommentDO comment = commentRepository.getComment(commentId);
            if(comment == null) {
                throw new CommentException();
            }

            if(comment.getStatus() == CommentStateEnum.DEL.getCode()) {
                return;
            }

            commentRepository.deleteComment(commentId);

            // 更新计数
            {
                if(comment.getVisibility() == CommentVisibilityEnum.COMMENT_SELF_VIEW.getCode()) {
                    commentCounterService.decrUserSelfCommentCount(comment.getCommentUid(), TopicTypeEnum.getByCode(comment.getTopicBizType()), comment.getTopicBizId());
                }
                else {
                    commentCounterService.decrCommentCount(TopicTypeEnum.getByCode(comment.getTopicBizType()), comment.getTopicBizId());
                }
            }

            LOGGER.info("act=deleteComment end commentId={}", commentId);
        }
        finally {
            // unlock
        }
    }

    /**
     * 隐藏评论.
     *
     * @param commentId 评论ID.
     */
    public void hideComment(Long commentId) {
        if(commentId == null) {
            throw new CommentException();
        }

        // lock
        try {
            CommentDO comment = commentRepository.getComment(commentId);
            if(comment == null || comment.getStatus() == CommentStateEnum.DEL.getCode()) {
                throw new CommentException();
            }

            if(comment.getVisibility() != CommentVisibilityEnum.DEF.getCode()) {
                throw new CommentException();
            }

            commentRepository.updateCommentVisibility(commentId, CommentVisibilityEnum.COMMENT_SELF_VIEW);

            // 更新计数
            {
                commentCounterService.decrCommentCount(TopicTypeEnum.getByCode(comment.getTopicBizType()), comment.getTopicBizId());
                commentCounterService.incrUserSelfCommentCount(comment.getCommentUid(), TopicTypeEnum.getByCode(comment.getTopicBizType()), comment.getTopicBizId());
            }

            LOGGER.info("act=hideComment end commentId={}", commentId);
        }
        finally {
            // unlock
        }
    }

    /**
     * 开放评论.
     *
     * @param commentId 评论ID.
     */
    public void openComment(Long commentId) {
        if(commentId == null) {
            throw new CommentException();
        }

        // lock
        try {
            CommentDO comment = commentRepository.getComment(commentId);
            if(comment == null || comment.getStatus() == CommentStateEnum.DEL.getCode()) {
                throw new CommentException();
            }

            if(comment.getVisibility() != CommentVisibilityEnum.COMMENT_SELF_VIEW.getCode()) {
                throw new CommentException();
            }

            commentRepository.updateCommentVisibility(commentId, CommentVisibilityEnum.DEF);

            // 更新计数
            {
                commentCounterService.incrCommentCount(TopicTypeEnum.getByCode(comment.getTopicBizType()), comment.getTopicBizId());
                commentCounterService.decrUserSelfCommentCount(comment.getCommentUid(), TopicTypeEnum.getByCode(comment.getTopicBizType()), comment.getTopicBizId());
            }

            LOGGER.info("act=openComment end commentId={}", commentId);
        }
        finally {
            // unlock
        }
    }

    /**
     * 复活评论.
     *
     * @param commentId 评论ID.
     */
    public void reviveComment(Long commentId) {
        if(commentId == null) {
            throw new CommentException();
        }

        // lock
        try {
            CommentDO comment = commentRepository.getComment(commentId);
            if(comment == null) {
                throw new CommentException();
            }

            if(comment.getStatus() != CommentStateEnum.DEL.getCode()) {
                throw new CommentException();
            }

            commentRepository.updateCommentStatus(commentId, CommentStateEnum.NORMAL);

            // 更新计数
            {
                commentCounterService.incrCommentCount(TopicTypeEnum.getByCode(comment.getTopicBizType()), comment.getTopicBizId());
            }

            LOGGER.info("act=reviveComment end commentId={}", commentId);
        }
        finally {
            // unlock
        }
    }

    /**
     * 通过主评论查询子评论.
     *
     * @param rootCommentId 主评论ID.
     * @return 评论列表.
     */
    public List<CommentModel> queryCommentTreeByRootComment(Long rootCommentId) {
        // todo 暂时不实现
        return null;
    }

    private Long generateCommentId(AbstractCommentCreateBO comment) {
        return idGenerator.generate(comment.getTopicType().getType(), Integer.parseInt(TableIndexGenerator.getTableIdx(comment.getTopicId())));
    }
}
