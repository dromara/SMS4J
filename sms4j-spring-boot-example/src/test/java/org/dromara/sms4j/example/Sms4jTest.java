package org.dromara.sms4j.example;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.service.AlibabaSmsImpl;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.cloopen.service.CloopenSmsImpl;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.ctyun.service.CtyunSmsImpl;
import org.dromara.sms4j.emay.service.EmaySmsImpl;
import org.dromara.sms4j.huawei.service.HuaweiSmsImpl;
import org.dromara.sms4j.jdcloud.service.JdCloudSmsImpl;
import org.dromara.sms4j.netease.service.NeteaseSmsImpl;
import org.dromara.sms4j.tencent.service.TencentSmsImpl;
import org.dromara.sms4j.unisms.service.UniSmsImpl;
import org.dromara.sms4j.yunpian.service.YunPianSmsImpl;
import org.dromara.sms4j.zhutong.service.ZhutongSmsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;

@Slf4j
@SpringBootTest
class Sms4jTest {

    /**
     * 填测试手机号
     */
    private static final String PHONE = "";

    @Test
    public void byLoadTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 通过负载均衡服务获取短信服务对象
        SmsResponse smsResponse = SmsFactory.getSmsBlend().sendMessage(PHONE, SmsUtil.getRandomInt(6));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void alibabaSmsTest() {
        // 阿里
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.ALIBABA).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void huaweiSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 华为
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.HUAWEI).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void cloopenSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 容联云
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.CLOOPEN).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void emaySmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 亿美软通
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.EMAY).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void jdCloudSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 京东云
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.JDCLOUD).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void yunPianSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 云片
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.YUNPIAN).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void tencentSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 腾讯
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.TENCENT).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void uniSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 合一
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.UNISMS).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void cyYunSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 天翼云
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.CTYUN).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void neteaseSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 网易云短信
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.NETEASE).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    /**
     * 助通短信测试1：无模板
     */
    @Test
    public void zhutongSms1Test() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 助通短信短信
        String msg = StrUtil.format("【图书商城】您好，你的验证码是{}：（5分钟失效）", SmsUtil.getRandomInt(6));
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.ZHUTONG).sendMessage(PHONE, msg);
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    /**
     * 助通短信测试2：有模板
     */
    @Test
    public void zhutongSmsTest2Template() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 助通短信短信
        LinkedHashMap<String, String> messages = new LinkedHashMap<>(1);
        messages.put("code", SmsUtil.getRandomInt(6));
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.ZHUTONG).sendMessage(PHONE, "59264", messages);
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    /**
     * 助通短信测试3：无模板群发
     */
    @Test
    public void zhutongSms3MoreTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 助通短信短信
        String msg = StrUtil.format("【图书商城】您好，你的验证码是{}：（5分钟失效）", SmsUtil.getRandomInt(6));
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.ZHUTONG).massTexting(ListUtil.of(PHONE, "180****1111"), msg);
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    /**
     * 助通短信测试4：有模板 多人群发
     */
    @Test
    public void zhutongSms4TemplateTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 助通短信短信
        LinkedHashMap<String, String> messages = new LinkedHashMap<>(1);
        messages.put("code", SmsUtil.getRandomInt(6));
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.ZHUTONG).massTexting(ListUtil.of(PHONE, "180****1111"), "59264", messages);
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

}