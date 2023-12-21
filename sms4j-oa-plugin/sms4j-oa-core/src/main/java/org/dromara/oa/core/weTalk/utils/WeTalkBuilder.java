package org.dromara.oa.core.weTalk.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.enums.MessageType;

import java.util.List;
import java.util.Objects;

import static org.dromara.oa.comm.enums.MessageType.WETETALK_MARKDOWN;
import static org.dromara.oa.comm.enums.MessageType.WETETALK_TEXT;


/**
 * 企业微信--签名和消息构建
 * @author dongfeng
 * 2023-10-19 13:07
 */
@Slf4j
public class WeTalkBuilder {


    public static JSONObject createWeTalkMessage(Request request, MessageType messageType) {

        JSONObject message = new JSONObject();
        List<String> userIdList = request.getUserIdList();
        List<String> phoneList = request.getPhoneList();
        StringBuilder content = new StringBuilder();
        Boolean isNoticeAll = request.getIsNoticeAll();
        if (messageType == WETETALK_TEXT) {
            message.set("msgtype", "text");
            JSONObject text = new JSONObject();
            text.set("content", request.getContent());
            boolean isContain = false;
            if(isNoticeAll&&ObjectUtil.isNull(userIdList)&&ObjectUtil.isNull(phoneList)){
                JSONArray userIdArray = new JSONArray();
                userIdArray.add("@all");
                text.set("mentioned_list", userIdArray);
            }
            if (!ObjectUtil.isNull(userIdList)) {
                JSONArray userIdArray = new JSONArray();
                userIdList.forEach(userIdArray::set);
                if (isNoticeAll) {
                    userIdArray.add("@all");
                    isContain = true;
                }
                text.set("mentioned_list", userIdArray);
            }
            if (!ObjectUtil.isNull(phoneList)) {
                JSONArray phoneArray = new JSONArray();
                phoneList.forEach(phoneArray::set);
                if (isNoticeAll && !isContain) {
                    phoneArray.add("@all");
                }
                text.set("mentioned_mobile_list",phoneArray);
            }
            message.set("text", text);
        } else if (messageType == WETETALK_MARKDOWN) {
            message.set("msgtype", "markdown");
            if(!Objects.isNull(userIdList)){
                userIdList.forEach(l -> content.append("<@").append(l).append(">"));
                content.append("\n");
            }
            content.append(request.getContent());
            JSONObject markdown = new JSONObject();
            markdown.set("content", content );
            markdown.set("title", request.getTitle());
            message.set("markdown", markdown);
        } else {
            log.error("输入的消息格式不对,message:"+messageType+"应该使用WETETALK前缀的消息类型");
        }
        return message;
    }

}
