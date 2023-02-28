package com.github.content.feed.domain.model.lifecycle;

import lombok.Data;
import lombok.Getter;

/**
 * feed生命周期变化协议 feed生命周期：创建 -> 隐藏 -> 开放 -> 内容修改 -> 删除.
 *
 * @author yanghuan
 * 注：消费方一定要保证幂等～ 使用方式的参考代码详见： README.md
 */
@Data
public class FeedLifeCycleProtocol {

    /**
     * feed信息变化类型
     */
    private Integer changeType;

    /**
     * feed的唯一ID
     */
    private Long feedId;

    /**
     * feed的类型
     */
    private Integer feedType;

    /**
     * feed的作者UID
     */
    private String uid;

    /**
     * feed当前的状态
     */
    private Integer state;

    /**
     * feed当前的可见范围
     */
    private Integer privilege;

    /**
     * feed的基础信息：属性信息等 {@link FeedBaseInfo}
     */
    private String feedBaseInfo;

    /**
     * 修改前feed信息
     * 如果是非信息类型的变化，feed信息数据可以从该字段获取
     */
    private String beforeModifyFeedInfo;

    /**
     * 修改后feed信息
     */
    private String afterModifyFeedInfo;

    public FeedLifeCycleProtocol() {

    }

    public FeedLifeCycleProtocol(Long feedId, Integer feedType, String feedUid, Integer changeType, Integer state, Integer privilege, String feedBaseInfo) {
        this.feedId = feedId;
        this.feedType = feedType;
        this.uid = feedUid;
        this.changeType = changeType;
        this.state = state;
        this.privilege = privilege;
        this.feedBaseInfo = feedBaseInfo;
        this.beforeModifyFeedInfo = "";
        this.afterModifyFeedInfo = "";
    }

    public FeedLifeCycleProtocol(Long feedId, Integer feedType, String feedUid, Integer changeType, Integer state, Integer privilege, String feedBaseInfo, String beforeModifyFeedInfo, String afterModifyFeedInfo) {
        this.feedId = feedId;
        this.feedType = feedType;
        this.uid = feedUid;
        this.changeType = changeType;
        this.state = state;
        this.privilege = privilege;
        this.feedBaseInfo = feedBaseInfo;
        this.beforeModifyFeedInfo = beforeModifyFeedInfo;
        this.afterModifyFeedInfo = afterModifyFeedInfo;
    }

    @Getter
    public enum ChangeType {

        /**
         * feed发生变化的类型
         */
        VISIBILITY_CHANGE(0, "feed可见性变化"),
        CONTENT_CHANGE(1, "feed内容修改"),
        BORN(2, "feed出生"),
        ;

        private final int code;
        private final String desc;
        ChangeType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    @Getter
    public enum FeedState {

        /**
         * feed的状态
         */
        NORMAL(0, "正常状态"),
        DEL(1, "删除状态");

        private final int code;
        private final String desc;
        FeedState(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    @Getter
    public enum FeedPrivilege {

        /**
         * feed的可见范围，业务方根据自己业务来决定不同可见范围的表现
         */
        FRIEND_VIEW(0, "仅好友可见"),
        ALL_VIEW(1, "全部人可见"),
        STRANGER_VIEW(2, "仅陌生人可见"),
        SELF_VIEW(3, "仅自己可见"),
        ;

        private final int code;
        private final String desc;
        FeedPrivilege(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }
}
