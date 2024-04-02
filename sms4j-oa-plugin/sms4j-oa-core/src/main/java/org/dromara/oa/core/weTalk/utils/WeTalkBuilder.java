package org.dromara.oa.core.weTalk.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.entity.WeTalkRequestArticle;
import org.dromara.oa.comm.enums.MessageType;
import org.dromara.oa.comm.errors.OaException;

import java.util.List;
import java.util.Objects;

import static org.dromara.oa.comm.enums.MessageType.WE_TALK_MARKDOWN;
import static org.dromara.oa.comm.enums.MessageType.WE_TALK_NEWS;
import static org.dromara.oa.comm.enums.MessageType.WE_TALK_TEXT;


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
        if (messageType == WE_TALK_TEXT) {
            if (Objects.isNull(request.getContent())) {
                throw new OaException("消息体content不能为空");
            }
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
        } else if (messageType == WE_TALK_MARKDOWN) {
            if (Objects.isNull(request.getContent())) {
                throw new OaException("消息体content不能为空");
            }
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
        } else if (messageType == WE_TALK_NEWS) {
            message.set("msgtype", "news");
            JSONObject news = new JSONObject();
            JSONArray articles = new JSONArray();
            List<WeTalkRequestArticle> articleList = request.getArticleList();
            if(!articleList.isEmpty()){
                articleList.forEach(article -> {
                    JSONObject articleJson = new JSONObject();
                    articleJson.set("title", article.getTitle());
                    articleJson.set("description", article.getDescription());
                    articleJson.set("url", article.getUrl());
                    articleJson.set("picurl", article.getPicUrl());
                    articles.add(articleJson);
                });
            }
            news.set("articles", articles );
            message.set("news", news);
        } else {
            log.error("输入的消息格式不对,message:"+messageType+"应该使用WE_TALK前缀的消息类型");
        }
        return message;
    }

}
