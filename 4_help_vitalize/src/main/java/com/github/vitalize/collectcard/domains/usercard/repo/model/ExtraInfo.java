package com.github.vitalize.collectcard.domains.usercard.repo.model;

import lombok.Data;

/**
 * 扩展信息.
 *
 * @author yanghuan
 */
@Data
public class ExtraInfo {

    /**
     * 是否锁定.
     */
    private Boolean isLock;

    /**
     * 是否已收集完成，卡牌全部集齐且全部激活.
     */
    private Boolean isCollectCompleted;
}
