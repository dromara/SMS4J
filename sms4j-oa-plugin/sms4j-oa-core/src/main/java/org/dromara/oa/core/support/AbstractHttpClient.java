package org.dromara.oa.core.support;

import java.util.Map;

public abstract class AbstractHttpClient implements MsgHttpClient{

    @Override
    public String get(String url) {
        return null;
    }

    @Override
    public String get(String url, Map<String, String> headers) {
        return null;
    }

    @Override
    public String post(String url) {
        return null;
    }

    @Override
    public String post(String url, Map<String, String> headers) {
        return null;
    }
}
