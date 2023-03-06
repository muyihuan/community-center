package com.github.interaction.comment.app.commet.model;

import com.github.interaction.comment.app.commet.enums.ContentTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文字评论创建
 * @author yanghuan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TextCommentCreateBO extends AbstractCommentCreateBO {

    public TextCommentCreateBO() {
        super(ContentTypeEnum.TEXT);
    }
}