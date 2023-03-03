package com.github.content.feed.app.processor.impl;

import com.github.content.feed.app.audit.AuditService;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 链接feed创建特有处理.
 *
 * @author yanghuan
 */
@Service
public class LinkCreateProcessor extends AbstractEmptyCreateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.LINK;
    }

    @Override
    public int order() {
        return 0;
    }
}
