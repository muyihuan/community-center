package com.github.interaction.comment.infra;

import org.springframework.stereotype.Service;

/**
 * ID生成器.
 *
 * @author yanghuan
 */
@Service
public class IdGenerator {

    /* 十进制后三 位存储分表值 => 最大可支持分表1000 */
    /* 十进制后四～六位存储业务类型 => 最大可支持1000个业务 */
    /* 十进制后十位之后存储全局唯一ID */

    private static final String BIZ_TAG = "comment";

    /**
     * 分别十进制左移 三位 、 六位
     */
    private static final int LEFT_MOVE_THREE = 1000;
    private static final int LEFT_MOVE_SIX = 1000000;

    /**
     * 生成ID
     * @param bizType 业务类型
     * @param tableIdx 分表值
     * @return 唯一ID
     */
    public Long generate(Integer bizType, Integer tableIdx) {
        return null;
    }

    /**
     * 生成ID
     * @param bizType 业务类型
     * @param tableIdx 分表值
     * @return 唯一ID
     */
    public Long generate(Long incrId, Integer bizType, Integer tableIdx) {
        if(incrId == null || bizType == null || tableIdx == null) {
            throw new RuntimeException("生成分布式ID失败，参数异常");
        }

        return incrId * LEFT_MOVE_SIX + bizType * LEFT_MOVE_THREE + tableIdx;
    }
}
