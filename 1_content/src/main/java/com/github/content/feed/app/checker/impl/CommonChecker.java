package com.github.content.feed.app.checker.impl;

import com.github.content.feed.app.audit.AuditService;
import com.github.content.feed.app.checker.Checker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 公共检查.
 *
 * @author yanghuan
 */
@Service
public class CommonChecker implements Checker {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateModel) {
        // 降级开关
        if (false) {
            return CheckResult.fail("平台维护中，暂时无法进行此操作");
        }

        // 文本长度校验
        String content = feedCreateModel.getTextContent();
        if (checkTooManyWords(content)) {
            return CheckResult.fail("字数超过1000字限制啦~精简下再发咯");
        }

        // 非系统自动发布需要审核
        boolean isUserPublish = feedCreateModel.getIsSystemFeed() == null || !feedCreateModel.getIsSystemFeed();
        if(isUserPublish) {
            // 文本审核校验 预审核文字及自定义标签内容
            if (feedCreateModel.getContentType() == FeedContentTypeEnum.TEXT ||
                feedCreateModel.getContentType() == FeedContentTypeEnum.AUDIO ||
                feedCreateModel.getContentType() == FeedContentTypeEnum.IMAGE ||
                feedCreateModel.getContentType() == FeedContentTypeEnum.IMAGE_MULTI ||
                feedCreateModel.getContentType() == FeedContentTypeEnum.VIDEO_PLAY ||
                feedCreateModel.getContentType() == FeedContentTypeEnum.VIDEO_PLAY_NEW ||
                feedCreateModel.getContentType() == FeedContentTypeEnum.PAINT_PLAY_NEW ||
                feedCreateModel.getContentType() == FeedContentTypeEnum.FORWARD)
            {
                if(StringUtils.isNotEmpty(content)) {
                    boolean result = this.syncAuditText(feedCreateModel.getUid(), content);
                    if (!result) {
                        return CheckResult.fail("您发布的内容有违规信息，请重新编辑后发送");
                    }
                }

                // 当是正常动态时，判断是否是先审后发的情况
                if (feedCreateModel.getSystemPrivilege() == null || feedCreateModel.getSystemPrivilege() == FeedSystemPrivilegeEnum.DEF) {
                    feedCreateModel.setSystemPrivilege(this.getFeedInitState(versionParam, feedCreateModel));
                }
            }
        }

        return CheckResult.success();
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return null;
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    /**
     * 获取初始feed系统可见性
     * 系统可见范围，是系统侧设置的可见范围，优先级大于用户设置的可见范围
     */
    private FeedSystemPrivilegeEnum getFeedInitState(VersionParam versionParam, AbstractFeedCreateBO feed) {
        return FeedSystemPrivilegeEnum.DEF;
    }

    /**
     * 审核feed文本
     * @return  true: 审核通过，false:审核拒绝
     */
    private boolean syncAuditText(String uid, String message) {
        return false;
    }

    /**
     * 校验feed文案字数
     */
    private boolean checkTooManyWords(String content) {
        return StringUtils.isNotEmpty(content) && content.length() > 1000;
    }
}
