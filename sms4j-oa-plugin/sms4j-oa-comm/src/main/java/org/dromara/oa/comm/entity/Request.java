package org.dromara.oa.comm.entity;


import lombok.Data;
import org.dromara.oa.comm.enums.MessageType;

import java.util.List;

@Data
public class Request implements Comparable<Request> {
    // 标题
    private String title;

    // 消息内容
    private String content;

    // link类型的参数
    private String picUrl;

    // link类型的参数
    private String messageUrl;

    private List<String> phoneList;

    // 用于@,唯一标识:userId/openId
    private List<String> userIdList;

    private List<String> userNamesList;

    private Boolean isNoticeAll = false;

    // oa类型
    private String oaType;

    // 优先级
    private Integer priority;

    // 消息类型,用于优先级队列
    private MessageType messageType;

    @Override
    public int compareTo(Request other) {
        return Integer.compare(other.priority,this.priority);
    }
}
