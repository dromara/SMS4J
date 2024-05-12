package org.dromara.sms4j.api.utils;

import org.dromara.sms4j.api.entity.SmsResponse;

public class SmsRespUtils {
    private SmsRespUtils() {
    }   //私有构造防止实例化

    public static SmsResponse error(){
        return error("error no response", null);
    }

    public static SmsResponse error(String configId){
        return error("error no response", configId);
    }

    public static SmsResponse error(String detailMessage, String configId){
        return resp(detailMessage, false, configId);
    }

    public static SmsResponse success(){
        return success(null);
    }

    public static SmsResponse success(Object data){
        return success(data, null);
    }

    public static SmsResponse resp(Object data, boolean success){
        return resp(data, success, null);
    }

    public static SmsResponse success(Object data, String configId){
        return resp(data, true, configId);
    }

    public static SmsResponse resp(boolean success){
        return success ? success() : error();
    }

    public static SmsResponse resp(boolean success, String configId){
        return resp(null, success, configId);
    }

    public static SmsResponse resp(Object data, boolean success, String configId){
        SmsResponse error = new SmsResponse();
        error.setSuccess(success);
        error.setData(data);
        error.setConfigId(configId);
        return error;
    }
}
