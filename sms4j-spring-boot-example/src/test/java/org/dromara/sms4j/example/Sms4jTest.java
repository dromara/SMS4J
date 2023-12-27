package org.dromara.sms4j.example;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.lianlu.service.LianLuSmsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class Sms4jTest {

    /**
     * 填测试手机号
     */
    private static final String PHONE = "17897011002";

    @Test
    public void byLoadTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 通过负载均衡服务获取短信服务对象
        SmsResponse smsResponse = SmsFactory.getSmsBlend().sendMessage(PHONE, SmsUtils.getRandomInt(6));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void alibabaSmsTest() {
        // 阿里
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.ALIBABA).sendMessage(PHONE, SmsUtils.getRandomInt(6));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void huaweiSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 华为
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.HUAWEI).sendMessage(PHONE, SmsUtils.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void cloopenSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 容联云
        Map<String, String> messageMap = MapUtil.newHashMap(2, true);
        messageMap.put("captcha", SmsUtils.getRandomInt(4));
        messageMap.put("expirationInMinutes", "5");
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.CLOOPEN)
                .sendMessage(PHONE, "1", (LinkedHashMap<String, String>) messageMap);
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void emaySmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 亿美软通
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.EMAY).sendMessage(PHONE, SmsUtils.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void jdCloudSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 京东云
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.JDCLOUD).sendMessage(PHONE, SmsUtils.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void yunPianSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 云片
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.YUNPIAN).sendMessage(PHONE, SmsUtils.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void tencentSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        LinkedHashMap<String, String> newMap = SmsUtils.getNewMap();
        // 验证码
        newMap.put("1", SmsUtils.getRandomInt(4));
        // 有效时间
        newMap.put("2", "2");
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.TENCENT)
                .sendMessage(PHONE, "1603670", newMap);
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void uniSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 合一
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.UNISMS).sendMessage(PHONE, SmsUtils.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void cyYunSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 天翼云
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.CTYUN).sendMessage(PHONE, SmsUtils.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void neteaseSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 网易云短信
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.NETEASE).sendMessage(PHONE, SmsUtils.getRandomInt(6));
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
        String msg = StrUtil.format("【图书商城】您好，你的验证码是{}：（5分钟失效）", SmsUtils.getRandomInt(6));
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
        messages.put("code", SmsUtils.getRandomInt(6));
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
        String msg = StrUtil.format("【图书商城】您好，你的验证码是{}：（5分钟失效）", SmsUtils.getRandomInt(6));
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
        messages.put("code", SmsUtils.getRandomInt(6));
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.ZHUTONG).massTexting(ListUtil.of(PHONE, "180****1111"), "59264", messages);
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    /**
     * 联麓模板短信
     */
    @Test
    public void lianLuTemplateSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.LIANLU)
                .sendMessage(PHONE, SmsUtils.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    /**
     * 联麓普通短信
     */
    @Test
    public void lianLuNormalSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        LianLuSmsImpl lianLuSms = (LianLuSmsImpl) SmsFactory.getBySupplier(SupplierConstant.LIANLU);
        SmsResponse smsResponse = lianLuSms.sendNormalMessage(PHONE, "测试短信" + SmsUtils.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    /**
     * 鼎众普通短信
     */
    @Test
    public void dingZhongSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        SmsBlend dz = SmsFactory.getBySupplier(SupplierConstant.DINGZHONG);

        LinkedHashMap<String, String> messages = new LinkedHashMap<>();
        messages.put("code", SmsUtils.getRandomInt(6));

        ArrayList<String> phones = new ArrayList<>();
        phones.add(PHONE);
        phones.add(PHONE);

        SmsResponse smsResponse = dz.sendMessage(PHONE, "测试短信" + SmsUtils.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());

        SmsResponse smsResponse1 = dz.sendMessage(PHONE, messages);
        log.info(JSONUtil.toJsonStr(smsResponse1));
        Assert.isTrue(smsResponse1.isSuccess());

        SmsResponse smsResponse3 = dz.massTexting(phones, "测试短信" + SmsUtils.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse3));
        Assert.isTrue(smsResponse3.isSuccess());

        SmsResponse smsResponse4 = dz.massTexting(phones, "527551147741814784" ,messages);
        log.info(JSONUtil.toJsonStr(smsResponse4));
        Assert.isTrue(smsResponse4.isSuccess());
        
    }

}