package com.github.content.feed.app.processor.impl;

import com.github.content.feed.app.audit.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 链接类型处理器
 * @author yanghuan
 */
@Service
public class LinkCreateProcessor extends AbstractEmptyCreateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    @Override
    public void beforeCreateFeed(AbstractFeedCreateBO feedCreateModel) {
        LinkFeedCreateBO linkFeedCreateBO = (LinkFeedCreateBO) feedCreateModel;

        LinkOptInfoBO linkOptInfo = linkFeedCreateBO.getLinkOptInfo();
        if(linkOptInfo != null) {
            return;
        }

        String url = linkFeedCreateBO.getUrl();
        if(StringUtils.isEmpty(url)) {
            return;
        }

        linkOptInfo = new LinkOptInfoBO();
        linkFeedCreateBO.setLinkOptInfo(linkOptInfo);

        // wanba统一跳转
        boolean isWanbaJump = url.startsWith("wanba:");
        if(isWanbaJump) {
            linkOptInfo.setUnifyJump(url);

            LOGGER.info("act=linkFeedBeforeCreateFeed isWanBaJump feedUid={} url={}", feedCreateModel.getUid(), url);
        }
        else {
            linkOptInfo.setUnifyJump(UnifyJumpUtils.generate("webview", "inner", ImmutableMap.of("url", url, "webFullScreen", 1)));

            LOGGER.info("act=linkFeedBeforeCreateFeed feedUid={} url={}", feedCreateModel.getUid(), url);
        }
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.LINK;
    }

    @Override
    public FeedSourceTypeEnum matchSourceType() {
        return FeedSourceTypeEnum.LINK;
    }

    @Override
    public int order() {
        return 0;
    }
}
