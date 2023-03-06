package com.github.interaction.comment.exception;

/**
 * comment 异常.
 *
 * @author yanghuan
 */
public class CommentException extends RuntimeException {

    public CommentException() {
        super("待确定");
    }

    public CommentException(String message) {
        super(message);
    }
}
