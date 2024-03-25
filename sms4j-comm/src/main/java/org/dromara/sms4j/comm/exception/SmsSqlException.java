package org.dromara.sms4j.comm.exception;

import lombok.Setter;

@Setter
public class SmsSqlException extends RuntimeException{
    /**
     * -- SETTER --
     *  设置 message
     */
    private String message;
    @Override
    public String getMessage() {
        return message;
    }

    public SmsSqlException(String message) {
        this.message = message;
    }

}
