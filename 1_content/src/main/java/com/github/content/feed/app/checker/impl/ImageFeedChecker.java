package com.github.content.feed.app.checker.impl;

import com.github.content.feed.app.checker.CheckResult;
import com.github.content.feed.app.checker.Checker;
import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.app.model.create.ImageFeedCreateBO;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 图片类型feed特有校验.
 *
 * @author yanghuan
 */
@Service
public class ImageFeedChecker implements Checker {

    private static final int COUNT = 9;

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateBO) {
        ImageFeedCreateBO multiImageFeedCreate = (ImageFeedCreateBO) feedCreateBO;

        List<String> urlList = multiImageFeedCreate.getImageUrlList();
        if(CollectionUtils.isEmpty(urlList) || urlList.size() > COUNT) {
            return CheckResult.fail("图片feed图片数量等于0或者大于9");
        }

        return CheckResult.success();
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.IMAGE;
    }

    @Override
    public int order() {
        return 1;
    }
}
