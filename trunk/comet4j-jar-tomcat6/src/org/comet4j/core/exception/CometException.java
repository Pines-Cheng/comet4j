package org.comet4j.core.exception;

public class CometException extends RuntimeException{
	private static final long serialVersionUID = 3048792398595051727L;
	private Throwable cause;

    public CometException(String message) {
        super(message);
    }

    public CometException(Throwable t) {
        super(t.getMessage());
        this.cause = t;
    }

    public Throwable getCause() {
        return this.cause;
    }
}