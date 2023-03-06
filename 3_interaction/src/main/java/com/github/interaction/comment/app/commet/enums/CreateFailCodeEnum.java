package com.github.interaction.comment.app.commet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评论创建失败原因的对应码.
 *
 * @author yanghuan
 */
@AllArgsConstructor
@Getter
public enum CreateFailCodeEnum {

    TEENAGER_LIMIT(20001, "用户开启了青少年模式"),
    FUNCTION_LIMIT(20002, "用户权限限制"),
    GAOSHIJUN_LIMIT(20003, "搞事君限制评论"),
    USER_IS_NULL(20004, "用户信息不存在"),
    BROADCAST_LIMIT_COMMENT(20005, "喊话限制评论"),
    COMMENT_CONTENT_OVER_LIMIT(20006, "评论字数过长"),
    AUDIO_COMMENT_OVER_LIMIT(20007, "语音评论时间过长"),
    COMMENT_CONTENT_AUDIT_REJECT(20008, "评论文本违规"),
    COMMENT_REQUEST_PARAM_ERROR (20009, "参数有误"),
    USER_CANNOT_COMMENT_BROADCAST (20010, "用户等级低于10级不能评论喊话"),
    FEED_IS_NULL(20011, "评论动态不存在"),
    COMMENT_CONTENT_IS_DUPLICATE(20012, "评论内容重复"),
    SERVER_ERROR(20013, "服务调用失败"),

    ;

    int code;
    String desc;
}
