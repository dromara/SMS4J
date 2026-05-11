package org.dromara.sms4j.comm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

class SmsHttpUtilsTest {

    @Test
    void shouldReuseProxyInstanceByHostAndPort() {
        SmsHttpUtils first = SmsHttpUtils.instance("127.0.0.1", 8080);
        SmsHttpUtils second = SmsHttpUtils.instance("127.0.0.1", 8080);

        assertSame(first, second);
    }

    @Test
    void shouldAllowDifferentProxyInstances() {
        SmsHttpUtils first = SmsHttpUtils.instance("127.0.0.1", 8080);
        SmsHttpUtils second = SmsHttpUtils.instance("127.0.0.1", 8081);

        assertNotSame(first, second);
    }

    @Test
    void shouldSeparateInstancesByTimeout() {
        SmsHttpUtils first = SmsHttpUtils.instance("127.0.0.1", 8080, 1000);
        SmsHttpUtils second = SmsHttpUtils.instance("127.0.0.1", 8080, 2000);

        assertNotSame(first, second);
    }
}
