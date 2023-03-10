package com.github.content.feed.domain.model.ugc;

import lombok.Data;

/**
 * 链接信息.
 *
 * @author yanghuan
 */
@Data
public class LinkContent {

    /**
     * 链接的标题.
     */
    private String title;

    /**
     * 链接的内容描述.
     */
    private String desc;

    /**
     * 链接内容的图片.
     */
    private String icon;

    /**
     * 链接的跳转地址.
     */
    private String url;
}
