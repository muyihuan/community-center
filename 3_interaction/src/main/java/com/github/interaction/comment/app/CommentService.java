package com.github.interaction.comment.app;

import com.github.interaction.comment.app.audit.AuditService;
import com.github.interaction.comment.app.commet.enums.*;
import com.github.interaction.comment.app.commet.model.*;
import com.github.interaction.comment.app.commet.processor.CreateProcessorManager;
import com.github.interaction.comment.domain.CommentCounterService;
import com.github.interaction.comment.domain.CommentDomainService;
import com.github.interaction.comment.domain.enums.CommentStateEnum;
import com.github.interaction.comment.domain.model.CommentModel;
import com.github.interaction.comment.domain.model.TopicCommentListMap;
import com.github.interaction.comment.domain.repo.query.QueryType;
import com.github.interaction.comment.exception.CommentException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 评论业务服务
 * @author yanghuan
 */
@Service
public class CommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);

    private static ExecutorService executorService = null;

    @Autowired
    private CommentDomainService commentDomainService;

    @Autowired
    private CommentCounterService commentCounterService;

    @Autowired
    private CreateProcessorManager createProcessorManager;

    @Autowired
    private AuditService commentAuditService;

    /**
     * 文字长度限制.
     */
    private static final Integer TEXT_LENGTH_LIMIT = 250;

    /**
     * 创建评论.
     *
     * @param comment 评论信息.
     */
    public CreateCommentResult create(AbstractCommentCreateBO comment) {
        CreateCommentResult result = new CreateCommentResult();
        if(comment == null) {
            result.setSucceed(false);
            result.setFailCode(CreateFailCodeEnum.COMMENT_REQUEST_PARAM_ERROR.getCode());
            result.setFailMsg("参数为空");
            return result;
        }

        if(StringUtils.isEmpty(comment.getUid())) {
            result.setSucceed(false);
            result.setFailCode(CreateFailCodeEnum.USER_IS_NULL.getCode());
            result.setFailMsg("评论作者不可以为空");
            return result;
        }

        if(comment.getTopicType() == null || comment.getTopicId() == null) {
            result.setSucceed(false);
            result.setFailCode(CreateFailCodeEnum.COMMENT_REQUEST_PARAM_ERROR.getCode());
            result.setFailMsg("评论TopicType和TopicId不可以为空");
            return result;
        }

        if(StringUtils.isNotBlank(comment.getTextContent())) {
            if(!commentAuditService.syncAuditCommentText(comment.getUid(), comment.getTextContent())) {
                result.setSucceed(false);
                result.setFailCode(CreateFailCodeEnum.COMMENT_CONTENT_AUDIT_REJECT.getCode());
                result.setFailMsg("您发布的内容，有违规信息，请重新编辑后发送");
                return result;
            }
        }

        if(comment.getContentType() == ContentTypeEnum.TEXT) {
            TextCommentCreateBO textComment = (TextCommentCreateBO) comment;
            if(StringUtils.isEmpty(textComment.getTextContent())) {
                result.setSucceed(false);
                result.setFailCode(CreateFailCodeEnum.COMMENT_REQUEST_PARAM_ERROR.getCode());
                result.setFailMsg("未填写内容");
                return result;
            }
            else if(textComment.getTextContent().length() > TEXT_LENGTH_LIMIT) {
                result.setSucceed(false);
                result.setFailCode(CreateFailCodeEnum.COMMENT_CONTENT_OVER_LIMIT.getCode());
                result.setFailMsg("评论内容过长");
                return result;
            }
        }
        else if(comment.getContentType() == ContentTypeEnum.IMAGE) {
            ImageCommentCreateBO imageComment = (ImageCommentCreateBO) comment;
            if(StringUtils.isEmpty(imageComment.getImageLargeUrl())) {
                result.setSucceed(false);
                result.setFailCode(CreateFailCodeEnum.COMMENT_REQUEST_PARAM_ERROR.getCode());
                result.setFailMsg("内容不可以为空");
                return result;
            }
        }
        else if(comment.getContentType() == ContentTypeEnum.AUDIO) {
            AudioCommentCreateBO audioComment = (AudioCommentCreateBO) comment;
            if(StringUtils.isEmpty(audioComment.getAudioUrl())) {
                result.setSucceed(false);
                result.setFailCode(CreateFailCodeEnum.COMMENT_REQUEST_PARAM_ERROR.getCode());
                result.setFailMsg("内容不可以为空");
                return result;
            }
        }
        else {
            result.setSucceed(false);
            result.setFailCode(CreateFailCodeEnum.COMMENT_REQUEST_PARAM_ERROR.getCode());
            result.setFailMsg("不支持发布该类型内容");
            return result;
        }

        // 前置处理.
        createProcessorManager.beforeCreateComment(comment);

        Long commentId = commentDomainService.createComment(comment);

        // 后置处理.
        createProcessorManager.afterCreateComment(commentId, comment);

        result.setSucceed(true);
        result.setCommentId(commentId);
        return result;
    }

    /**
     * 批量通过评论宿主查询评论列表.
     *
     * @param user 查询的用户.
     * @param param 查询参数.
     * @return 评论列表.
     */
    public List<CommentBO> queryCommentTreeByTopic(String user, QueryTopicCommentListParam param) {
        if(param == null) {
            return null;
        }

        BatchTopicCommentListResult batchTopicCommentListResult = batchQueryCommentTreeByTopic(user, Collections.singletonList(param));
        if(batchTopicCommentListResult == null) {
            return null;
        }

        return batchTopicCommentListResult.getTopicCommentList(param.getTopicType(), param.getTopicId());
    }

    /**
     * 批量通过评论宿主查询评论列表和评论数.
     *
     * @param user 查询的用户.
     * @param paramList 参数列表.
     * @return 评论列表.
     */
    public BatchTopicCommentsAndCountResult batchQueryCommentTreeAndCountByTopic(String user, List<QueryTopicCommentListParam> paramList) {
        if(CollectionUtils.isEmpty(paramList)) {
            return null;
        }

        // 1.加载评论数
        Map<TopicTypeEnum, Map<Long, Long>> counterMap = new ConcurrentHashMap<>(paramList.size());
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            Map<TopicTypeEnum, List<QueryTopicCommentListParam>> topicTypeMap = paramList.stream().collect(Collectors.groupingBy(QueryTopicCommentListParam::getTopicType));
            for(Map.Entry<TopicTypeEnum, List<QueryTopicCommentListParam>> entry : topicTypeMap.entrySet()) {
                if(CollectionUtils.isEmpty(entry.getValue())) {
                    continue;
                }

                Map<Long, Long> topicCommentCount = batchGetCommentCount(user, entry.getKey(), entry.getValue().stream().map(QueryTopicCommentListParam::getTopicId).collect(Collectors.toList()));
                counterMap.put(entry.getKey(), topicCommentCount);
            }
        }, executorService);

        // 2.查询评论
        BatchTopicCommentListResult topicCommentList = batchQueryCommentTreeByTopic(user, paramList);
        if(topicCommentList == null) {
            return null;
        }

        // 3.等待一下
        try {
            future.get(100, TimeUnit.MILLISECONDS);
        }
        catch (Exception e) {
        }

        BatchTopicCommentsAndCountResult result = new BatchTopicCommentsAndCountResult();
        for(QueryTopicCommentListParam param : paramList) {
            CommentsAndCountBO commentsAndCountBO = new CommentsAndCountBO();
            commentsAndCountBO.setCommentBOList(topicCommentList.getTopicCommentList(param.getTopicType(), param.getTopicId()));
            if(MapUtils.isNotEmpty(counterMap) && MapUtils.isNotEmpty(counterMap.get(param.getTopicType()))) {
                commentsAndCountBO.setAllCommentsCount(counterMap.get(param.getTopicType()).get(param.getTopicId()));
            }
            result.setTopicCommentList(param.getTopicType(), param.getTopicId(), commentsAndCountBO);
        }

        return result;
    }

    /**
     * 批量通过评论宿主查询评论列表-倒序
     * @param user 查询的用户
     * @param paramList 查询参数
     * @return 评论列表
     */
    public BatchTopicCommentListResult batchQueryCommentTreeByTopic(String user, List<QueryTopicCommentListParam> paramList) {
        if(CollectionUtils.isEmpty(paramList)) {
            return null;
        }

        TopicCommentListMap topicCommentListMap = commentDomainService.batchQueryCommentTreeByTopic(paramList, QueryType.TIME_SORT_DESC);
        if(topicCommentListMap == null) {
            return null;
        }

        BatchTopicCommentListResult result = new BatchTopicCommentListResult();
        // 过滤自见的评论，自见的评论只能作者可见
        for(QueryTopicCommentListParam param : paramList) {
            List<CommentModel> commentModelList = topicCommentListMap.getTopicCommentList(param.getTopicType(), param.getTopicId());
            if(CollectionUtils.isEmpty(commentModelList)) {
                continue;
            }

            commentModelList.removeIf(comment -> filterVisibility(user, comment));

            result.setTopicCommentList(param.getTopicType(), param.getTopicId(), commentModelList.stream().map(this::transformCommentModel).collect(Collectors.toList()));
        }

        return result;
    }

    /**
     * 批量通过评论宿主查询评论列表.
     *
     * @param user 查询的用户.
     * @param param 查询参数.
     * @return 评论列表.
     */
    public List<CommentBO> queryCommentTreeByTopicAsc(String user, QueryTopicCommentListParam param) {
        if(param == null) {
            return null;
        }

        TopicCommentListMap topicCommentListMap = commentDomainService.batchQueryCommentTreeByTopic(Collections.singletonList(param), QueryType.TIME_SORT_ASC);
        if(topicCommentListMap == null) {
            return null;
        }

        List<CommentModel> commentModelList = topicCommentListMap.getTopicCommentList(param.getTopicType(), param.getTopicId());
        if(CollectionUtils.isEmpty(commentModelList)) {
            return Collections.emptyList();
        }

        commentModelList.removeIf(comment -> filterVisibility(user, comment));

        return commentModelList.stream().map(this::transformCommentModel).collect(Collectors.toList());
    }

    /**
     * 查询评论.
     *
     * @param commentId 评论ID.
     * @return 评论信息.
     * 注意： 已经删除的评论，不会再让其他业务拿到.
     */
    public CommentBO getComment(Long commentId) {
        CommentModel commentModel = commentDomainService.getComment(commentId);

        // 已经删除的评论，不会再让其他业务拿到
        if(commentModel == null || commentModel.getStatus() == CommentStateEnum.DEL) {
            return null;
        }

        return transformCommentModel(commentModel);
    }

    /**
     * 获取评论数.
     *
     * @param topicType 评论宿主类型.
     * @param topicId 评论宿主ID.
     */
    public Long getCommentCount(String user, TopicTypeEnum topicType, Long topicId) {
        Map<Long, Long> commentCountMap = commentCounterService.batchGetCommentCount(user, topicType, Collections.singletonList(topicId));
        if(MapUtils.isEmpty(commentCountMap)) {
            return 0L;
        }

        return commentCountMap.get(topicId);
    }

    /**
     * 获取评论数.
     *
     * @param topicType 评论宿主类型.
     * @param topicIdList 评论宿主ID列表.
     */
    public Map<Long, Long> batchGetCommentCount(String user, TopicTypeEnum topicType, List<Long> topicIdList) {
        return commentCounterService.batchGetCommentCount(user, topicType, topicIdList);
    }

    /**
     * 删除评论.
     *
     * @param commentId 评论ID.
     */
    public void deleteComment(Long commentId) {
        commentDomainService.deleteComment(commentId);

        executorService.execute(() -> {
            CommentModel commentModel = commentDomainService.getComment(commentId);
            if(commentModel == null) {
                LOGGER.error("act=deleteCommentAfter fail commentId={}", commentId);
            }
        });
    }

    /**
     * 系统删除评论.
     *
     * @param commentId 评论ID.
     */
    public void deleteCommentBySystem(Long commentId, String operator) {
        commentDomainService.deleteComment(commentId);

        executorService.execute(() -> {
            CommentModel commentModel = commentDomainService.getComment(commentId);
            if(commentModel == null) {
                LOGGER.error("act=deleteCommentAfter fail commentId={}", commentId);
            }
        });

        LOGGER.info("act=deleteCommentBySystem end commentId={} operator={}", commentId, operator);
    }

    /**
     * 隐藏评论.
     *
     * @param commentId 评论ID.
     */
    public void hideComment(Long commentId) {
        commentDomainService.hideComment(commentId);
    }

    /**
     * 公开评论.
     *
     * @param commentId 评论ID.
     */
    public void openComment(Long commentId) {
        commentDomainService.openComment(commentId);
    }

    /**
     * 复活评论.
     *
     * @param commentId 评论ID.
     */
    public void reviveComment(Long commentId) {
        commentDomainService.reviveComment(commentId);
    }

    /**
     * 获取锚点评论.
     */
    public List<CommentBO> getAnchorCommentList(String fromUid, Long commentId, AnchorTypeEnum anchorType) {
        if(anchorType == null) {
            throw new CommentException();
        }

        CommentModel comment = commentDomainService.getComment(commentId);
        if(comment == null) {
            LOGGER.info("act=getAnchorCommentList fail comment == null fromUid={} commentId={} anchorType={}", fromUid, commentId, anchorType);
            return Collections.emptyList();
        }

        String showCommentUid = "";
        String toUid = null;
        List<CommentModel> commentModelList = null;
        if(anchorType == AnchorTypeEnum.SELF) {
            showCommentUid = comment.getCommentUid();
            commentModelList = new ArrayList<>(1);
            commentModelList.add(comment);
        }
        else if(anchorType == AnchorTypeEnum.COMMENT_TOPIC) {
            showCommentUid = comment.getCommentUid();
            toUid = comment.getCommentUid();
            commentModelList = commentDomainService.queryCommentTreeByTopicAndUsers(comment.getTopicType(), comment.getTopicId(), 100, Arrays.asList(fromUid, toUid));
        }
        else if(anchorType == AnchorTypeEnum.REPLIED_COMMENT) {
            showCommentUid = fromUid;
            toUid = comment.getCommentUid();
            if(comment.getRepliedCommentId() == null || comment.getRepliedCommentId() <= 0) {
                LOGGER.info("act=getAnchorCommentList fail repliedCommentId == null fromUid={} commentId={} anchorType={}", fromUid, commentId, anchorType);
                commentModelList = Collections.emptyList();
            }
            else {
                commentModelList = commentDomainService.queryCommentTreeByTopicAndUsers(comment.getTopicType(), comment.getTopicId(), 100, Arrays.asList(fromUid, toUid));
            }
        }
        else {
            throw new RuntimeException("暂不支持该种类型操作");
        }

        if(CollectionUtils.isEmpty(commentModelList)) {
            LOGGER.info("act=getAnchorCommentList fail commentModelList is empty fromUid={} commentId={} anchorType={}", fromUid, commentId, anchorType);
            return Collections.emptyList();
        }

        commentModelList.removeIf(commentModel -> filterVisibility(fromUid, commentModel));

        List<CommentModel> result = new ArrayList<>();
        // 被回复的评论ID
        Set<Long> repliedCommentIds = new HashSet<>();
        // 检测每条评论，是否符合规则
        for (CommentModel commentModel : commentModelList) {
            String authorId = commentModel.getCommentUid();
            String repliedUid = "";
            if(commentModel.getRepliedCommentId() != null && commentModel.getRepliedCommentId() > 0) {
                repliedUid = commentModel.getExtraInfo().getRepliedInfo().getUid();
            }

            // 1.两个人互相回复的可以看
            boolean isFromUidRepliedToUid = StringUtils.equals(fromUid, authorId) && StringUtils.equals(toUid, repliedUid);
            boolean isToUidRepliedFromUid = StringUtils.equals(toUid, authorId) && StringUtils.equals(fromUid, repliedUid);
            if (isFromUidRepliedToUid || isToUidRepliedFromUid) {
                repliedCommentIds.add(commentModel.getRepliedCommentId());
                result.add(commentModel);
                continue;
            }

            // 2.同上也是为了获取互相回复的评论
            if(repliedCommentIds.contains(commentModel.getCommentId())) {
                result.add(commentModel);
                continue;
            }

            // 3.可以全部展示的用户评论
            if (StringUtils.equals(showCommentUid, authorId)) {
                result.add(commentModel);
            }
        }

        // 查的时候是反序的，返回时候要正序
        Collections.reverse(result);

        return result.stream().map(this::transformCommentModel).collect(Collectors.toList());
    }

    private CommentBO transformCommentModel(CommentModel commentModel) {
        if(commentModel == null) {
            return null;
        }

        return null;
    }

    /**
     * 可见性过滤
     */
    private boolean filterVisibility(String user, CommentModel comment) {
        if(comment == null) {
            return true;
        }

        if(comment.getVisibility() == CommentVisibilityEnum.COMMENT_SELF_VIEW && !StringUtils.equals(user, comment.getCommentUid())) {
            return true;
        }

        return false;
    }
}
