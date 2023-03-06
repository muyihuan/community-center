package com.github.interaction.comment.infra;

import org.springframework.stereotype.Service;

/**
 * 审核三方.
 *
 * @author yanghuan
 */
@Service
public class AuditFacade {

    /**
     * 同步审核文本 true：通过、false：拒绝.
     */
    public boolean syncAuditText(String uid, String message, String bizId) {
        return false;
    }
}
