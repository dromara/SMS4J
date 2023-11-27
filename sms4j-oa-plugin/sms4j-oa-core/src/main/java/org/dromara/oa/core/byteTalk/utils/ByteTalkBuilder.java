package org.dromara.oa.core.byteTalk.utils;

import cn.hutool.json.JSONObject;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.enums.MessageType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

import static org.dromara.oa.comm.enums.MessageType.TEXT;

/**
 * 飞书通知签名和信息构建
 * @author dongfeng
 * 2023-10-19 13:07
 */
public class ByteTalkBuilder {

    public static String byteTalkSign(String secret, Long timestamp) {
        //把timestamp+"\n"+密钥当做签名字符串
        String stringToSign = timestamp + "\n" + secret;
        //使用HmacSHA256算法计算签名
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] signData = mac.doFinal(new byte[]{});
        return new String(Base64.getEncoder().encode(signData));
    }


    public static JSONObject createByteTalkMessage(Request request, MessageType messageType, String sign, Long timestamp) {
        JSONObject message = new JSONObject();
        if (messageType == TEXT) {
            message.set("msg_type", "text");
            message.set("timestamp", timestamp);
            message.set("sign", sign);
            StringBuilder content = new StringBuilder();
            List<String> userNamesList = request.getUserNamesList();
            Boolean isNoticeAll = request.getIsNoticeAll();
            if (isNoticeAll) {
                content.append("<at user_id=\"all\">所有人</at>");
            }
            userNamesList.forEach(l -> content.append("<at user_id=\"ou_xxx\">").append(l).append("</at>"));
            content.append(request.getContent());
            JSONObject text = new JSONObject();
            text.set("text", content);
            message.set("content", text);
        }
        return message;
    }
}
