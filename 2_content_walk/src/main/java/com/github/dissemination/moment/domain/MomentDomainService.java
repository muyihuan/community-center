package com.github.dissemination.moment.domain;

import com.github.content.feed.exception.FeedException;
import com.github.content.ugc.domain.enums.ContentCarrierTypeEnum;
import com.github.dissemination.mine.infra.FriendFacade;
import com.github.dissemination.moment.domain.config.MomentConfig;
import com.github.dissemination.moment.domain.repo.MomentRepository;
import com.github.dissemination.moment.domain.repo.model.MomentRecordDO;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 朋友圈领域能力.
 *
 * @author yanghuan
 */
@Service
public class MomentDomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MomentDomainService.class);

    private static ExecutorService executorService = null;

    @Autowired
    private MomentRepository momentRepository;

    @Autowired
    private FriendFacade friendFacade;

    @Autowired
    private ActiveMomentCollector activeMomentCollector;

    /**
     * 推送动态给好友.
     *
     * @param uid 用户ID.
     * @param carrierType 内容载体类型.
     * @param contentCarrierId 内容ID.
     */
    public void distributeContentToFriends(String uid, ContentCarrierTypeEnum carrierType, Long contentCarrierId) {
        // 官方账号不在这下发 太多了 等用户登陆的时候再下发
        Set<String> officialAccounts = MomentConfig.get().getOfficialAccounts();
        if(CollectionUtils.isNotEmpty(officialAccounts) && officialAccounts.contains(uid)) {
            LOGGER.info("act=distributeContentToFriends is officialUser uid={} carrierType={} contentCarrierId={}", uid, carrierType, contentCarrierId);
            return;
        }

        // 先同步到自己的收件箱，保证自己可以立即看到
        distributeContentToSelf(uid, carrierType, contentCarrierId);

        // 同步到好友的收件箱
        executorService.submit(() -> {
            try {
                List<String> friends = friendFacade.getUserFriends(uid);
                if(CollectionUtils.isEmpty(friends)) {
                    LOGGER.info("act=distributeContentToFriends friends isEmpty uid={} carrierType={} contentCarrierId={}", uid, carrierType, contentCarrierId);
                    return;
                }

                friends = filterNotActiveUser(friends);
                if(CollectionUtils.isEmpty(friends)) {
                    LOGGER.info("act=distributeContentToFriends friends isEmpty uid={} carrierType={} contentCarrierId={}", uid, carrierType, contentCarrierId);
                    return;
                }

                for(String friendUid : friends) {
                    try {
                        MomentRecordDO momentRecord = new MomentRecordDO();
                        momentRecord.setUid(friendUid);
                        momentRecord.setFriendUid(uid);
                        momentRecord.setContentCarrierType(carrierType.getCode());
                        momentRecord.setContentCarrierId(contentCarrierId);
                        momentRepository.saveMomentRecord(momentRecord);

                        activeMomentCollector.collect(friendUid);

                    }
                    catch (Exception e) {
                        LOGGER.info("act=saveMomentRecord error friendUid={} carrierType={} contentCarrierId={}", friendUid, carrierType, contentCarrierId, e);
                    }
                }
            }
            catch (Exception e) {
                LOGGER.info("act=saveMomentRecord error uid={} carrierType={} contentCarrierId={}", uid, carrierType, contentCarrierId, e);
            }
        });
    }

    /**
     * 推送动态给自己.
     *
     * @param uid 用户ID.
     * @param carrierType 内容载体类型.
     * @param contentCarrierId 内容ID.
     */
    public void distributeContentToSelf(String uid, ContentCarrierTypeEnum carrierType, Long contentCarrierId) {
        Set<String> officialAccounts = MomentConfig.get().getOfficialAccounts();
        // 官方账号不下发
        if(CollectionUtils.isNotEmpty(officialAccounts) && officialAccounts.contains(uid)) {
            LOGGER.info("act=distributeContentToFriends is officialUser uid={} carrierType={} contentCarrierId={}", uid, carrierType, contentCarrierId);
            return;
        }

        // 先同步到自己的收件箱，保证自己可以立即看到
        MomentRecordDO momentSelfRecord = new MomentRecordDO();
        momentSelfRecord.setUid(uid);
        momentSelfRecord.setFriendUid(uid);
        momentSelfRecord.setContentCarrierType(carrierType.getCode());
        momentSelfRecord.setContentCarrierId(contentCarrierId);
        activeMomentCollector.collect(uid);
    }

    /**
     * 删除动态.
     *
     * @param uid 用户ID.
     * @param carrierType 内容载体类型.
     * @param contentCarrierId 内容ID.
     */
    public void deleteContent(String uid, ContentCarrierTypeEnum carrierType, Long contentCarrierId) {
        if(StringUtils.isEmpty(uid) || carrierType == null || contentCarrierId == null) {
            throw new FeedException();
        }

        List<String> friends = friendFacade.getUserFriends(uid);
        if(CollectionUtils.isEmpty(friends)) {
            LOGGER.info("act=deleteContent friends isEmpty uid={} carrierType={} contentCarrierId={}", uid, carrierType, contentCarrierId);
            return;
        }

        boolean existFeed = momentRepository.existFeed(uid, carrierType, contentCarrierId);
        if(!existFeed) {
            // 用来实现幂等，如果作者的朋友圈都不存在该feed，理论情况下好友的朋友圈必然也不存在
            return;
        }

        momentRepository.remMomentRecord(uid, carrierType, contentCarrierId);

        // 从该用户的好友朋友圈里删除动态
        executorService.submit(() -> {
            for(String friendUid : friends) {
                try {
                    momentRepository.remMomentRecord(friendUid, carrierType, contentCarrierId);
                }
                catch (Exception e) {
                    LOGGER.error("act=remMomentRecord error uid={} friendUid={} carrierType={} contentCarrierId={}", uid, friendUid, carrierType, contentCarrierId, e);
                }
            }
        });
    }

    /**
     * 查询朋友圈动态.
     *
     * @param uid 用户ID.
     * @param lastId 分页使用.
     * @return 动态列表.
     */
    public List<Long> queryFeeds(String uid, ContentCarrierTypeEnum carrierType, Long lastId) {
        if(StringUtils.isEmpty(uid)) {
            throw new FeedException();
        }

        int count = 10;
        if(lastId == null || lastId == 0) {
            count = MomentConfig.get().getFirstPageCount();
        }

        return momentRepository.queryMomentContents(uid, carrierType, lastId, count);
    }

    /**
     * 删除好友动态.
     *
     * @param uid 用户ID.
     * @param friendUid 好友用户ID.
     */
    public void deleteFriendFeeds(String uid, String friendUid) {
        if(StringUtils.equals(uid, friendUid)) {
            throw new FeedException();
        }

        momentRepository.delFriendContentFromMe(uid, friendUid);
    }

    /**
     * 获取用户朋友圈最老的动态.
     *
     * @return 最老的动态.
     */
    public Long getUserMomentOldestRecordId(String uid) {
        if(StringUtils.isEmpty(uid)) {
            throw new FeedException();
        }

        return momentRepository.getOldestContent(uid);
    }

    /**
     * 定期清理朋友圈动态.
     */
    public void cleanMoments() {
        long startTime = System.currentTimeMillis();

        MomentConfig momentConfig = MomentConfig.get();
        int max = momentConfig.getCleanMomentMaxCount();
        int count = 0;
        while(count <= max) {
            count ++;

            List<String> cleanMoments = activeMomentCollector.pop(100);
            if(CollectionUtils.isEmpty(cleanMoments)) {
                LOGGER.info("act=cleanMoments cleanMoments isEmpty");
                break;
            }

            for(String uid : cleanMoments) {
                try {
                    momentRepository.cleanOverage(uid, momentConfig.getMomentThreshold());
                }
                catch (Exception e) {
                    LOGGER.error("act=cleanMoments error uid={}", uid, e);
                }
            }
        }

        LOGGER.info("act=cleanMoments end cost={} count={}", System.currentTimeMillis() - startTime, count);
    }

    /**
     * 过滤掉非活跃用户.
     *
     * @param uidList 用户id.
     * @return 活跃用户id集合.
     */
    private List<String> filterNotActiveUser(List<String> uidList) {
        if(CollectionUtils.isEmpty(uidList)) {
            return Collections.emptyList();
        }

        long now = System.currentTimeMillis();
        List<String> result = new ArrayList<>();

        List<List<String>> partition = Lists.partition(uidList, 50);
        for (List<String> uidListPartition : partition) {
            Map<String, Map<String, Object>> userInfoMap = null;
            if(MapUtils.isEmpty(userInfoMap)) {
                continue;
            }

            for (Map.Entry<String, Map<String, Object>> entry : userInfoMap.entrySet()) {
                Map<String, Object> userInfo = entry.getValue();
                Long lastLoginTime = MapUtils.getLong(userInfo, "last_login_time");
                if(lastLoginTime == null) {
                    continue;
                }

                if (now < lastLoginTime) {
                    continue;
                }

                if (now - lastLoginTime <= TimeUnit.DAYS.toMillis(MomentConfig.get().getActiveUserLoginDurationDay())) {
                    result.add(entry.getKey());
                }
            }
        }

        return result;
    }
}
