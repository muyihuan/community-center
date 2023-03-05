package com.github.dissemination.mine.app;

import com.github.content.feed.app.FeedService;
import com.github.content.feed.domain.enums.FeedPrivilegeEnum;
import com.github.content.feed.domain.enums.FeedSystemPrivilegeEnum;
import com.github.content.feed.domain.model.FeedModel;
import com.github.content.feed.exception.FeedException;
import com.github.content.ugc.domain.enums.ContentCarrierTypeEnum;
import com.github.dissemination.mine.domain.MineContentDomainService;
import com.github.dissemination.mine.domain.enums.IdentityType;
import com.github.dissemination.mine.domain.enums.PrivilegeType;
import com.github.dissemination.mine.domain.model.MineContentRecord;
import com.github.dissemination.mine.infra.FriendFacade;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 基于feed的个人动态业务.
 *
 * @author yanghuan
 */
@Service
public class MineFeedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MineFeedService.class);

    @Autowired
    private MineContentDomainService mineDomainService;

    @Autowired
    private FeedService feedService;

    @Autowired
    private FriendFacade friendFacade;

    /**
     * 浏览用户个人页.
     *
     * @param visitorUid 访问者用户ID.
     * @param beVisitedUid 被拜访者用户ID.
     * @param lastId,count 分页信息.
     * @return feed列表.
     */
    public List<Long> queryMineFeedList(String visitorUid, String beVisitedUid, Long lastId, Integer count) {
        if(StringUtils.isEmpty(visitorUid) || StringUtils.isEmpty(beVisitedUid)) {
            throw new FeedException();
        }

        IdentityType visitorIdentity;
        if(StringUtils.equals(visitorUid, beVisitedUid)) {
            visitorIdentity = IdentityType.MYSELF;
        }
        else {
            boolean isFriend = friendFacade.isFriend(visitorUid, beVisitedUid);
            if(isFriend) {
                visitorIdentity = IdentityType.FRIEND;
            }
            else {
                visitorIdentity = IdentityType.STRANGER;
            }
        }

        return mineDomainService.queryUserProfileFeeds(visitorIdentity, beVisitedUid, ContentCarrierTypeEnum.FEED, lastId, count);
    }

    /**
     * 用户发布动态
     */
    public void publishFeed(String uid, Long feedId) {
        if(StringUtils.isEmpty(uid) || feedId == null) {
            throw new FeedException();
        }

        FeedModel feed = feedService.getFeed(feedId);
        if(feed == null) {
            throw new FeedException();
        }

        MineContentRecord profileFeed = new MineContentRecord();
        profileFeed.setUid(uid);
        profileFeed.setContentCarrierType(ContentCarrierTypeEnum.FEED);
        profileFeed.setContentCarrierId(feedId);
        profileFeed.setPrivilege(transformFeedPrivilegeToProfilePrivilege(feed));
        mineDomainService.saveFeed(profileFeed);

        LOGGER.info("act=publishFeed uid={} feedId={}", uid, feedId);
    }

    /**
     * 用户删除动态
     */
    public void deleteFeed(String uid, Long contentCarrierId) {
        if(StringUtils.isEmpty(uid) || contentCarrierId == null) {
            throw new FeedException();
        }

        mineDomainService.deleteFeed(uid, ContentCarrierTypeEnum.FEED, contentCarrierId);

        LOGGER.info("act=deleteFeed uid={} contentCarrierId={}", uid, contentCarrierId);
    }

    /**
     * 把feed的可见范围转发为个人页的可见范围
     */
    private PrivilegeType transformFeedPrivilegeToProfilePrivilege(FeedModel feed) {
        if(feed.getSystemPrivilege() == FeedSystemPrivilegeEnum.FEED_SELF_VISIBLE || feed.getSystemPrivilege() == FeedSystemPrivilegeEnum.UGC_SELF_VISIBLE) {
            return PrivilegeType.SELF_VISIBLE;
        }

        if(feed.getPrivilege() == FeedPrivilegeEnum.FRIEND_VISIBLE) {
            return PrivilegeType.FRIEND_VISIBLE;
        }

        if(feed.getPrivilege() == FeedPrivilegeEnum.ALL_VISIBLE) {
            return PrivilegeType.ALL_VISIBLE;
        }

        if(feed.getPrivilege() == FeedPrivilegeEnum.STRANGER_VISIBLE) {
            return PrivilegeType.STRANGER_VISIBLE;
        }

        if(feed.getPrivilege() == FeedPrivilegeEnum.SELF_VISIBLE) {
            return PrivilegeType.SELF_VISIBLE;
        }

        return PrivilegeType.ALL_VISIBLE;
    }
}
