package com.github.content.ugc.domain.model;

import com.github.content.ugc.domain.enums.UgcTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 音频内容.
 *
 * @author yanghuan
 */
@Getter
@Setter
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

    @Override
    public UgcTypeEnum getUgcType() {
        return UgcTypeEnum.AUDIO;
    }
}
