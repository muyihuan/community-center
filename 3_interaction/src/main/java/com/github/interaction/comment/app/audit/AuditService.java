package com.github.interaction.comment.app.audit;

import com.github.interaction.comment.app.audit.enums.AuditPolicyEnum;
import com.github.interaction.comment.app.audit.enums.CommentAuditResultEnum;
import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import com.github.interaction.comment.app.commet.model.AbstractCommentCreateBO;
import com.github.interaction.comment.domain.CommentDomainService;
import com.github.interaction.comment.domain.model.CommentModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 评论审核服务.
 *
 * @author yanghuan
 */
@Service
public class AuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    @Autowired
    private CommentDomainService commentDomainService;

    /**
     * 同步审核文本 true：通过、false：拒绝
     */
    public boolean syncAuditCommentText(String uid, String message) {
        if(StringUtils.isBlank(message)) {
            return true;
        }

        return false;
    }

    /**
     * 异步提交审核
     */
    public boolean asyncAuditComment(Long commentId, AbstractCommentCreateBO commentCreateBO) {
        return true;
    }

    /**
     * 审核回调统一处理
     */
    public void auditCallback(Long commentId, CommentAuditResultEnum auditResult, String operator) {
        LOGGER.info("act=auditCallback commentId={} auditResult={} operator={}", commentId, auditResult, operator);

        CommentModel commentModel = commentDomainService.getComment(commentId);
        if(commentModel == null) {
            LOGGER.error("act=auditCallback fail commentModel == null commentId={} auditResult={}", commentId, auditResult);
            return;
        }

        if(auditResult == CommentAuditResultEnum.MACHINE_PASS) {
        }
        else if(auditResult == CommentAuditResultEnum.PEOPLE_PASS) {
        }
        else if(auditResult == CommentAuditResultEnum.MACHINE_REJECT || auditResult == CommentAuditResultEnum.PEOPLE_REJECT) {
        }
        else {
            throw new RuntimeException("不支持此种审核结果");
        }
    }

    /**
     * 获取评论业务的审核策略
     */
    public AuditPolicyEnum getAuditPolicy(String uid, TopicTypeEnum topicType, Long topicId) {
        return AuditPolicyEnum.AFTER_AUDIT;
    }
}
