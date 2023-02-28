package com.github.content.feed.domains.model;

import com.github.content.feed.domains.enums.FeedContentTypeEnum;
import com.github.content.feed.domains.enums.FeedPrivilegeEnum;
import com.github.content.feed.domains.enums.FeedSourceTypeEnum;
import com.github.content.feed.domains.enums.FeedSystemPrivilegeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * feed模型
 * @author yanghuan
 */
@Data
public class FeedSourceModel {

    /**
     * feed的id
     */
    private Long feedId;

    /**
     * feed的作者
     */
    private String uid;

    /**
     * feed的内容类型
     */
    private FeedContentTypeEnum contentType;

    /**
     * feed的状态
     */
    private FeedStateEnum state;

    /**
     * 可见范围，是用户侧设置的可见范围，优先级小于系统可见范围
     */
    private FeedPrivilegeEnum privilege;

    /**
     * 系统可见范围，是系统侧设置的可见范围，优先级大于用户设置的可见范围
     */
    private FeedSystemPrivilegeEnum systemPrivilege;

    /**
     * feed对应的资源id
     */
    private Long sourceId;

    /**
     * feed对应的资源类型
     */
    private FeedSourceTypeEnum sourceType;

    /**
     * 作者昵称
     */
    @Deprecated
    private String userName;

    /**
     * 作者头像
     */
    @Deprecated
    private String userIcon;

    /**
     * 性别
     */
    @Deprecated
    private String gender;

    /**
     * feed的送花数量
     */
    @Deprecated
    private Integer roseCount;

    /**
     * feed的中文字内容
     */
    private String textContent;

    /**
     * feed的topicId 0表示没有topic，大于0表示有topic并且为对应topic的id
     */
    @Deprecated
    private Integer topicId;

    /**
     * feed的创建时间
     */
    private Date feedCreateTime;

    /**
     * feed的坐标信息信息（经纬度）
     */
    private LbsInfo lbsInfo;

    /**
     * 是否是匿名feed，0：不是、1：是匿名feed
     */
    private Integer anonymousFeedType;

    /**
     * feed礼物面板配置
     */
    private Integer giftStrategyId;

    /**
     * 文字内容信息
     */
    private TextContentModel text;

    /**
     * 话题信息
     */
    private List<Long> tagIds;

    /**
     * 艾特信息
     */
    private List<AtInfo> atInfoList;

    /**
     * 同类签信息
     */
    private LabelInfo labelInfo;

    /**
     * 词卡信息
     */
    private CardInfo cardInfo;

    /**
     * 场景标识别
     */
    private Integer sceneType;

    /**
     * 场景参数
     */
    private Map sceneParam;

    /**
     * 猜猜信息
     */
    @Deprecated
    private LotteryContentModel lottery;

    /**
     * 照片信息
     */
    private AlbumImageContentModel albumImage;

    /**
     * 多图信息
     */
    private MultiImageContentModel multiImage;

    /**
     * 花魁信息
     */
    private RoseChampionContentModel roseChampion;

    /**
     * 网页链接信息
     */
    private LinkContentModel webLink;

    /**
     * 画猜信息
     */
    private PaintContentModel paint;

    /**
     * 视频feed
     */
    private VideoContentModel video;

    /**
     * 徽章
     */
    private MedalContentModel medal;

    /**
     * 转发信息
     */
    private RepostInfoOverAllModel repost;

    /**
     * 语音feed
     */
    private AudioContentModel audio;

    /**
     * feed扩展
     */
    private ExtraInfo extraInfo;

    /**
     * 兼容老逻辑使用的
     */
    @Deprecated
    private String ugcContent;
}
