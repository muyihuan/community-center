package com.github.content.feed.app.audit.model;

import lombok.Data;

/**
 * 审核回调协议
 * @author yanghuan
 */
@Data
public class AuditCallbackProtocol {

    /**
     * feed的ID
     */
    private Long feedId;
}
