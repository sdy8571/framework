package com.framework.sequence.exception;

/**
 * @author sdy
 * @date 2020/1/3
 * @Version 1.0
 * @Description
 */
public class SequenceException extends RuntimeException {

    private String message;

    public SequenceException() {
        super();
    }

    public SequenceException(String message) {
        super(message);
    }

    public SequenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SequenceException(Throwable cause) {
        super(cause);
    }

}
