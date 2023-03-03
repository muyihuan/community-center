package com.github.content.feed.app.model.update;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 更新feed话题信息参数.
 *
 * @author yanghuan
 */
@Setter
@Getter
public class UpdateFeedTagsParam {

    /**
     * 是谁要删除更新，没有传空.
     */
    private String uid;

    /**
     * 要删除的feed的ID.
     */
    private Long feedId;

    /**
     * 更新为哪些话题.
     */
    private List<Long> newTagList;
}
