package com.github.content.feed.app.processor.impl;

import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.app.model.create.ForwardFeedCreateBO;
import com.github.content.feed.domain.FeedDomainService;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import com.github.content.feed.domain.model.FeedModel;
import com.github.content.feed.exception.FeedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 转发feed创建特有处理.
 *
 * @author yanghuan
 */
@Service
public class ForwardCreateProcessor extends AbstractEmptyCreateProcessor {

    @Autowired
    private FeedDomainService feedDomainService;

    @Override
    public void beforeCreateFeed(AbstractFeedCreateBO feedCreateModel) {
        ForwardFeedCreateBO forwardFeedCreateBO = (ForwardFeedCreateBO) feedCreateModel;
        if (StringUtils.isEmpty(feedCreateModel.getTextContent()) && StringUtils.isEmpty(forwardFeedCreateBO.getAudioUrl()) && StringUtils.isEmpty(forwardFeedCreateBO.getImageUrl())) {
            feedCreateModel.setTextContent( "转发了这条动态");
        }
    }

    @Override
    public void afterCreateFeed(long feedId, AbstractFeedCreateBO feedCreateModel) {
        FeedModel feedModel = feedDomainService.getFeed(feedId);
        if(feedModel == null) {
            throw new FeedException();
        }

        Long originFeedId = ((ForwardFeedCreateBO) feedCreateModel).getForwardFeedId();
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.FORWARD;
    }

    @Override
    public int order() {
        return 0;
    }
}
