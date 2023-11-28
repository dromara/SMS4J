package org.dromara.oa.comm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 厂商原返回体
     */
    private Object data;

    /**
     * 配置标识名 如未配置取对应渠道名例如
     */
    private String oaConfigId;
}
