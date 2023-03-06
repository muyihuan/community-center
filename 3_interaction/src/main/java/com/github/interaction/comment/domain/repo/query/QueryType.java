package com.github.interaction.comment.domain.repo.query;

import lombok.Getter;

/**
 * 查询类型.
 *
 * @author yanghuan
 */
@Getter
public enum QueryType {


    TIME_SORT_ASC("按时间升序查"),
    TIME_SORT_DESC("按时间降序查"),
    BY_USER("按评论作者降序查"),

    ;

    private final String desc;

    QueryType(String desc) {
        this.desc = desc;
    }
}
