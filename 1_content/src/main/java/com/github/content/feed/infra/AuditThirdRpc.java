package com.github.content.feed.infra;

import com.github.content.feed.infra.enums.SyncAuditResultEnum;
import org.springframework.stereotype.Service;

/**
 * 审核.
 *
 * @author yanghuan
 */
@Service
public class AuditThirdRpc {

    /**
     * 同步审核文本
     */
    public SyncAuditResultEnum syncAuditText(String uid, String message, String bizId) {
        return SyncAuditResultEnum.REJECTED;
    }
}
