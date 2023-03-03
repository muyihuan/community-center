package com.github.content.feed.app.model.create;

import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import com.github.content.feed.domain.enums.FeedPrivilegeEnum;
import com.github.content.feed.domain.enums.FeedSourceTypeEnum;
import com.github.content.feed.domain.enums.FeedSystemPrivilegeEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * feed创建基础参数.
 *
 * @author yanghuan
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractFeedCreateBO {

    /**
     * 发布人ID.
     */
    protected String uid;

    /**
     * feed的类型.
     */
    @Setter(AccessLevel.NONE)
    protected FeedContentTypeEnum contentType;

    /**
     * @ 信息.
     */
    protected List<AtInfoBO> atInfo;

    /**
     * 可见范围.
     */
    protected FeedPrivilegeEnum privilege;

    /**
     * 系统可见范围，是系统侧设置的可见范围，优先级大于用户设置的可见范围.
     */
    protected FeedSystemPrivilegeEnum systemPrivilege;

    /**
     * 话题列表.
     */
    protected List<Long> tagIds;

    /**
     * 文字内容.
     */
    protected String textContent;

    /**
     * 地理位置信息.
     */
    protected LbsBO lbs;

    /**
     * 来源场景类型.
     */
    protected FeedSourceTypeEnum sourceType;

    /**
     * 来源场景对应业务的ID.
     */
    protected String sourceId;

    /**
     * 是否是系统产生的feed，true：系统产生的feed，不是用户创建的、false：用户创建的（默认）.
     */
    protected Boolean isSystemFeed;

    /**
     * 来源，例如广场、朋友圈、话题里发布的feed.
     */
    protected String sourceFrom;

    /**
     * feed扩展项.
     */
    protected ExtraInfoBO extraInfo;

    /**
     * 子类必须设置 contentType.
     *
     * @param contentType {@link FeedContentTypeEnum}.
     */
    public AbstractFeedCreateBO(FeedContentTypeEnum contentType) {
        this.contentType = contentType;
    }
}