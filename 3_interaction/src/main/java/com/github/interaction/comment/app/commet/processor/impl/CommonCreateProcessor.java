package com.github.interaction.comment.app.commet.processor.impl;

import com.github.interaction.comment.app.audit.AuditService;
import com.github.interaction.comment.app.audit.enums.AuditPolicyEnum;
import com.github.interaction.comment.app.audit.enums.CommentAuditResultEnum;
import com.github.interaction.comment.app.commet.enums.CommentVisibilityEnum;
import com.github.interaction.comment.app.commet.model.AbstractCommentCreateBO;
import com.github.interaction.comment.app.commet.model.SystemParam;
import com.github.interaction.comment.app.commet.processor.CreateProcessor;
import com.github.interaction.comment.domain.CommentCounterService;
import com.github.interaction.comment.domain.CommentDomainService;
import com.github.interaction.comment.infra.IpFacade;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.ExecutorService;

/**
 * 主要公共的创建处理器.
 *
 * @author yanghuan
 */
@Service
public class CommonCreateProcessor implements CreateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonCreateProcessor.class);

    private static ExecutorService executorService = null;

    @Autowired
    private CommentDomainService commentDomainService;

    @Autowired
    private AuditService commentAuditService;

    @Autowired
    private CommentCounterService commentCounterService;

    @Autowired
    private IpFacade ipFacade;

    private static final String LOCALHOST = "127.0.0.1";

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void beforeCreateComment(AbstractCommentCreateBO commentCreateBO) {
        // 是否是系统发布的评论
        boolean isSystemComment = BooleanUtils.isTrue(commentCreateBO.getIsSystemComment());
        if(!isSystemComment) {
            AuditPolicyEnum auditPolicy = null;
            // 先审后发的feed需要先自见，等审核通过后才会展示给全部人
            if(auditPolicy == AuditPolicyEnum.AUDIT_FIRST) {
                commentCreateBO.setVisibility(CommentVisibilityEnum.COMMENT_SELF_VIEW);
            }
        }

        // 如果用户ip没有传递，查询填补
        if(commentCreateBO.getSystemParam() == null || StringUtils.isEmpty(commentCreateBO.getSystemParam().getUserIp())) {
            String ip = ipFacade.getUserIp(commentCreateBO.getUid());
            if(StringUtils.isNotEmpty(ip)) {
                SystemParam systemParam = commentCreateBO.getSystemParam() == null ? new SystemParam() : commentCreateBO.getSystemParam();
                systemParam.setUserIp(ip);
                commentCreateBO.setSystemParam(systemParam);
            }
        }
        else if(StringUtils.equals(commentCreateBO.getSystemParam().getUserIp(), LOCALHOST)) {
            LOGGER.error("act=beforeCreateComment checkUserIp LOCALHOST");
        }
    }

    @Override
    public void afterCreateComment(Long commentId, AbstractCommentCreateBO commentCreateBO) {
        executorService.execute(() -> {
            try {
                // 预加载
                {
                    commentCounterService.batchGetCommentCount(commentCreateBO.getUid(), commentCreateBO.getTopicType(), Collections.singletonList(commentCreateBO.getTopicId()));
                }

                // 是否是系统发布的评论
                boolean isSystemComment = BooleanUtils.isTrue(commentCreateBO.getIsSystemComment());
                // 1.系统评论直接消息通知
                if(isSystemComment) {
//                    noticeService.notice(commentId);
                }
                else {
                    // 1.提交审核
                    boolean res = commentAuditService.asyncAuditComment(commentId, commentCreateBO);
                    if (!res) {
                        // 2.提交审核失败，先删除评论
                        commentDomainService.deleteComment(commentId);

                        LOGGER.info("act=submitAuditComment fail commentId={}", commentId);
                        throw new RuntimeException("提交审核异常");
                    }
                    else {
                        // 审核提交成功就认为机器审核通过了
                        commentAuditService.auditCallback(commentId, CommentAuditResultEnum.MACHINE_PASS, "");
                    }
                }
            }
            catch (Exception e) {
                LOGGER.error("act=afterCreateComment error commentId={}", commentId, e);
            }
        });
    }
}
