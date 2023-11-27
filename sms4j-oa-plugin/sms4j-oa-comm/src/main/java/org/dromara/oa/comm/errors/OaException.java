package org.dromara.oa.comm.errors;

public class OaException extends RuntimeException {

    public OaException() {
        super();
    }

    public OaException(String message) {
        super(message);
    }

    public OaException(String message,String configId) {
        super("configId为{"+configId+"}抛出异常:"+message);
    }
}
