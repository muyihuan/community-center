package com.github.dissemination.mine.domain;

import com.github.content.feed.exception.FeedException;
import com.github.content.ugc.domain.enums.ContentCarrierTypeEnum;
import com.github.dissemination.mine.domain.enums.IdentityType;
import com.github.dissemination.mine.domain.model.MineContentRecord;
import com.github.dissemination.mine.domain.repo.MineContentRepository;
import com.github.dissemination.mine.domain.repo.model.MineContentDO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 个人动态页基础能力.
 *
 * @author yanghuan
 */
@Service
public class MineContentDomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MineContentDomainService.class);

    @Autowired
    private MineContentRepository mineContentRepository;

    /**
     * 获取用户个人动态列表.
     *
     * @param yourIdentity 你的身份.
     * @param toVisitUid 被拜访者ID.
     * @param maxId 分页信息.
     * @param count 获取数量.
     * @return feed列表.
     */
    public List<Long> queryUserProfileFeeds(IdentityType yourIdentity, String toVisitUid, ContentCarrierTypeEnum carrierType, Long maxId, Integer count) {
        if(yourIdentity == null || StringUtils.isEmpty(toVisitUid)) {
            throw new FeedException();
        }

        return mineContentRepository.queryUserFeeds(yourIdentity, toVisitUid, carrierType, maxId, count);
    }

    /**
     * 保存feed
     * @param profileFeed 个人页动态信息
     */
    public void saveFeed(MineContentRecord profileFeed) {
        if(profileFeed == null) {
            throw new FeedException();
        }

        MineContentDO mineContentDO = new MineContentDO();
        mineContentDO.setUid(profileFeed.getUid());
        mineContentDO.setContentCarrierType(profileFeed.getContentCarrierType().getCode());
        mineContentDO.setContentCarrierId(profileFeed.getContentCarrierId());
        mineContentDO.setPrivilege(profileFeed.getPrivilege().getCode());
        mineContentRepository.saveUserFeed(mineContentDO);
    }

    /**
     * 删除个人动态.
     *
     * @param uid 用户ID.
     * @param carrierType 内容载体类型.
     * @param contentCarrierId 内容载体ID.
     */
    public void deleteFeed(String uid, ContentCarrierTypeEnum carrierType, Long contentCarrierId) {
        if(carrierType == null || contentCarrierId == null) {
            throw new FeedException();
        }

        mineContentRepository.deleteUserFeed(uid, carrierType, contentCarrierId);
    }
}
