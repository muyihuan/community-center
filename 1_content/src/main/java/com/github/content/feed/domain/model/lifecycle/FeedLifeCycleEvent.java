package com.github.content.feed.domain.model.lifecycle;

import com.github.content.feed.domain.enums.FeedPrivilegeEnum;
import com.github.content.feed.domain.enums.FeedSystemPrivilegeEnum;
import com.github.content.feed.domain.model.property.AtInfo;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
import java.util.List;

/**
 * feed生命周期变化事件 feed生命周期：创建 -> 隐藏 -> 开放 -> 内容修改 -> 删除.
 * 注：消费方一定要保证幂等～ 使用方式的参考代码详见： README.md
 *
 * @author yanghuan
 */
@Data
public class FeedLifeCycleEvent {

    /**
     * 变化类型.
     */
    private Integer changeType;

    /**
     * feed的ID.
     */
    private Long feedId;

    /**
     * feed的类型.
     */
    private Integer feedType;

    /**
     * feed的作者UID.
     */
    private String feedUid;

    /**
     * feed当前的状态.
     */
    private Integer state;

    /**
     * feed当前的可见范围.
     */
    private Integer privilege;

    /**
     * feed的基础信息.
     */
    private FeedBaseInfo feedBaseInfo;

    /**
     * 修改前feed信息.
     * 如果是非信息类型的变化，feed信息数据可以从该字段获取.
     */
    private FeedModifyInfo beforeModifyFeedInfo;

    /**
     * 修改后feed信息.
     */
    private FeedModifyInfo afterModifyFeedInfo;

    public FeedLifeCycleEvent(Long feedId, Integer feedType, String feedUid, Integer changeType, Integer state, Integer privilege, FeedBaseInfo feedBaseInfo) {
        this.feedId = feedId;
        this.feedType = feedType;
        this.feedUid = feedUid;
        this.changeType = changeType;
        this.state = state;
        this.privilege = privilege;
        this.feedBaseInfo = feedBaseInfo;
        this.beforeModifyFeedInfo = null;
        this.afterModifyFeedInfo = null;
    }

    public FeedLifeCycleEvent(Long feedId, Integer feedType, String feedUid, Integer changeType, Integer state, Integer privilege, FeedBaseInfo feedBaseInfo, FeedModifyInfo beforeModifyFeedInfo, FeedModifyInfo afterModifyFeedInfo) {
        this.feedId = feedId;
        this.feedType = feedType;
        this.feedUid = feedUid;
        this.changeType = changeType;
        this.state = state;
        this.privilege = privilege;
        this.feedBaseInfo = feedBaseInfo;
        this.beforeModifyFeedInfo = beforeModifyFeedInfo;
        this.afterModifyFeedInfo = afterModifyFeedInfo;
    }

    /**
     * feed基础信息.
     *
     * @author yanghuan
     */
    @Getter
    public static class FeedBaseInfo {

        /**
         * @ 信息.
         */
        private List<AtInfo> atInfoList;

        /**
         * feed的创建时间.
         */
        private Date createTime;

        /**
         * feed携带的话题信息.
         */
        private List<Long> tagIdList;

        public FeedBaseInfo(List<AtInfo> atInfoList, Date createTime, List<Long> tagIdList) {
            this.atInfoList = atInfoList;
            this.createTime = createTime;
            this.tagIdList = tagIdList;
        }
    }

    /**
     * feed的修改信息.
     *
     * @author yanghuan
     */
    @Getter
    public static class FeedModifyInfo {

        /**
         * feed携带的话题信息.
         */
        private List<Long> tagIdList;

        public FeedModifyInfo(List<Long> tagIdList) {
            this.tagIdList = tagIdList;
        }
    }

    @Getter
    public enum ChangeType {

        VISIBILITY_CHANGE(0, "可见性变化"),
        CONTENT_CHANGE(1, "内容修改"),
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

        SELF_VISIBLE(0, "仅自己可见"),
        FRIEND_VISIBLE(1, "仅好友可见"),
        STRANGER_VISIBLE(2, "仅陌生人可见"),
        ALL_VISIBLE(3, "全部人可见"),
        ;

        private final int code;
        private final String desc;
        FeedPrivilege(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * feed可见性解析转换.
     */
    public static FeedPrivilege parseFeedPrivilege(FeedPrivilegeEnum feedPrivilege, FeedSystemPrivilegeEnum systemPrivilege) {
        if(systemPrivilege != null) {
            // 现在用户侧可以设置的可见范围没有自见，所以不用考虑两个可见性范围冲突问题
            if(systemPrivilege == FeedSystemPrivilegeEnum.UGC_SELF_VISIBLE) {
                return FeedLifeCycleEvent.FeedPrivilege.SELF_VISIBLE;
            }
            else if(systemPrivilege == FeedSystemPrivilegeEnum.FEED_SELF_VISIBLE) {
                return FeedLifeCycleEvent.FeedPrivilege.SELF_VISIBLE;
            }
        }

        if(feedPrivilege == FeedPrivilegeEnum.FRIEND_VISIBLE) {
            return FeedLifeCycleEvent.FeedPrivilege.FRIEND_VISIBLE;
        }
        else if(feedPrivilege == FeedPrivilegeEnum.ALL_VISIBLE) {
            return FeedLifeCycleEvent.FeedPrivilege.ALL_VISIBLE;
        }
        else if(feedPrivilege == FeedPrivilegeEnum.STRANGER_VISIBLE) {
            return FeedLifeCycleEvent.FeedPrivilege.STRANGER_VISIBLE;
        }
        else if(feedPrivilege == FeedPrivilegeEnum.SELF_VISIBLE) {
            return FeedLifeCycleEvent.FeedPrivilege.SELF_VISIBLE;
        }

        return FeedLifeCycleEvent.FeedPrivilege.ALL_VISIBLE;
    }
}
