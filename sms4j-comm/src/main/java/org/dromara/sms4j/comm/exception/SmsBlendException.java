package org.dromara.sms4j.comm.exception;

public class SmsBlendException extends RuntimeException{
    public String code;
    public String message;

    public SmsBlendException(Throwable cause, String code, String message, String requestId) {
        super(cause);
        this.code = code;
        this.message = message;
        this.requestId = requestId;
    }

    public String requestId;

    public SmsBlendException(String message) {
        super(message);
        this.message = message;
    }

    public SmsBlendException(Exception exception){
        super(exception);
        this.message = exception.getMessage();
    }

    public SmsBlendException(String code, String message) {
        super("[" + code + "] " + message);
        this.message = message;
        this.code = code;
    }

    public SmsBlendException(String code, String message, String requestId) {
        super("[" + code + "] " + message);
        this.message = message;
        this.code = code;
        this.code = requestId;
    }
}
