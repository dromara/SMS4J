package org.dromara.sms4j.comm.utils;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.util.Map;

public class SmsHttpUtils {

    private SmsHttpUtils() {
    }

    private static class SmsHttpHolder {
        private static final SmsHttpUtils INSTANCE = new SmsHttpUtils();
    }

    public static SmsHttpUtils instance() {
        return SmsHttpHolder.INSTANCE;
    }

    /**
     * 发送post json请求
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param body    请求体(json格式字符串)
     * @return 返回体
     */
    public JSONObject postJson(String url, Map<String, String> headers, String body) {
        try (HttpResponse response = HttpRequest.post(url)
                .addHeaders(headers)
                .body(body)
                .execute()) {
            return JSONUtil.parseObj(response.body());
        } catch (Exception e) {
            throw new SmsBlendException(e.getMessage());
        }
    }

    /**
     * 发送post json请求
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param body    请求体(map格式请求体)
     * @return 返回体
     */
    public JSONObject postJson(String url, Map<String, String> headers, Map<String, Object> body) {
        return postJson(url, headers, JSONUtil.toJsonStr(body));
    }

    /**
     * 发送post form 请求
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param body    请求体(map格式请求体)
     * @return 返回体
     */
    public JSONObject postFrom(String url, Map<String, String> headers, Map<String, Object> body) {
        try (HttpResponse response = HttpRequest.post(url)
                .addHeaders(headers)
                .form(body)
                .execute()) {
            return JSONUtil.parseObj(response.body());
        } catch (Exception e) {
            throw new SmsBlendException(e.getMessage());
        }
    }

    /**
     * 发送post form 请求
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param body    请求体(map格式请求体)
     * @param username 用户名
     * @param password 密码
     * @return 返回体
     */
    public JSONObject postBasicFrom(String url, Map<String, String> headers, String username, String password, Map<String, Object> body) {
        try (HttpResponse response = HttpRequest.post(url)
                .addHeaders(headers)
                .basicAuth(username, password)
                .form(body)
                .execute()) {
            return JSONUtil.parseObj(response.body());
        } catch (Exception e) {
            throw new SmsBlendException(e.getMessage());
        }
    }

    /**
     * 发送post url 参数拼装url传输
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param params  请求参数
     * @return 返回体
     */
    public JSONObject postUrl(String url, Map<String, String> headers, Map<String, Object> params) {
        String urlWithParams = url + "?" + URLUtil.buildQuery(params, null);
        try (HttpResponse response = HttpRequest.post(urlWithParams)
                .addHeaders(headers)
                .execute()) {
            return JSONUtil.parseObj(response.body());
        } catch (Exception e) {
            throw new SmsBlendException(e.getMessage());
        }
    }

    /**
     * 发送get
     *
     * @param url 请求地址
     * @return 返回体
     */
    public JSONObject getBasic(String url, String username, String password) {
        try (HttpResponse response = HttpRequest.get(url)
                .basicAuth(username, password)
                .execute()) {
            return JSONUtil.parseObj(response.body());
        } catch (Exception e) {
            throw new SmsBlendException(e.getMessage());
        }
    }

    /**
     * 发送get
     *
     * @param url 请求地址
     * @return 返回体
     */
    public JSONObject getUrl(String url) {
        try (HttpResponse response = HttpRequest.get(url)
                .execute()) {
            return JSONUtil.parseObj(response.body());
        } catch (Exception e) {
            throw new SmsBlendException(e.getMessage());
        }
    }

    /**
     * 线程睡眠
     *
     * @param retryInterval 秒
     */
    public void safeSleep(int retryInterval) {
        ThreadUtil.safeSleep(retryInterval * 1000L);
    }
}
