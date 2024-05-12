package org.dromara.sms4j.jiguang.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

@Data
@EqualsAndHashCode(callSuper = true)
public class JiguangConfig extends BaseConfig {
    /**
     * appKey
     */
    private String appKey;
    /**
     * masterSecret
     */
    private String masterSecret;
    /**
     * signid
     */
    private String signId;
    /**
     * requestUrl
     */
    private String requestUrl = "https://api.sms.jpush.cn/v1";
    /**
     * 发送文本验证码短信 API
     * curl --insecure -X POST -v https://api.sms.jpush.cn/v1/codes -H "Content-Type: application/json" \
     * -u "7d431e42dfa6a6d693ac2d04:5e987ac6d2e04d95a9d8f0d1" -d '{"mobile":"xxxxxxxxxxx","sign_id":*,"temp_id":*}'
     */
    private String codeAction = "codes";
    /**
     * 发送单条模板短信 API
     * curl --insecure -X POST -v https://api.sms.jpush.cn/v1/messages -H "Content-Type: application/json" -u "7d431e42dfa6a6d693ac2d04:5e987ac6d2e04d95a9d8f0d1" \
     * -d '{"mobile":"xxxxxxxxxxxxxx","sign_id":*,"temp_id":1,"temp_para":{"xxxx":"xxxx"}}'
     */
    private String singleTemplateAction = "messages";
    /**
     * 发送批量模板短信 API
     * curl --insecure -X POST -v https://api.sms.jpush.cn/v1/messages/batch -H "Content-Type: application/json" -u "7d431e42dfa6a6d693ac2d04:5e987ac6d2e04d95a9d8f0d1" -d \
     * '{
     *     "sign_id": *,
     *     "temp_id": 1250,
     *     "tag":"标签",
     *     "recipients": [
     *         {
     *             "mobile": "13812345678",
     *             "temp_para": {
     *                 "number": "741627"
     *             }
     *         },
     *         {
     *             "mobile": "18603050709",
     *             "temp_para": {
     *                 "number": "147721"
     *             }
     *         }
     *     ]
     * }'
     */
    private String batchTemplateAction = "messages/batch";
    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.JIGUANG;
    }
}
