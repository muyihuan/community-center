package com.github.content.ugc.domain.model;

import com.github.content.ugc.domain.enums.UgcTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 文字内容.
 *
 * @author yanghuan
 */
@Getter
@Setter
public class Text implements Content {

    /**
     * 文字.
     */
    private String text;

    @Override
    public UgcTypeEnum getUgcType() {
        return UgcTypeEnum.TEXT;
    }
}
