package com.github.content.feed.infra;

import com.github.content.feed.infra.enums.AuditResultEnum;
import org.springframework.stereotype.Service;

/**
 * 审核.
 *
 * @author yanghuan
 */
@Service
public class AuditFacade {

    /**
     * 同步审核文本
     */
    public AuditResultEnum syncAuditText(String uid, String message, String bizId) {
        return AuditResultEnum.REJECTED;
    }
}
