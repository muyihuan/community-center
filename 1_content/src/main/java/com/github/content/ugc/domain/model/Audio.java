package com.github.content.ugc.domain.model;

import com.github.content.ugc.domain.enums.UgcTypeEnum;
import lombok.Data;

/**
 * 音频内容.
 *
 * @author yanghuan
 */
@Data
public class Audio implements Content {

    /**
     * 音频地址.
     */
    private String audioUrl;

    /**
     * 音频长度(秒).
     */
    private Integer audioLength;

    /**
     * 音频大小.
     */
    private Integer audioSize;

    /**
     * 兼容老代码.
     */
    private String extra;

    @Override
    public UgcTypeEnum getUgcType() {
        return UgcTypeEnum.AUDIO;
    }
}
