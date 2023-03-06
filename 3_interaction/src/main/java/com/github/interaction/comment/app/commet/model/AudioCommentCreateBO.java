package com.github.interaction.comment.app.commet.model;

import com.github.interaction.comment.app.commet.enums.ContentTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 音频评论创建.
 *
 * @author yanghuan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AudioCommentCreateBO extends AbstractCommentCreateBO {

    /**
     * url地址.
     */
    private String audioUrl;

    /**
     * 长度，秒.
     */
    private Integer audioLength;

    public AudioCommentCreateBO() {
        super(ContentTypeEnum.AUDIO);
    }
}
