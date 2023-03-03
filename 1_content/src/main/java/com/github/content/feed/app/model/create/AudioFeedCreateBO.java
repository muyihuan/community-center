package com.github.content.feed.app.model.create;

import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 音频feed特有信息.
 *
 * @author yanghuan
 */
@Setter
@Getter
public class AudioFeedCreateBO extends AbstractFeedCreateBO {

    /**
     * 音频地址.
     */
    private String audioUrl;

    /**
     * 音频长度(秒)
     */
    private Integer audioLength;

    /**
     * 音频文件大小(字节)
     */
    private Integer audioSize;

    public AudioFeedCreateBO() {
        super(FeedContentTypeEnum.AUDIO);
    }
}
