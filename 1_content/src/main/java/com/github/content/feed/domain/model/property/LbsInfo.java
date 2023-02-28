package com.github.content.feed.domain.model.property;

import lombok.Data;

/**
 * 坐标信息
 * @author yanghuan
 */
@Data
public class LbsInfo {

    /**
     * 经度
     */
    private Double lo;

    /**
     * 纬度
     */
    private Double la;
}
