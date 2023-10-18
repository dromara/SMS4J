package org.dromara.oa.comm.entity;


import lombok.Data;

import java.util.List;

@Data
public class Request {
    // 标题
    private String title;

    // 消息内容
    private String content;

    private List<String> phones;

    // oa类型
    private String  oaType;

}
