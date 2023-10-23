package org.dromara.oa.core.support;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.util.Map;

public class HttpClientImpl extends AbstractHttpClient {
    @Override
    public <T> String post(StringBuilder url, Map<String, String> headers, T message) throws Exception {
        // 构建请求体
        // 发送POST请求
        HttpResponse response = HttpRequest.post(url.toString())
                .headerMap(headers, true)
                .body(message.toString())
                .execute();
        return response.body();
    }
}
