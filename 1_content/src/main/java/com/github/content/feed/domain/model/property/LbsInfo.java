package com.github.content.feed.domain.model.property;

import lombok.Data;

/**
 * 坐标信息.
 *
 * @author yanghuan
 */
@Data
public class LbsInfo {

    /**
     * 经度.
     */
    protected Double longitude;

    /**
     * 纬度.
     */
    protected Double latitude;
}
