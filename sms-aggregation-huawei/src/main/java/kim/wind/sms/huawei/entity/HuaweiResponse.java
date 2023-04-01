package kim.wind.sms.huawei.entity;

import lombok.Data;

import java.util.List;

/**
 * HuaweiResponse
 * <p> 华为响应参数
 *
 * @author :Wind
 * 2023/3/31  22:20
 **/
@Data
public class HuaweiResponse {

    /** 请求返回的结果码*/
    private String code;

    /** 请求返回的结果码描述*/
    private String description;

    /** 短信ID列表，当目的号码存在多个时，每个号码都会返回一个SmsID。
     当返回异常响应时不携带此字段*/
    private List<SmsId> result;
}
