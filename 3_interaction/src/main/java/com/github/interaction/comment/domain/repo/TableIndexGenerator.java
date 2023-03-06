package com.github.interaction.comment.domain.repo;

/**
 * 分表值生成
 * @author yanghuan
 */
public class TableIndexGenerator {

    private static final int TABLE_SHARD_COUNT = 1000;

    /**
     * 通过评论ID获取分表值
     * @param id 分表ID
     */
    public static String getTableIdx(Long id) {
        return String.valueOf(id % TABLE_SHARD_COUNT);
    }
}
