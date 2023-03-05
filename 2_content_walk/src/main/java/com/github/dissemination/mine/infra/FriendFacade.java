package com.github.dissemination.mine.infra;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 好友关系.
 *
 * @author yanghuan
 */
@Service
public class FriendFacade {

    /**
     * 获取用户好友列表.
     */
    public List<String> getUserFriends(String uid) {
        return null;
    }

    /**
     * 判断是否是好友关系.
     */
    public boolean isFriend(String uid, String maybeFriendUid) {
       return false;
    }
}
