package org.dromara.oa.api;

import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.entity.Response;
import org.dromara.oa.comm.enums.MessageType;

public interface OaSender {
    Response sender(Request request, MessageType messageType);
}
