package org.dromara.oa.core.byteTalk.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.enums.MessageType;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.dromara.oa.comm.enums.MessageType.BYTE_TALK_TEXT;

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
        HMac mac = new HMac(HmacAlgorithm.HmacSHA256, stringToSign.getBytes(StandardCharsets.UTF_8));
        byte[] signData = mac.digest(new byte[]{});
        return Base64.encode(signData);
    }


    public static JSONObject createByteTalkMessage(Request request, MessageType messageType, String sign, Long timestamp) {
        JSONObject message = new JSONObject();
        List<String> userIdList = request.getUserIdList();
        if (messageType == BYTE_TALK_TEXT) {
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
