package com.github.content.feed.app.model.create;

import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 链接feed特有信息.
 *
 * @author yanghuan
 */
@Setter
@Getter
public class LinkFeedCreateBO extends AbstractFeedCreateBO {

    /**
     * 链接的标题.
     */
    private String linkTitle;

    /**
     * 链接的描述.
     */
    private String linkDesc;

    /**
     * 链接的icon.
     */
    private String linkIcon;

    /**
     * 跳转地址.
     */
    private String url;

    public LinkFeedCreateBO() {
        super(FeedContentTypeEnum.LINK);
    }
}
