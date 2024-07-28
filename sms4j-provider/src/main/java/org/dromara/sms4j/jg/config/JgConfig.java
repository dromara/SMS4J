package org.dromara.sms4j.jg.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * <p>类名: JgConfig
 * <p>说明：极光 sms
 *
 * @author :SmartFire
 * 2024/3/15
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class JgConfig extends BaseConfig {
    /**
     * 签名 ID，该字段为空则使用应用默认签名
     */
    private String signId;

    /**
     * 调用地址
     */
    private String requestUrl = Constant.HTTPS_PREFIX + "api.sms.jpush.cn/v1/";

    /**
     * 默认请求方法 messages
     * 发送文本验证码短信 codes
     * 发送语音验证码短信 voice_codes
     * 验证验证码是否有效 valid
     * 注意：此处直接写valid即为验证码验证请求 系统会自动补充完整请求地址为codes/{msg_id}/valid (注:msg_id 为调用发送验证码 API 的返回值)
     * 发送单条模板短信 messages
     * 发送批量模板短信 messages/batch
     */
    private String action = "messages";

    /**
     * 模板变量名称
     */
    private String templateName;

    /**
     * action设置为voice_codes有效
     * 语音验证码播报语言选择，0：中文播报，1：英文播报，2：中英混合播报
     */
    private String voice;

    /**
     * action设置为voice_codes有效
     * 验证码有效期，默认为 60 秒
     */
    private Integer ttl = 60;

    /**
     * action设置为messages/batch有效
     * 标签
     */
    private String tag;

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
