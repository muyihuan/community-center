package com.github.content.feed.domain.repo;

import com.github.content.feed.domain.enums.FeedPrivilegeEnum;
import com.github.content.feed.domain.enums.FeedStateEnum;
import com.github.content.feed.domain.repo.model.FeedDO;

import java.util.List;

/**
 * feed存储/计算/缓存处理.
 *
 * @author yanghuan
 */
public interface FeedRepository {

    /**
     * 创建feed.
     */
    void createFeed(FeedDO feedSource);

    /**
     * 更新feed状态.
     */
    void updateFeedState(Long feedId, FeedStateEnum state);

    /**
     * 更新feed可见范围.
     */
    void updateFeedPrivilege(Long feedId, FeedPrivilegeEnum targetPrivilege);

    /**
     * 更新feed话题信息.
     */
    void updateFeedTags(Long feedId, String tags);

    /**
     * 获取feed信息.
     */
    FeedDO getFeedById(Long feedId);

    /**
     * 批量获取feed信息.
     */
    List<FeedDO> batchGetFeedByIds(List<Long> feedIds);
}
