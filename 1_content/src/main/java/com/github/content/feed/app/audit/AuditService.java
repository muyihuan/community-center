package com.github.content.feed.app.audit;

import com.github.content.feed.app.audit.enums.FeedAuditResultEnum;
import com.github.content.feed.domains.FeedDomainService;
import com.github.content.feed.domains.enums.FeedSystemPrivilegeEnum;
import com.github.content.feed.domains.model.FeedSourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * feed的审核和审核回调处理
 *
 * 时序图：
 * feed服务            审核服务            数美(机审核)           审核后台(人审)
 *    \                  \                  \                     \
 *    \ ----提交审核-----> \ ----提交审核-----> \                     \
 *    \                  \ ---------------同步到后台--------------> \
 *    \                  \ <---审核结果-----  \                     \
 *    \                  \                  \                     \
 *    \ <---机审结果-----  \                                        \
 *    \                  \                                        \
 *    \                  \ <---------------审核结果---------------- \
 *    \                  \                                        \
 *    \ <---人审结果-----  \                                        \
 *
 * @author yanghuan
 */
@Service
public class AuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    @Autowired
    private FeedDomainService feedService;

    /**
     * 审核回调统一处理
     */
    public void auditCallback(Long feedId, FeedAuditResultEnum auditResult) {
        // 1.是否是正常回调
        if(feedId == null || auditResult == null) {
            LOGGER.info("act=auditCallback auditResult is null feedId={}", feedId);
        }

        // lock
        try {
            FeedSourceModel feedSource = feedService.getFeed(feedId);
            if(feedSource == null) {
                LOGGER.error("act=auditCallback feedSource is null feedId={} auditResult={}", feedId, auditResult);
                return;
            }

            // 合法性校验，被自见的feed就不再关心审核了
            if(feedSource.getSystemPrivilege() == FeedSystemPrivilegeEnum.FEED_SELF_VIEW) {
                return;
            }

            // 2.幂等校验 = systemPrivilege 为 DEF 表示已经审核成功了，所以不需要再处理
            if(auditResult == FeedAuditResultEnum.MACHINE_PASS) {
                if(feedSource.getSystemPrivilege() == FeedSystemPrivilegeEnum.UGC_SELF_VIEW) {
                    LOGGER.info("act=auditCallback systemPrivilege is not UGC_SELF_VIEW feedId={} auditResult={}", feedId, auditResult);
                }
                else {
                    auditSuccTodo(feedSource);
                }
                // systemPrivilege 为 DEF 表示已经审核成功了，所以不需要再处理
                return;
            }

            // 3.审核结果解析
            if(auditResult == FeedAuditResultEnum.PEOPLE_PASS) {
                // 4.更新状态
                feedService.openFeedUgc(feedId);

                {
                    auditSuccTodo(feedSource);
                }
            }
            else if(auditResult == FeedAuditResultEnum.REJECT) {
                // 4.更新状态
                feedService.deleteFeed(feedId);
            }
            else if(auditResult == FeedAuditResultEnum.REJECT_SELF_VIEW) {
                // 4.更新状态
                feedService.hideFeed(feedId);
            }
            else if(auditResult == FeedAuditResultEnum.MACHINE_REJECT) {
                // 4.更新状态
                feedService.hideFeedUgc(feedId);
            }
        }
        finally {
            // unlock
        }

        // 5.deal other
    }

    /**
     * 提交审核
     */
    public void auditFeed(long feedId, Object feedCreateModel) {

        LOGGER.info("act=sendFeedAuditReport end feedId={}", feedId);
    }

    private void auditSuccTodo(FeedSourceModel feedSource) {
    }
}
