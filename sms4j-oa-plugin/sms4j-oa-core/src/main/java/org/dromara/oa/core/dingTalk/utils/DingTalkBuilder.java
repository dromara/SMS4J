package org.dromara.oa.core.dingTalk.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.enums.MessageType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static org.dromara.oa.comm.enums.MessageType.*;

/**
 * 钉钉通知签名和信息构建
 * @author dongfeng
 * 2023-10-19 13:07
 */
public class DingTalkBuilder {
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
            sign = URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return "&timestamp=" + timestamp + "&sign=" + sign;
    }


    public static JSONObject createMessage(Request request, MessageType messageType) {


        JSONObject message = new JSONObject();
        if (messageType == TEXT) {
            message.set("msgtype", "text");
            JSONObject text = new JSONObject();
            text.set("content", request.getContent());
            message.set("text", text);
        } else if (messageType == MARKDOWN) {
            message.set("msgtype", "markdown");
            JSONObject markdown = new JSONObject();
            markdown.set("text", request.getContent());
            markdown.set("title", request.getTitle());
            message.set("markdown", markdown);
        } else if (messageType == LINK) {
            message.set("msgtype", "link");
            JSONObject link = new JSONObject();
            link.set("text", request.getContent());
            link.set("title", request.getTitle());
            link.set("picUrl", request.getPicUrl());
            link.set("messageUrl", request.getMessageUrl());
            message.set("link", link);
        }


        // 处理提到的人
        JSONObject at = new JSONObject();
        List<String> phoneList = request.getPhoneList();
        List<String> userIdList = request.getUserIdList();
        if(!Objects.isNull(phoneList)){
            JSONArray phoneArray = new JSONArray();
            phoneList.forEach(phoneArray::set);
            at.set("atMobiles", phoneArray);
        }
        if(!Objects.isNull(userIdList)){
            JSONArray userIdArray = new JSONArray();
            userIdList.forEach(userIdArray::set);
            at.set("atUserIds", userIdArray);
        }
        at.set("isAtAll", request.getIsNoticeAll());
        message.set("at", at);
        return message;
    }
}
