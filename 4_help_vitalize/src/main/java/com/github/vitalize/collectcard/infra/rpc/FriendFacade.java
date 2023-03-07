package com.github.vitalize.collectcard.infra.rpc;

import org.springframework.stereotype.Service;

/**
 * 用户好友门户.
 *
 * @author yanghuan
 */
@Service
public class FriendFacade {

    /**
     * 是否是好友关系.
     */
    public boolean isFriend(String fromUid, String toUid) {

        return false;
    }
}
