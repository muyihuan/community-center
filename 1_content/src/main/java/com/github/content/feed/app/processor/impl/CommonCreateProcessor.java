package com.github.content.feed.app.processor.impl;

import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import com.github.content.feed.domain.enums.FeedSourceTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author yanghuan
 * @version 1.0.0
 * @Description 主要公共的创建处理器
 * @createTime 2021年05月10日 17:44:00
 */
@Service
public class CommonCreateProcessor extends AbstractEmptyCreateProcessor {

    private static ExecutorService executor = Metrics.wrap(AsyncExecutorFactory.getDefaultExecutor().getExecutor(), "feedCreate");

    @Autowired
    private AuditService auditService;
    @Autowired
    private FeedDomainService feedService;

    @Override
    public void beforeCreateFeed(AbstractFeedCreateBO feedCreateModel) {
        // @信息转换
        List<AtInfoBO> atInfos = feedCreateModel.getAtInfos();
        buildAtInfos(atInfos, feedCreateModel.getUid());
        feedCreateModel.setAtInfos(atInfos);
    }

    @Override
    public void afterCreateFeed(long feedId, AbstractFeedCreateBO feedCreateModel) {
        FeedSourceModel feedSource = feedService.getFeed(feedId);
        if(feedSource == null) {
            throw new FeedException(FeedContentError.FEED_NOT_FOUND);
        }

        // 提交审核
        auditService.auditFeed(feedId, feedCreateModel);

        executor.execute(() -> {
            // 如果该feed走的是先发后审，那么直接认为机器审核通过
            if(feedSource.getSystemPrivilege() == FeedSystemPrivilegeEnum.DEF) {
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
    public FeedSourceTypeEnum matchSourceType() {
        return null;
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    /**
     * 艾特信息转换
     */
    private void buildAtInfos(List<AtInfoBO> atInfosList, String fromUid) {

    }
}
