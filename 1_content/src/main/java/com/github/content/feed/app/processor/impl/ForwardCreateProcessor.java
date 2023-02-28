package com.github.content.feed.app.processor.impl;

import com.github.content.feed.domains.enums.FeedContentTypeEnum;
import com.github.content.feed.domains.enums.FeedSourceTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanghuan
 * @version 1.0.0
 * @Description 转发feed创建处理
 * @createTime 2021年05月10日 17:44:00
 */
@Service
public class ForwardCreateProcessor extends AbstractEmptyCreateProcessor {

    private ForwardService forwardService = BeanFactory.get(ForwardService.class);

    @Autowired
    private FeedDomainService feedService;

    @Override
    public void beforeCreateFeed(AbstractFeedCreateBO feedCreateModel) {
        ForwardFeedCreateBO forwardFeedCreateBO = (ForwardFeedCreateBO) feedCreateModel;
        if (StringUtils.isEmpty(feedCreateModel.getTextContent()) && StringUtils.isEmpty(forwardFeedCreateBO.getAudioUrl()) && StringUtils.isEmpty(forwardFeedCreateBO.getImageUrl())) {
            feedCreateModel.setTextContent( "转发了这条动态");
        }
    }

    @Override
    public void afterCreateFeed(long feedId, AbstractFeedCreateBO feedCreateModel) {
        FeedSourceModel feedSource = feedService.getFeed(feedId);
        if(feedSource == null) {
            throw new FeedException(FeedContentError.FEED_NOT_FOUND);
        }

        Long originFeedId = ((ForwardFeedCreateBO) feedCreateModel).getForwardFeedId();
        forwardService.saveForwardFeed(feedId, originFeedId);
        forwardService.saveForwardInfo(feedSource, originFeedId);
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.FORWARD;
    }

    @Override
    public FeedSourceTypeEnum matchSourceType() {
        return FeedSourceTypeEnum.REPOST;
    }

    @Override
    public int order() {
        return 0;
    }
}
