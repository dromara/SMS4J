package org.dromara.sms4j.example;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.noear.solon.test.SolonJUnit5Extension;
import org.noear.solon.test.SolonTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author handy
 */
@Slf4j
@ExtendWith(SolonJUnit5Extension.class)
@SolonTest
public class SmsUtilsTest {

    @Test
    public void getRandomString() {
        String randomString = SmsUtils.getRandomString();
        log.info(randomString);
        Assert.isTrue(randomString.length() == 6);
    }

    @Test
    public void testGetRandomString() {
        String randomString = SmsUtils.getRandomString(4);
        log.info(randomString);
        Assert.isTrue(randomString.length() == 4);
    }

    @Test
    public void getRandomInt() {
        String randomInt = SmsUtils.getRandomInt(4);
        log.info(randomInt);
        Assert.isTrue(randomInt.length() == 4);
    }

    @Test
    public void isEmpty() {
        Assert.isTrue(SmsUtils.isEmpty(""));
    }

    @Test
    public void isNotEmpty() {
        Assert.isTrue(SmsUtils.isNotEmpty("not"));
    }

    @Test
    public void jsonForObject() {
        AlibabaConfig alibabaConfig = SmsUtils.jsonForObject("{'templateName':'Test'}", AlibabaConfig.class);
        Assert.isTrue(alibabaConfig.getTemplateName().equals("Test"));
    }

    @Test
    public void copyBean() {
        AlibabaConfig alibabaConfig = SmsUtils.jsonForObject("{'templateName':'Test'}", AlibabaConfig.class);
        AlibabaConfig alibabaConfig1 = new AlibabaConfig();
        SmsUtils.copyBean(alibabaConfig, alibabaConfig1);
        Assert.isTrue(alibabaConfig1.getTemplateName().equals("Test"));
    }

    @Test
    public void getNewMap() {
        SmsUtils.getNewMap();
    }

    @Test
    public void joinComma() {
        List<String> list = new ArrayList<>();
        list.add("12312341234");
        list.add("12312341235");
        String str = SmsUtils.joinComma(list);
        log.info(str);
        Assert.isTrue(str.equals("12312341234,12312341235"));
    }

    @Test
    public void addCodePrefixIfNot() {
        List<String> list = new ArrayList<>();
        list.add("12312341234");
        list.add("12312341235");
        String str = SmsUtils.addCodePrefixIfNot(list);
        log.info(str);
        Assert.isTrue(str.equals("+8612312341234,+8612312341235"));
    }

    @Test
    public void addCodePrefixIfNotToArray() {
        List<String> list = new ArrayList<>();
        list.add("12312341234");
        list.add("12312341235");
        String[] str = SmsUtils.addCodePrefixIfNotToArray(list);
        Assert.isTrue(str[0].equals("+8612312341234") && str[1].equals("+8612312341235"));
    }

}
