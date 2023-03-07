package com.github.vitalize.collectcard.app.card.config;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 限制相关配置.
 *
 * @author yanghuan
 */
@Data
public class LimitConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(LimitConfig.class);

    private static final LimitConfig DEF_CONFIG = new LimitConfig();

    /**
     * 每日每个用户下发卡总数限制.
     */
    private int dayIssuedCardCountMax = 3;

    /**
     * 每日每个用户赠卡总数限制.
     */
    private int daySendCardCountMax = 1;

    /**
     * 每日每个用户收卡总数限制.
     */
    private int dayReceiveCardCountMax = 1;

    /**
     * 每日每个用户收卡总数限制.
     */
    private int sendCardExpireHours = 24;

    /**
     * 集卡开始时间.
     */
    private long collectCardStartTime;

    /**
     * 集卡结束时间.
     */
    private long collectCardEndTime;

    /**
     * 灰度白名单.
     */
    private List<String> testWhiteList;

    /**
     * 获取默认的活动配置.
     *
     * @return 配置
     */
    public static LimitConfig get() {
        return get("default");
    }

    /**
     * 通过场景key获取具体的配置，实现不同场景中的配置隔离.
     *
     * @param sceneKey 场景key.
     * @return 限制配置
     */
    public static LimitConfig get(String sceneKey){
        Map<String,LimitConfig> limitConfigMap = loadLimitConfig();
        if(limitConfigMap == null || limitConfigMap.isEmpty()) {
            return DEF_CONFIG;
        }

        LimitConfig limitConfig = null;
        Set<String> keys = limitConfigMap.keySet();
        for (String key : keys) {
            if(StringUtils.startsWith(sceneKey,key)) {
                limitConfig = limitConfigMap.get(key);
            }
        }

        if(limitConfig == null) {
            return DEF_CONFIG;
        }

        return limitConfig;
    }
    /**
     * 集卡业务已关闭(判断默认活动是否关闭)
     */
    public static boolean isClose(String uid) {
        List<String> testWhiteList = LimitConfig.get().getTestWhiteList();
        if(CollectionUtils.isNotEmpty(testWhiteList) && testWhiteList.contains(uid)) {
            return false;
        }

        long collectCardEndTime = LimitConfig.get().getCollectCardEndTime();
        if(collectCardEndTime < 0) {
            return false;
        }

        long collectCardStartTime = LimitConfig.get().getCollectCardStartTime();
        long now = System.currentTimeMillis();
        return now > collectCardEndTime || now < collectCardStartTime;
    }

    /**
     * 从配置中心获取limitConfig数据
     * @return Map 将活动的配置加载过来
     */
    private static Map<String,LimitConfig> loadLimitConfig(){
        return null;
    }

    /**
     * 通过 sceneKey 判断此场景下的活动是否有效
     * @param uid 用户id
     * @param sceneKey 场景key
     * @return boolean true.已关闭 false.未关闭
     */
    public static boolean isClose(String uid , String sceneKey) {
        LimitConfig limitConfig = LimitConfig.get(sceneKey);
        List<String> testWhiteList = limitConfig.getTestWhiteList();
        if(CollectionUtils.isNotEmpty(testWhiteList) && testWhiteList.contains(uid)) {
            return false;
        }

        long collectCardEndTime = limitConfig.getCollectCardEndTime();
        if(collectCardEndTime < 0) {
            return false;
        }

        long collectCardStartTime = limitConfig.getCollectCardStartTime();
        long now = System.currentTimeMillis();
        return now > collectCardEndTime || now < collectCardStartTime;
    }
}
