package org.dromara.oa.comm.entity;

import lombok.Data;

@Data
public class Response {
    /**
     * 响应码
     */
    private String code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应数据
     */
    private String data;
}
