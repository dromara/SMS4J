package org.dromara.oa.core.weTalk.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.enums.MessageType;

import java.util.List;

import static org.dromara.oa.comm.enums.MessageType.MARKDOWN;
import static org.dromara.oa.comm.enums.MessageType.TEXT;

/**
 * 企业微信--签名和消息构建
 * @author dongfeng
 * 2023-10-19 13:07
 */
public class WeTalkBuilder {


    public static JSONObject createWeTalkMessage(Request request, MessageType messageType) {

        JSONObject message = new JSONObject();
        if (messageType == TEXT) {
            message.set("msgtype", "text");
            JSONObject text = new JSONObject();
            text.set("content", request.getContent());
            boolean isContain = false;
            List<String> userIdList = request.getUserIdList();
            List<String> phoneList = request.getPhoneList();
            Boolean isNoticeAll = request.getIsNoticeAll();
            if (!ObjectUtil.isNull(userIdList)) {
                if (isNoticeAll) {
                    userIdList.add("@all");
                    isContain = true;
                }
                text.set("mentioned_list", userIdList.toArray());
            }
            if (!ObjectUtil.isNull(phoneList)) {
                if (isNoticeAll && !isContain) {
                    phoneList.add("@all");
                }
                text.set("mentioned_mobile_list", phoneList.toArray());
            }
            message.set("text", text);
        } else if (messageType == MARKDOWN) {
            message.set("msgtype", "markdown");
            JSONObject markdown = new JSONObject();
            markdown.set("content", request.getContent());
            markdown.set("title", request.getTitle());
            message.set("markdown", markdown);
        }
        return message;
    }

}
