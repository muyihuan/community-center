package com.github.interaction.comment.domain.model;

import lombok.Data;

/**
 * 评论语音信息
 * @author yanghuan
 */
@Data
public class AudioInfo {

    /**
     * url地址
     */
    private String audioUrl;

    /**
     * 长度，秒
     */
    private Integer audioLength;
}
