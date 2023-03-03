package com.github.content.feed.domain.model.ugc;

import lombok.Data;

/**
 * 转发信息.
 *
 * @author yanghuan
 */
@Data
public class RepostContent {

    /**
     * 根feed的id
     */
    private Long rootFeedId;

    /**
     * 本feed的文字信息.
     */
    private TextContent text;

    /**
     * 本feed的语音信息.
     */
    private AudioContent audio;

    /**
     * 本feed的图片信息.
     */
    private ImageContent image;
}
