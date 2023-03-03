package com.github.content.feed.app.model.create;

import lombok.Getter;
import lombok.Setter;

/**
 * 地理位置坐标信息.
 *
 * @author yanghuan
 */
@Setter
@Getter
public class LbsBO {

    /**
     * 经度
     */
    protected Double longitude;

    /**
     * 纬度
     */
    protected Double latitude;
}
