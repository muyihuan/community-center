package com.github.content.ugc.domain.model;

import com.github.content.ugc.domain.enums.UgcTypeEnum;
import lombok.Data;

/**
 * 文字内容.
 *
 * @author yanghuan
 */
@Data
public class Text implements Content {

    /**
     * 文字.
     */
    private String text;

    /**
     * 0:签名 1:feed
     */
    private Integer fromType;

    @Override
    public UgcTypeEnum getUgcType() {
        return UgcTypeEnum.TEXT;
    }
}
