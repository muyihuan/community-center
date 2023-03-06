package com.github.interaction.comment.app.commet;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 评论互动通知.
 *
 * @author yanghuan
 */
@Service
public class CommentNoticeService {

    /**
     * 私聊消息
     */
    private static void sendChatMsg(String fromUid, String toUid, Map<String, Object> bodyMap) {

    }

    /**
     * 互动消息
     */
    private static void sendActMsg(String fromUid, String toUid, String iconImg, String title, String message, String opt, int anonymousFeedType) {

    }
}