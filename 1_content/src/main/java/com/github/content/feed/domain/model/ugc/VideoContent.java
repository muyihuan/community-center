package com.github.content.feed.domain.model.ugc;

import lombok.Data;

/**
 * 视频内容.
 *
 * @author edz
 */
@Data
public class VideoContent {

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
}
