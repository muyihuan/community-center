package com.github.content.feed.app.model.create;

import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 视频feed特有信息.
 *
 * @author yanghuan
 */
@Setter
@Getter
public class VideoFeedCreateBO extends AbstractFeedCreateBO {

    /**
     * 视频播放地址.
     */
    private String videoUrl;

    /**
     * 视频封面地址.
     */
    private String coverUrl;

    /**
     * 视频播放长度.
     */
    private Integer videoLength;

    /**
     * 视频高度.
     */
    private Integer videoHeight;

    /**
     * 视频宽度.
     */
    private Integer videoWidth;

    public VideoFeedCreateBO() {
        super(FeedContentTypeEnum.VIDEO);
    }
}
