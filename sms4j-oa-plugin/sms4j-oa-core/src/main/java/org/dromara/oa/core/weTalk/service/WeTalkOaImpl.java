package org.dromara.oa.core.weTalk.service;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.entity.Response;
import org.dromara.oa.comm.enums.MessageType;
import org.dromara.oa.comm.enums.OaType;
import org.dromara.oa.comm.errors.OaException;
import org.dromara.oa.core.provider.service.AbstractOaBlend;
import org.dromara.oa.core.support.HttpClientImpl;
import org.dromara.oa.core.weTalk.config.WeTalkConfig;
import org.dromara.oa.core.weTalk.utils.WeTalkBuilder;

import java.util.HashMap;
import java.util.Map;

import static org.dromara.oa.comm.enums.OaType.WE_TALK;

/**
 * @author dongfeng
 * 2023-10-22 21:01
 */
@Slf4j
public class WeTalkOaImpl extends AbstractOaBlend<WeTalkConfig> {

    private final HttpClientImpl httpClient = new HttpClientImpl();

    public WeTalkOaImpl(WeTalkConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return OaType.WE_TALK.getType();
    }

    @Override
    public Response sender(Request request, MessageType messageType) {


        StringBuilder webhook = new StringBuilder();
        JSONObject message = null;
        WeTalkConfig config = getConfig();
        webhook.append(WE_TALK.getUrl());
        webhook.append(config.getTokenId());
        message = WeTalkBuilder.createWeTalkMessage(request, messageType);
        String post;
        try {
            post = httpClient.post(webhook, getHeaders(), message);
            log.info("请求返回结果：" + post);
        } catch (Exception e) {
            log.warn("请求失败问题：" + e.getMessage());
            throw new OaException(e.getMessage());
        }
        // 后续解析响应体提取errorCode判断是否成功
        return new Response(true, post, config.getConfigId());
    }

    public static Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }
}
