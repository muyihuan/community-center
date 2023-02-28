package com.github.content.feed.exception;

/**
 * 起始code 20001
 * @author yanghuan
 */
public enum FeedErrorCode {

    SOURCE_ID_ERROR(20001, "sourceId 有误"),
    UGC_CREATE_ERROR(20002, "ugc 创建失败"),
    FEED_CREATE_ERROR(20003, "feed 创建失败"),
    FEED_TYPE_NOT_SUPPORT_ERROR(20004, "不支持该内容类型"),
    FEED_PARSE_NOT_SUPPORT_ERROR(20005, "feed 内容解析失败，不支持该内容类型"),
    FEED_QUERY_ERROR(20006, "feed 查询失败"),
    FEED_PARSE_ERROR(20007, "feed 内容解析失败"),
    FEED_NOT_FOUND(20008, "feed 信息不存在"),
    FEED_UPDATE_STATUS_ERROR(20009, "feed 更新状态失败"),
    UGC_CREATOR_ERROR(20010, "ugc 创建器配置存在问题"),
    FEED_TAG_CREATE_ERROR(20011, "tag 创建失败"),
    FEED_TAG_INVALID_ERROR(20012, "feed 添加了无效话题"),
    FEED_PARAM_ERROR(20013, "feed 参数有误"),
    QUERY_FEED_PARAM_ERROR(20014, "查询feed 参数有误"),
    OPERATE_FREQUENTLY_ERROR(20015, "操作频繁 执行失败"),
    UPDATE_FEED_ERROR(20016, "更新 feed 失败"),
    OPT_ILLEGAL_ERROR(20017, "操作不合法"),
    UPDATE_FEED_PARAM_ERROR(20018, "更新feed 参数有误"),
    UGC_CREATE_PARAM_ERROR(20019, "ugc创建 参数有误"),
    OPT_NOT_SUPPORT_ERROR(20020, "不支持该操作"),

    ;

    private final int code;
    private final String msg;

    FeedErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
