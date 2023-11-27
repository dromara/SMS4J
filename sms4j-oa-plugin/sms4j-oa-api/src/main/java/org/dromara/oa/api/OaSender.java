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

    /**
     * 异步(回调)
     */
    void senderAsync(Request request, MessageType messageType);

    /**
     * 异步(不回调)
     * @param request oa请求体
     * @param messageType 消息类型
     * @param callBack 回调方法
     */
    void senderAsync(Request request, MessageType messageType, OaCallBack callBack);

    /**
     * 发送带优先级的消息
     * @param request oa请求体
     * @param messageType 消息类型
     */

    void senderAsyncByPriority(Request request, MessageType messageType);
}
