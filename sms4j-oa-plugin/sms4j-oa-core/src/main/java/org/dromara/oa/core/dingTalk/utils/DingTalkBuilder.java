package org.dromara.oa.core.dingTalk.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.enums.MessageType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.dromara.oa.comm.enums.MessageType.DING_TALK_LINK;
import static org.dromara.oa.comm.enums.MessageType.DING_TALK_MARKDOWN;
import static org.dromara.oa.comm.enums.MessageType.DING_TALK_TEXT;

/**
 * 钉钉通知签名和信息构建
 * @author dongfeng
 * 2023-10-19 13:07
 */
@Slf4j
public class DingTalkBuilder {
    public static String sign(String secret) {
        Long timestamp = System.currentTimeMillis();

        String stringToSign = timestamp + "\n" + secret;
        HMac mac = new HMac(HmacAlgorithm.HmacSHA256, secret.getBytes(StandardCharsets.UTF_8));
        byte[] signData = mac.digest(stringToSign.getBytes(StandardCharsets.UTF_8));
        String sign;
        try {
            sign = URLEncoder.encode(Base64.encode(signData), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return "&timestamp=" + timestamp + "&sign=" + sign;
    }


    public static JSONObject createMessage(Request request, MessageType messageType) {


        JSONObject message = new JSONObject();
        if (messageType == DING_TALK_TEXT) {
            message.set("msgtype", "text");
            JSONObject text = new JSONObject();
            text.set("content", request.getContent());
            message.set("text", text);
        } else if (messageType == DING_TALK_MARKDOWN) {
            message.set("msgtype", "markdown");
            JSONObject markdown = new JSONObject();
            markdown.set("text", request.getContent());
            markdown.set("title", request.getTitle());
            message.set("markdown", markdown);
        } else if (messageType == DING_TALK_LINK) {
            message.set("msgtype", "link");
            JSONObject link = new JSONObject();
            link.set("text", request.getContent());
            link.set("title", request.getTitle());
            link.set("picUrl", request.getPicUrl());
            link.set("messageUrl", request.getMessageUrl());
            message.set("link", link);
        }else{
            log.error("输入的消息格式不对,message:"+messageType+"应该使用DINGTALK前缀的消息类型");
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
