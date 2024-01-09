package org.dromara.oa.core.byteTalk.utils;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.enums.MessageType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static org.dromara.oa.comm.enums.MessageType.BYTETALK_TEXT;


/**
 * 飞书通知签名和信息构建
 * @author dongfeng
 * 2023-10-19 13:07
 */
@Slf4j
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
        List<String> userIdList = request.getUserIdList();
        if (messageType == BYTETALK_TEXT) {
            message.set("msg_type", "text");
            message.set("timestamp", timestamp);
            message.set("sign", sign);
            StringBuilder content = new StringBuilder();
            Boolean isNoticeAll = request.getIsNoticeAll();
            boolean isNotice = false;
            if (!Objects.isNull(isNoticeAll)&&isNoticeAll) {
                content.append("<at user_id=\"all\">所有人</at>");
                isNotice=true;
            }
            if(!Objects.isNull(userIdList)){
                userIdList.forEach(l -> content.append("<at user_id=\"").append(l).append("\"></at>"));
                isNotice=true;
            }
            // 如果有@就进行消息体换行
            if(isNotice){
                content.append("\n");
            }
            content.append(request.getContent());
            JSONObject text = new JSONObject();
            text.set("text", content);
            message.set("content", text);
        } else {
            log.error("输入的消息格式不对,message:"+messageType+"应该使用BYTETALK前缀的消息类型");
        }
        return message;
    }
}
