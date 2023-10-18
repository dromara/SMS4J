package org.dromara.oa.core.service;

import org.dromara.oa.api.OaSender;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.entity.Response;
import org.dromara.oa.comm.enums.MessageType;
import org.dromara.oa.comm.errors.OaException;
import org.dromara.oa.core.support.HttpClientImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import cn.hutool.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static org.dromara.oa.comm.enums.MessageType.*;
import static org.dromara.oa.comm.enums.OaType.DINGTALK;

public class SenderImpl implements OaSender {

    private static Logger logger = Logger.getLogger("oaLog");

    private OaBuild oaBuild;

    private HttpClientImpl httpClient = new HttpClientImpl();

    public SenderImpl() {
    }
    public SenderImpl(OaBuild oaBuild) {
        this.oaBuild = oaBuild;
    }

    public static SenderImpl NewSender(OaBuild oaBuild){
        return  new SenderImpl(oaBuild);
    }

    @Override
    public Response sender(Request request, MessageType messageType) {
        StringBuilder webhook = new StringBuilder();
        webhook.append(DINGTALK.getUrl());
        webhook.append(oaBuild.getConfig().getTokenId());
        if (request.getOaType().equals(DINGTALK.getType())) {
            // todo 等待完善钉钉和飞书的sign
            webhook.append(sign(oaBuild.getConfig().getSign()));
        }
        JSONObject message = createMessage(request, messageType);
        try {
            String post = httpClient.post(webhook, getHeaders(), message);
            logger.info("请求返回结果：" + post);
        } catch (Exception e) {
            logger.warning("请求失败问题：" + e.getMessage());
            throw new OaException(e.getMessage());
        }
        return new Response();
    }

    public static Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }
    public static String sign(String secret) {
        Long timestamp = System.currentTimeMillis();

        String stringToSign = timestamp + "\n" + secret;
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        String sign = null;
        try {
            sign = URLEncoder.encode(new String(Base64.getEncoder().encode(signData)),StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return "&timestamp=" + timestamp + "&sign=" + sign;
    }
    public static JSONObject  createMessage(Request request, MessageType messageType) {
        JSONObject message = new JSONObject();
        if (messageType ==  TEXT){
            message.set("msgtype", "text");
            JSONObject text = new JSONObject();
            text.set("content", request.getContent());
            JSONObject at = new JSONObject();
            at.set("atMobiles", request.getPhones());
            message.set("at", at);
            message.set("text", text);
        } else if (messageType == LINK) {
            message.set("msgtype", "link");
            JSONObject link = new JSONObject();
            link.set("text", request.getContent());
            link.set("title", request.getTitle());
            message.set("link", link);
        } else if (messageType == MARKDOWN) {
            message.set("msgtype", "markdown");
            JSONObject markdown = new JSONObject();
            markdown.set("text", request.getContent());
            markdown.set("title", request.getTitle());
            JSONObject at = new JSONObject();
            at.set("atMobiles", request.getPhones());
            message.set("at", at);
            message.set("markdown", markdown);
        }
        return message;
    }


}
