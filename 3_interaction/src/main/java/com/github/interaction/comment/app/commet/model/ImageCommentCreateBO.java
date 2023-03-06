package com.github.interaction.comment.app.commet.model;

import com.github.interaction.comment.app.commet.enums.ContentTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图片评论创建
 * @author yanghuan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageCommentCreateBO extends AbstractCommentCreateBO {

    public ImageCommentCreateBO() {
        super(ContentTypeEnum.IMAGE);
    }

    /**
     * 图片url地址
     */
    private String imageUrl;

    /**
     * 大图url地址
     */
    private String imageLargeUrl;

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 高度
     */
    private Integer height;
}