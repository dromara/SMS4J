package org.dromara.oa.api;

import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.entity.Response;
import org.dromara.oa.comm.enums.MessageType;

public interface OaSender {

    /**
     * 获取通知webhook实例唯一标识
     */
    String getConfigId();

    /**
     * 获取供应商标识
     */
    String getSupplier();

    /**
     * 发送消息
     */
    Response sender(Request request, MessageType messageType);
}
