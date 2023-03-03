package com.github.content.feed.domain.repo;

import com.github.content.feed.domain.enums.FeedPrivilegeEnum;
import com.github.content.feed.domain.enums.FeedStateEnum;
import com.github.content.feed.domain.repo.model.FeedDO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * feed存储/计算/缓存处理.
 *
 * @author yanghuan
 */
@Service
public class FeedRepository {

    /**
     * 创建feed.
     */
    public void createFeed(FeedDO feedSource) {

    }

    /**
     * 更新feed状态.
     */
    public void updateFeedState(Long feedId, FeedStateEnum state) {

    }

    /**
     * 更新feed可见范围.
     */
    public void updateFeedPrivilege(Long feedId, FeedPrivilegeEnum targetPrivilege) {

    }

    /**
     * 更新feed话题信息.
     */
    public void updateFeedTags(Long feedId, String tags) {

    }

    /**
     * 获取feed信息.
     */
    public FeedDO getFeedById(Long feedId) {
        return null;
    }

    /**
     * 批量获取feed信息.
     */
    public List<FeedDO> batchGetFeedByIds(List<Long> feedIds) {
        return null;
    }
}
