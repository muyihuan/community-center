package com.github.interaction.comment.app.commet.enums;

import lombok.Getter;

/**
 * 评论的宿主.
 *
 * @author yanghuan
 */
@Getter
public enum TopicTypeEnum {

    /**
     * 评论宿主类型
     */
    FEED(0, "feed"),
    ;

    private final int type;
    private final String desc;

    TopicTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static TopicTypeEnum getByCode(int type) {
        for (TopicTypeEnum value : TopicTypeEnum.values()) {
            if (value.getType() == type) {
                return value;
            }
        }

        throw new RuntimeException("HostBizTypeEnum can't find : " + type);
    }
}
