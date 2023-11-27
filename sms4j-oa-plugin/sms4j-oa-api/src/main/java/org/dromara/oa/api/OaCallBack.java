package org.dromara.oa.api;

import org.dromara.oa.comm.entity.Response;

/**
 * @author dongfeng
 * @date 2023-10-28 14:26
 */
@FunctionalInterface
public interface OaCallBack {
    void callBack(Response smsResponse);
}
