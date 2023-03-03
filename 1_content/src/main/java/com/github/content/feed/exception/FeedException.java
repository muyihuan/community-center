package com.github.content.feed.exception;

/**
 * feed业务的相关异常.
 *
 * @author yanghuan
 */
public class FeedException extends RuntimeException {

    public FeedException() {
        super("待确定");
    }

    public FeedException(String message) {
        super(message);
    }
}
