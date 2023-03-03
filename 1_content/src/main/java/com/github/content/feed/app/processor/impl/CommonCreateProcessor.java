package com.github.content.feed.app.processor.impl;

import com.github.content.feed.app.audit.AuditService;
import com.github.content.feed.app.audit.enums.FeedAuditResultEnum;
import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.domain.FeedDomainService;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import com.github.content.feed.domain.enums.FeedSystemPrivilegeEnum;
import com.github.content.feed.domain.model.FeedModel;
import com.github.content.feed.exception.FeedException;
import com.github.content.ugc.domain.UgcDomainService;
import com.github.content.ugc.domain.enums.ContentCarrierTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

/**
 * feed创建公共处理.
 *
 * @author yanghuan
 */
@Service
public class CommonCreateProcessor extends AbstractEmptyCreateProcessor {

    private static ExecutorService executor = null;

    @Autowired
    private FeedDomainService feedDomainService;

    @Autowired
    private UgcDomainService ugcDomainService;

    @Autowired
    private AuditService auditService;

    @Override
    public void beforeCreateFeed(AbstractFeedCreateBO feedCreateModel) {

    }

    @Override
    public void afterCreateFeed(long feedId, AbstractFeedCreateBO feedCreateModel) {
        FeedModel feedModel = feedDomainService.getFeed(feedId);
        if(feedModel == null) {
            throw new FeedException();
        }

        // 提交审核.
        auditService.auditFeed(feedId, feedCreateModel);

        // 创建ugc.
        Long ugcId = ugcDomainService.createUgc(feedCreateModel.getUid(), null, ContentCarrierTypeEnum.FEED, String.valueOf(feedId));

        executor.execute(() -> {
            // 如果该feed走的是先发后审，那么直接认为机器审核通过.
            if(feedModel.getSystemPrivilege() == FeedSystemPrivilegeEnum.DEF) {
                auditService.auditCallback(feedId, FeedAuditResultEnum.MACHINE_PASS);
            }

            // 其他.
        });
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return null;
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
