package com.github.content.feed.app.model.update;

import com.github.content.feed.domain.enums.FeedPrivilegeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 修改feed可见范围参数.
 *
 * @author yanghuan
 */
@Setter
@Getter
public class ChangeFeedPrivilegeParam {

    /**
     * 是谁要修改.
     */
    private String uid;

    /**
     * feed的ID.
     */
    private Long feedId;

    /**
     * 可见范围修改为～.
     */
    private FeedPrivilegeEnum targetPrivilege;
}
