package com.github.dissemination.mine.domain.repo;

import com.github.content.ugc.domain.enums.ContentCarrierTypeEnum;
import com.github.dissemination.mine.domain.enums.IdentityType;
import com.github.dissemination.mine.domain.enums.PrivilegeType;
import com.github.dissemination.mine.domain.repo.model.MineContentDO;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 个人动态数据存储/计算/缓存处理.
 * @author yanghuan
 */
@Service
public class MineContentRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MineContentRepository.class);

    /**
     * 不同身份可见范围配置
     */
    private static final Map<IdentityType, List<PrivilegeType>> IDENTITY_PRIVILEGE_CONFIG = ImmutableMap.of(
            IdentityType.MYSELF, Arrays.asList(PrivilegeType.FRIEND_VISIBLE, PrivilegeType.ALL_VISIBLE, PrivilegeType.STRANGER_VISIBLE, PrivilegeType.SELF_VISIBLE),
            IdentityType.FRIEND, Arrays.asList(PrivilegeType.FRIEND_VISIBLE, PrivilegeType.ALL_VISIBLE),
            IdentityType.STRANGER, Arrays.asList(PrivilegeType.STRANGER_VISIBLE, PrivilegeType.ALL_VISIBLE),
            IdentityType.SYSTEM, Arrays.asList(PrivilegeType.FRIEND_VISIBLE, PrivilegeType.ALL_VISIBLE, PrivilegeType.STRANGER_VISIBLE, PrivilegeType.SELF_VISIBLE));

    /**
     * 保存个人页动态信息.
     *
     * @param mineContentDO 个人页记录信息.
     */
    public void saveUserFeed(MineContentDO mineContentDO) {

    }

    /**
     * 删除内容.
     *
     * @param uid 用户ID.
     * @param carrierType 内容载体类型.
     * @param contentCarrierId 内容载体ID.
     */
    public void deleteUserFeed(String uid, ContentCarrierTypeEnum carrierType, Long contentCarrierId) {

    }

    /**
     * 获取个人动态.
     *
     * @param visitorIdentity 访问者身份.
     * @param beVisitUid 被访问者.
     * @param carrierType 访问的内容载体类型.
     * @param maxId 分页使用.
     * @param count 获取数量.
     * @return 内容列表.
     */
    public List<Long> queryUserFeeds(IdentityType visitorIdentity, String beVisitUid, ContentCarrierTypeEnum carrierType, Long maxId, Integer count) {
        return null;
    }
}
