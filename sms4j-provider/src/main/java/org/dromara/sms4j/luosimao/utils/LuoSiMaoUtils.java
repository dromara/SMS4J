package org.dromara.sms4j.luosimao.utils;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.utils.SmsDateUtils;
import org.dromara.sms4j.comm.utils.SmsUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class LuoSiMaoUtils {

    public static LinkedHashMap<String, String> buildHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>(1);
        headers.put(Constant.CONTENT_TYPE, Constant.APPLICATION_FROM_URLENCODED);
        return headers;
    }

    public static LinkedHashMap<String, Object> buildBody(String phone, String message) {
        LinkedHashMap<String, Object> body = new LinkedHashMap<>(2);
        body.put("mobile", phone);
        body.put("message", message);
        return body;
    }

    public static LinkedHashMap<String, Object> buildBody(List<String> phones, String message, Date date) {
        LinkedHashMap<String, Object> body = new LinkedHashMap<>(2);
        body.put("mobile", SmsUtils.joinComma(phones));
        body.put("message", message);
        if (date != null) {
            body.put("time", SmsDateUtils.normDatetimeGmt8(date));
        }
        return body;
    }
}
