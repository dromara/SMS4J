package org.dromara.sms4j.comm.utils;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.util.Map;

public class SmsHttpUtils {

    /**
     * 是否启用代理 默认不启用
     */
    private final Boolean enable;

    /**
     * 代理服务器地址
     */
    private final String host;

    /**
     * 代理服务器端口
     */
    private final Integer port;

    // 无代理单例（饿汉式加载）
    private static final SmsHttpUtils NON_PROXY_INSTANCE = new SmsHttpUtils();

    // 代理单例（双重校验锁延迟加载）
    private static volatile SmsHttpUtils PROXY_INSTANCE;

    // 无代理构造方法
    private SmsHttpUtils() {
        this.enable = false;
        this.host = null;
        this.port = null;
    }

    // 代理构造方法
    private SmsHttpUtils(String host, Integer port) {
        this.enable = true;
        this.host = host;
        this.port = port;
    }

    /**
     * 获取无代理单例
     */
    public static SmsHttpUtils instance() {
        return NON_PROXY_INSTANCE;
    }

    /**
     * 获取代理单例（线程安全 + 参数校验）
     */
    public static SmsHttpUtils instance(String host, Integer port) {
        if (PROXY_INSTANCE == null) {
            synchronized (SmsHttpUtils.class) {
                if (PROXY_INSTANCE == null) {
                    validateProxyParams(host, port);
                    PROXY_INSTANCE = new SmsHttpUtils(host, port);
                }
            }
        } else {
            // 二次调用时校验参数一致性
            if (!PROXY_INSTANCE.host.equals(host) || !PROXY_INSTANCE.port.equals(port)) {
                throw new IllegalStateException("Proxy parameters cannot be modified after initialization");
            }
        }
        return PROXY_INSTANCE;
    }

    // 代理参数校验
    private static void validateProxyParams(String host, Integer port) {
        if (StrUtil.isBlank(host) || port == null || port <= 0) {
            throw new IllegalArgumentException("Invalid proxy host or port");
        }
    }

    /**
     * 配置请求 是否走代理
     * @param url 请求地址
     * @return HttpRequest
     */
    private HttpRequest request(String url){
        HttpRequest request = HttpRequest.of(url);
        if (enable){
            request.setHttpProxy(host, port);
        }
        return request;
    }

    /**
     * 构造post请求
     * @param url 请求地址
     * @return HttpRequest
     */
    private HttpRequest post(String url){
        HttpRequest post = request(url);
        post.setMethod(Method.POST);
        return post;
    }

    /**
     * 构造get请求
     * @param url 请求地址
     * @return HttpRequest
     */
    private HttpRequest get(String url){
        HttpRequest get = request(url);
        get.setMethod(Method.GET);
        return get;
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
        try (HttpResponse response = post(url)
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
        try (HttpResponse response = post(url)
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
        try (HttpResponse response = post(url)
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
        try (HttpResponse response = post(urlWithParams)
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
        try (HttpResponse response = get(url)
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
        try (HttpResponse response = get(url)
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
