package org.dromara.sms4j.example;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.baidu.service.BaiduSmsImpl;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.danmi.service.DanMiSmsImpl;
import org.dromara.sms4j.jg.service.JgSmsImpl;
import org.dromara.sms4j.lianlu.service.LianLuSmsImpl;
import org.dromara.sms4j.luosimao.service.LuoSiMaoSmsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.noear.solon.test.SolonJUnit5Extension;
import org.noear.solon.test.SolonTest;

import java.util.*;

@Slf4j
@ExtendWith(SolonJUnit5Extension.class)
@SolonTest
public class Sms4jTest {

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

        SmsResponse smsResponse4 = dz.massTexting(phones, "" ,messages);
        log.info(JSONUtil.toJsonStr(smsResponse4));
        Assert.isTrue(smsResponse4.isSuccess());

    }

    /**
     * 中国移动 云MAS
     */
    @Test
    public void masSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 发送一对一/一对多普通短信
        // HTTP模式下 action请配置为 norsubmit
        // HTTPS模式下 action请配置为 submit
        SmsResponse oneToMany = SmsFactory.getBySupplier(SupplierConstant.MAS)
                .sendMessage(PHONE, "测试短信" + SmsUtils.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(oneToMany));

        // 发送多对多普通短信
        // HTTP模式下 action请配置为 norsubmit
        // HTTPS模式下 action请配置为 submit
        LinkedHashMap<String, String> content = new LinkedHashMap<>();
        content.put("18***1", "测试短信1");
        content.put("18***2", "测试短信2");
        content.put("18***3", "测试短信3");
        content.put("18***4", "测试短信4");
        SmsResponse manyToMany1 = SmsFactory.getBySupplier(SupplierConstant.MAS)
                .sendMessage(PHONE, content);
        log.info(JSONUtil.toJsonStr(manyToMany1));

        // 或者
        SmsResponse manyToMany2 = SmsFactory.getBySupplier(SupplierConstant.MAS)
                .sendMessage(PHONE, JSONUtil.toJsonStr(content));
        log.info(JSONUtil.toJsonStr(manyToMany2));

        // 发送模板短信
        // HTTP模式下或者HTTPS模式下 action请都配置为 tmpsubmit
        // 无参数
        SmsResponse strRes = SmsFactory.getBySupplier(SupplierConstant.MAS)
                .sendMessage(PHONE, StrUtil.EMPTY);
        log.info(JSONUtil.toJsonStr(strRes));

        //数组格式
        String[] paramsArr = {"param1", "param2"};
        SmsResponse arrRes = SmsFactory.getBySupplier(SupplierConstant.MAS)
                .sendMessage(PHONE, JSONUtil.toJsonStr(paramsArr));
        log.info(JSONUtil.toJsonStr(arrRes));

        //list格式
        List<String> paramsList = new ArrayList<>();
        paramsList.add("param1");
        paramsList.add("param2");
        SmsResponse listRes = SmsFactory.getBySupplier(SupplierConstant.MAS)
                .sendMessage(PHONE, JSONUtil.toJsonStr(paramsList));
        log.info(JSONUtil.toJsonStr(listRes));
    }

    /**
     * 百度短信
     */
    @Test
    public void baiduSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }

        // 发送短信
        SmsResponse resp = SmsFactory.getBySupplier(SupplierConstant.BAIDU)
                .sendMessage(PHONE, SmsUtils.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(resp));

        // 发送携带幂等性参数短信
        BaiduSmsImpl sendWithClientToken = (BaiduSmsImpl) SmsFactory.getBySupplier(SupplierConstant.BAIDU);
        String clientToken = UUID.fastUUID().toString(true);
        SmsResponse respWithClientToken = sendWithClientToken.sendMessageWithClientToken(PHONE,
                SmsUtils.getRandomInt(6),
                clientToken);
        log.info(JSONUtil.toJsonStr(respWithClientToken));
    }

    /**
     * 创蓝短信
     */
    @Test
    public void chungLanTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }

        //测试群发【模板】
        List<String> arrayList = new ArrayList<>();
        arrayList.add(PHONE);
        arrayList.add("135****000");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("1", "1544");
        map.put("2", "2222");
        SmsResponse smsResponse2 = SmsFactory.getBySupplier(SupplierConstant.CHUANGLAN).massTexting(arrayList, "[test]你的验证码是{$val},{$val}", map);
        log.info("smsResponse2:{}", smsResponse2);

        //测试单条发送
        SmsResponse smsResponse1 = SmsFactory.getBySupplier(SupplierConstant.CHUANGLAN).sendMessage(PHONE, "1544&2222");
        log.info("smsResponse1:{}", smsResponse1);

        //测试单条模板发送
        LinkedHashMap<String, String> map3 = new LinkedHashMap<>();
        map3.put("1", "1544");
        map3.put("2", "2222");
        SmsResponse smsResponse3 = SmsFactory.getBySupplier(SupplierConstant.CHUANGLAN).sendMessage(PHONE, "[test]你的验证码是{$val},{$val}", map3);
        log.info("smsResponse3:{}", smsResponse3);
    }

    /**
     * 极光短信
     */
    @Test
    public void jgSmsTest() {
        // 极光 发送文本验证码短信 API 不需要传入具体验证码 返回 {"msg_id": "288193860302"}
        SmsResponse smsResponse1 = SmsFactory.getBySupplier(SupplierConstant.JIGUANG).sendMessage(PHONE, "");
        Assert.isTrue(smsResponse1.isSuccess());

        // 极光 发送语音验证码短信 请确保action配置为voice_codes
        JgSmsImpl voiceCode = (JgSmsImpl) SmsFactory.getBySupplier(SupplierConstant.JIGUANG);
        SmsResponse voiceResp = voiceCode.sendVoiceCode(PHONE,
                SmsUtils.getRandomInt(6));
        Assert.isTrue(voiceResp.isSuccess());

        // 验证验证码是否有效 请确保action配置为voice_codes
        JgSmsImpl verify = (JgSmsImpl) SmsFactory.getBySupplier(SupplierConstant.JIGUANG);
        SmsResponse verifyResp = verify.verifyCode("123456", "288193860302");
        Assert.isTrue(verifyResp.isSuccess());

        // 极光 发送单条模板短信 API 发送自定义验证码 sendTemplateSMS
        LinkedHashMap<String, String> map1 = new LinkedHashMap<>();
        map1.put("code", "123456");
        SmsResponse smsResponse2 = SmsFactory.getBySupplier(SupplierConstant.JIGUANG).sendMessage(PHONE, map1);
        Assert.isTrue(smsResponse2.isSuccess());

        // 极光 发送单条模板短信 API 发送多参数自定义模板短信 sendTemplateSMS_with_multipleParameters
        LinkedHashMap<String, String> map2 = new LinkedHashMap<>();
        map2.put("name", "test");
        map2.put("password", "test");
        SmsResponse smsResponse3 = SmsFactory.getBySupplier(SupplierConstant.JIGUANG).sendMessage(PHONE, "226992", map2);
        Assert.isTrue(smsResponse3.isSuccess());

        // sendBatchTemplateSMS
        LinkedHashMap<String, String> map3 = new LinkedHashMap<>();
        map3.put("name", "test");
        map3.put("password", "test");
        List<String> phones = new ArrayList<>();
        phones.add(PHONE);
        phones.add("xxx");
        SmsResponse smsResponse4 = SmsFactory.getBySupplier(SupplierConstant.JIGUANG).massTexting(phones, "226992", map3);
        Assert.isTrue(smsResponse4.isSuccess());
    }

    /**
     * 螺丝帽短信
     */
    @Test
    public void luosimaoSmsTest() {
        // 螺丝帽 发送短信接口详细 发送短信接口详细 send.json
        SmsResponse smsResponse1 = SmsFactory.getBySupplier(SupplierConstant.LUO_SI_MAO).sendMessage(PHONE, "");
        Assert.isTrue(smsResponse1.isSuccess());

        // 螺丝帽 批量发送接口详细 send_batch.json
        SmsResponse smsResponse2 = SmsFactory.getBySupplier(SupplierConstant.LUO_SI_MAO).massTexting(Collections.singletonList(PHONE), "");
        Assert.isTrue(smsResponse2.isSuccess());

        // 螺丝帽 定时批量发送接口详细 send_batch.json
        LuoSiMaoSmsImpl luoSiMao = (LuoSiMaoSmsImpl) SmsFactory.getBySupplier(SupplierConstant.LUO_SI_MAO);
        SmsResponse smsResponse3 = luoSiMao.massTextingOnTime(Collections.singletonList(PHONE), "", new Date());
        Assert.isTrue(smsResponse3.isSuccess());

        // 螺丝帽 查询账户余额 status.json
        SmsResponse smsResponse4 = luoSiMao.queryAccountBalance();
        Assert.isTrue(smsResponse4.isSuccess());
    }

    /**
     * SUBMAIL短信
     */
    @Test
    public void mysubmailSmsTest() {
        // 短信发送 send.json 【xxxx】签名可配置 系统自动带入
        SmsResponse smsResponse1 = SmsFactory.getBySupplier(SupplierConstant.MY_SUBMAIL).sendMessage(PHONE, "【SUBMAIL】你好，你的验证码是2257");
        Assert.isTrue(smsResponse1.isSuccess());

        // 短信模板发送 xsend.json
        LinkedHashMap<String, String> vars = new LinkedHashMap<>();
        vars.put("code", "123456");
        SmsResponse smsResponse2 = SmsFactory.getBySupplier(SupplierConstant.MY_SUBMAIL).sendMessage(PHONE, "xxx", vars);
        Assert.isTrue(smsResponse2.isSuccess());

        // 短信一对多发送 multisend.json 【xxxx】签名可配置 系统自动带入 content字段说明：短信正文 案例没有说明无需传
        LinkedHashMap<String, String> vars1 = new LinkedHashMap<>();
        vars1.put("content", "【SUBMAIL】您的短信验证码：4438，请在10分钟内输入。");
        vars1.put("code", "123456");
        vars1.put("time", "10");
        SmsResponse smsResponse3 = SmsFactory.getBySupplier(SupplierConstant.MY_SUBMAIL).massTexting(Collections.singletonList(PHONE), JSONUtil.toJsonStr(vars1));
        Assert.isTrue(smsResponse3.isSuccess());

        // 短信模板一对多发送 multixsend.json
        LinkedHashMap<String, String> vars2 = new LinkedHashMap<>();
        vars2.put("code", "123456");
        SmsResponse smsResponse4 = SmsFactory.getBySupplier(SupplierConstant.MY_SUBMAIL).massTexting(Collections.singletonList(PHONE), "xxx", vars2);
        Assert.isTrue(smsResponse4.isSuccess());

        // 短信批量群发 batchsend.json 【xxxx】签名可配置 系统自动带入 content字段说明：短信正文 案例没有说明无需传
        LinkedHashMap<String, String> vars3 = new LinkedHashMap<>();
        vars3.put("content", "123456");
        SmsResponse smsResponse5 = SmsFactory.getBySupplier(SupplierConstant.MY_SUBMAIL).massTexting(Collections.singletonList(PHONE), JSONUtil.toJsonStr(vars3));
        Assert.isTrue(smsResponse5.isSuccess());

        // 短信批量模板群发 batchxsend.json
        LinkedHashMap<String, String> vars4 = new LinkedHashMap<>();
        vars4.put("code", "123456");
        vars4.put("time", "10");
        SmsResponse smsResponse6 = SmsFactory.getBySupplier(SupplierConstant.MY_SUBMAIL).massTexting(Collections.singletonList(PHONE), "xxx", vars4);
        Assert.isTrue(smsResponse6.isSuccess());
    }

    /**
     * danmi短信
     */
    @Test
    public void danmiSmsTest() {
        // 短信发送 distributor/sendSMS 群发 massTexting
        SmsResponse smsResponse1 = SmsFactory.getBySupplier(SupplierConstant.DAN_MI).sendMessage(PHONE, "【danmi】你好，你的验证码是666");
        Assert.isTrue(smsResponse1.isSuccess());

        DanMiSmsImpl danMiSms = (DanMiSmsImpl) SmsFactory.getBySupplier(SupplierConstant.DAN_MI);
        // 短信余额查询 distributor/user/query
        SmsResponse smsResponse2 = danMiSms.queryBalance();
        Assert.isTrue(smsResponse2.isSuccess());

        // 语音验证码发送 voice/voiceCode
        SmsResponse smsResponse3 = danMiSms.voiceCode(PHONE, "666");
        Assert.isTrue(smsResponse3.isSuccess());

        // 语音通知文件发送 voice/voiceNotify
        SmsResponse smsResponse4 = danMiSms.voiceNotify(PHONE, "sjkhduiq");
        Assert.isTrue(smsResponse4.isSuccess());

        // 语音模板通知发送 voice/voiceTemplate
        SmsResponse smsResponse5 = danMiSms.voiceTemplate(PHONE, "opipedlqza", "111,222,333");
        Assert.isTrue(smsResponse5.isSuccess());
    }
}
