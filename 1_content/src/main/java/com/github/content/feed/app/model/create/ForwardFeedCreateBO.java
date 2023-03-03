package com.github.content.feed.app.model.create;

import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 转发feed特有信息.
 *
 * @author yanghuan
 */
@Setter
@Getter
public class ForwardFeedCreateBO extends AbstractFeedCreateBO {

    /**
     * 被转发feed的id.
     */
    private Long forwardFeedId;

    /**
     * 用户发布的音频地址.
     */
    private String audioUrl;

    /**
     * 用户发布的音频时长.
     */
    private Integer audioLength;

    /**
     * 用户发布的音频地址.
     */
    private List<String> imageUrl;

    public ForwardFeedCreateBO() {
        super(FeedContentTypeEnum.FORWARD);
    }
}
