package com.github.content.feed.app.audit;

import com.github.content.feed.app.audit.enums.FeedAuditResultEnum;
import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.domain.FeedDomainService;
import com.github.content.feed.domain.model.FeedModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * feed的审核和审核回调处理.
 *
 *  时序图：
 *
 *    feed服务            审核服务            三方(机审核)           审核后台(人审)
 *       \                  \                  \                     \
 *       \ ----提交审核-----> \ ----提交审核-----> \                     \
 *       \                  \ ---------------同步到后台------------->  \
 *       \                  \ <---审核结果-----  \                     \
 *       \                  \                  \                     \
 *       \ <---机审结果-----  \                                        \
 *       \                  \                                        \
 *       \                  \ <---------------审核结果---------------- \
 *       \                  \                                        \
 *       \ <---人审结果-----  \                                        \
 *
 * @author yanghuan
 */
@Service
public class AuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    @Autowired
    private FeedDomainService feedDomainService;

    /**
     * 提交审核
     */
    public void auditFeed(long feedId, AbstractFeedCreateBO feedCreateBO) {

        LOGGER.info("act=sendFeedAuditReport end feedId={}", feedId);
    }

    /**
     * 审核回调统一处理
     */
    public void auditCallback(Long feedId, FeedAuditResultEnum auditResult) {
        // lock
        try {
            FeedModel feedSource = feedDomainService.getFeed(feedId);
            if(feedSource == null) {
                LOGGER.error("act=auditCallback feedSource is null feedId={} auditResult={}", feedId, auditResult);
                return;
            }

            // 1.合法性/参数校验
            // 2.幂等校验
            // 3.审核结果解析
            // 4.更新状态
            // 5.处理
            // 6.deal other
        }
        finally {
            // unlock
        }
    }
}
