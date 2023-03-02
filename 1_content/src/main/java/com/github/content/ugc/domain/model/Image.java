package com.github.content.ugc.domain.model;

import com.github.content.ugc.domain.enums.UgcTypeEnum;
import lombok.Data;

/**
 * 图片内容.
 *
 * @author yanghuan
 */
@Data
public class Image implements Content {

    /**
     * 兼容老代码，不要使用.
     */
    @Deprecated
    private Integer dbId;

    /**
     * 图片地址.
     */
    private String imageUrl;

    /**
     * 对应小图地址.
     */
    private String smallImageUrl;

    /**
     * 兼容老代码.
     */
    private Long groupId;

    /**
     * 兼容老代码.
     */
    private Integer isForward;

    @Override
    public UgcTypeEnum getUgcType() {
        return UgcTypeEnum.IMAGE;
    }
}
