package com.github.content.feed.domain.model.ugc;

import lombok.Data;

/**
 * 音频信息.
 *
 * @author yanghuan
 */
@Data
public class AudioContent {

    /**
     * 用户uid
     */
    private String uid;

    /**
     * url地址
     */
    private String audioUrl;

    /**
     * 长度，秒
     */
    private Integer audioLength;

    /**
     * 文件大小，字节
     */
    private Integer audioSize;
}
