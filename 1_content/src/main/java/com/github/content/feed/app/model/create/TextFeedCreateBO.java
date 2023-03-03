package com.github.content.feed.app.model.create;

import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 文字feed特有信息.
 *
 * @author yanghuan
 */
@Setter
@Getter
public class TextFeedCreateBO extends AbstractFeedCreateBO {

    public TextFeedCreateBO() {
        super(FeedContentTypeEnum.TEXT);
    }
}