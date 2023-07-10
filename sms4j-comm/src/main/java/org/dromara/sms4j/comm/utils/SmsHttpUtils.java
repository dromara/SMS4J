package org.dromara.sms4j.comm.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.util.Map;

/**
 * @author bleachtred
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SmsHttpUtils {

    /**
     * 发送post json请求
     * @param url 请求地址
     * @param headers 请求头
     * @param body 请求体(json格式字符串)
     * @return 返回体
     */
    public static JSONObject postJson(String url, Map<String, String> headers, String body){
        try(HttpResponse response = HttpRequest.post(url)
                .addHeaders(headers)
                .body(body)
                .execute()){
            return JSONUtil.parseObj(response.body());
        }catch (Exception e){
            throw new SmsBlendException(e.getMessage());
        }
    }

    /**
     * 发送post json请求
     * @param url 请求地址
     * @param headers 请求头
     * @param body 请求体(map格式请求体)
     * @return 返回体
     */
    public static JSONObject postJson(String url, Map<String, String> headers, Map<String, Object> body){
        return postJson(url, headers, JSONUtil.toJsonStr(body));
    }

    /**
     * 发送post form 请求
     * @param url 请求地址
     * @param headers 请求头
     * @param body 请求体(map格式请求体)
     * @return 返回体
     */
    public static JSONObject postFrom(String url, Map<String, String> headers, Map<String, Object> body){
        try(HttpResponse response = HttpRequest.post(url)
                .addHeaders(headers)
                .form(body)
                .execute()){
            return JSONUtil.parseObj(response.body());
        }catch (Exception e){
            throw new SmsBlendException(e.getMessage());
        }
    }
}
