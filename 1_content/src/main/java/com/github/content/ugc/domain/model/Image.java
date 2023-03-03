package com.github.content.ugc.domain.model;

import com.github.content.ugc.domain.enums.UgcTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 图片内容.
 *
 * @author yanghuan
 */
@Getter
@Setter
public class Image implements Content {

    /**
     * 图片地址.
     */
    private String imageUrl;

    @Override
    public UgcTypeEnum getUgcType() {
        return UgcTypeEnum.IMAGE;
    }
}
