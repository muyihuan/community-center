package com.github.content.ugc.domain.model;

import com.github.content.ugc.domain.enums.UgcTypeEnum;

/**
 * 纯内容.
 *
 * @author yanghuan
 */
public interface Content {

    /**
     * 对应ugc的类型.
     *
     * @return ugc的类型.
     */
    UgcTypeEnum getUgcType();
}


