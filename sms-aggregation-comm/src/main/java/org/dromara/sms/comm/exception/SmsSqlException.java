package org.dromara.sms.comm.exception;

public class SmsSqlException extends RuntimeException{
    private String message;
    @Override
    public String getMessage() {
        return message;
    }

    public SmsSqlException(String message) {
        this.message = message;
    }

    /**
     * 设置 message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
