package com.github.content;

import com.github.content.ugc.domain.enums.ContentCarrierTypeEnum;

/**
 * 内容载体.
 * 1.动态.
 * 2.短视频.
 * 3.电影.
 * 4.其他.
 *
 * @author yanghuan
 */
public interface ContentCarrier {

    /**
     * 内容载体类型.
     *
     * @return 载体类型.
     */
    ContentCarrierTypeEnum getCarrierType();

    /**
     * 唯一ID.
     *
     * @return id.
     */
    Long getCarrierId();
}
