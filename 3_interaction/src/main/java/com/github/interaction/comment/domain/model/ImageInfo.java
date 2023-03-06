package com.github.interaction.comment.domain.model;

import lombok.Data;

/**
 * 评论图片信息.
 *
 * @author yanghuan
 */
@Data
public class ImageInfo {

    /**
     * 大图url地址.
     */
    private String imageLargeUrl;

    /**
     * 宽度.
     */
    private Integer width;

    /**
     * 高度.
     */
    private Integer height;
}
