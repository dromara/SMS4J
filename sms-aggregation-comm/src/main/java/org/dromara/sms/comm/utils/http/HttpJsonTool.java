package org.dromara.sms.comm.utils.http;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public class HttpJsonTool {


    public static <T> T getJSONBody(Object response,Class<T>t){
        return JSONObject.parseObject(response.toString(), t);
    }

    public static <T> T getJSONBody(String response,Class<T>t){
        return JSONObject.parseObject(response, t);
    }

    public static JSONObject getJSONObject(Object obj){
        return JSONObject.parseObject(obj.toString());
    }
}
