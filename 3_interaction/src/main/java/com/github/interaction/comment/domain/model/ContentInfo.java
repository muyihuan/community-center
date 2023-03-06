package com.github.interaction.comment.domain.model;

import lombok.Data;

/**
 * 评论内容信息.
 *
 * @author yanghuan
 */
@Data
public class ContentInfo {

    /**
     * 文字信息
     */
    private TextInfo textInfo;

    /**
     * 图片内容
     */
    private ImageInfo imageInfo;

    /**
     * 语音内容
     */
    private AudioInfo audioInfo;
}
