package com.github.content.feed.app.model.delete;

import lombok.Getter;
import lombok.Setter;

/**
 * 删除feed的参数.
 *
 * @author yanghuan
 */
@Setter
@Getter
public class DeleteFeedParam {

    /**
     * 是谁要删除.
     */
    private String uid;

    /**
     * 要删除的feed的ID.
     */
    private Long feedId;
}
