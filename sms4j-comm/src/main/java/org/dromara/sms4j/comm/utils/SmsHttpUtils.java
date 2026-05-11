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
import java.util.concurrent.ConcurrentHashMap;

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

    /**
     * 请求超时时间，单位毫秒
     */
    private final Integer timeout;

    // 无代理单例（饿汉式加载）
    private static final SmsHttpUtils NON_PROXY_INSTANCE = new SmsHttpUtils();

    // 代理实例按代理地址和超时隔离，避免不同短信供应商配置不同网络参数时互相冲突。
    private static final Map<String, SmsHttpUtils> PROXY_INSTANCES = new ConcurrentHashMap<>();

    // 无代理构造方法
    private SmsHttpUtils() {
        this.enable = false;
        this.host = null;
        this.port = null;
        this.timeout = null;
    }

    // 代理构造方法
    private SmsHttpUtils(String host, Integer port, Integer timeout) {
        this.enable = StrUtil.isNotBlank(host) && port != null;
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    /**
     * 获取无代理单例
     */
    public static SmsHttpUtils instance() {
        return NON_PROXY_INSTANCE;
    }

    public static SmsHttpUtils instance(Integer timeout) {
        validateTimeout(timeout);
        // 直连场景也要按 timeout 隔离，否则后创建的配置会复用错误的请求超时。
        String key = "direct:" + timeout;
        return PROXY_INSTANCES.computeIfAbsent(key, item -> new SmsHttpUtils(null, null, timeout));
    }

    public static SmsHttpUtils instance(String host, Integer port) {
        return instance(host, port, null);
    }

    public static SmsHttpUtils instance(String host, Integer port, Integer timeout) {
        validateProxyParams(host, port);
        if (timeout != null) {
            validateTimeout(timeout);
        }
        // 同一代理但不同 timeout 代表不同 HTTP 行为，不能复用同一个实例。
        String key = host + ":" + port + ":" + timeout;
        return PROXY_INSTANCES.computeIfAbsent(key, item -> new SmsHttpUtils(host, port, timeout));
    }

    // 代理参数校验
    private static void validateProxyParams(String host, Integer port) {
        if (StrUtil.isBlank(host) || port == null || port <= 0) {
            throw new IllegalArgumentException("Invalid proxy host or port");
        }
    }

    private static void validateTimeout(Integer timeout) {
        if (timeout == null || timeout <= 0) {
            throw new IllegalArgumentException("Invalid http timeout");
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
        if (timeout != null) {
            // 外部短信供应商网络不可控，必须允许配置请求超时，避免线程池长期被阻塞。
            request.timeout(timeout);
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
            throw new SmsBlendException(e.getMessage(), e);
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
            throw new SmsBlendException(e.getMessage(), e);
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
            throw new SmsBlendException(e.getMessage(), e);
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
            throw new SmsBlendException(e.getMessage(), e);
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
            throw new SmsBlendException(e.getMessage(), e);
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
            throw new SmsBlendException(e.getMessage(), e);
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
