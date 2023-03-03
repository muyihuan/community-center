package com.github.content.ugc.domain.model;

import com.github.content.ugc.domain.enums.UgcTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 视频内容.
 *
 * @author yanghuan
 */
@Getter
@Setter
public class Video implements Content {

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

    @Override
    public UgcTypeEnum getUgcType() {
        return UgcTypeEnum.VIDEO;
    }
}
