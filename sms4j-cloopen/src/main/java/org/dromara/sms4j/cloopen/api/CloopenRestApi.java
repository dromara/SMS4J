package org.dromara.sms4j.cloopen.api;

import com.dtflys.forest.annotation.Address;
import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.PostRequest;

import java.util.Map;

/**
 * 容联云 REST API
 *
 * @author Charles7c
 * @since 2023/4/17 20:57
 */
@Address(basePath = "{baseUrl}")
@BaseRequest(headers = {
        "Accept: application/json",
        "Content-Type: application/json;charset=utf-8",
        "Authorization: {authorization}",
})
public interface CloopenRestApi {

    /**
     * 发送模板短信
     *
     * @param paramMap 请求参数
     * @return 响应结果
     */
    @PostRequest("/Accounts/{accessKeyId}/SMS/TemplateSMS?sig={sign}")
    Map<String, Object> sendSms(@Body Map<String, Object> paramMap);
}
