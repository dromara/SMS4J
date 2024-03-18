package org.dromara.oa.core.support;

import java.util.Map;

public interface MsgHttpClient {

    String get(String url);

    String get(String url, Map<String, String> headers);

    String post(String url);

    String post(String url, Map<String, String> headers);

    <T> String post(StringBuilder url, Map<String, String> headers, T message);

}
