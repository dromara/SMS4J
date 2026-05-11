package org.dromara.sms4j.provider.config;

import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BaseConfigTest {

    @Test
    void shouldValidateTimeout() {
        TestConfig config = new TestConfig();

        config.setTimeout(3000);

        assertEquals(3000, config.getTimeout());
        assertThrows(SmsBlendException.class, () -> config.setTimeout(0));
    }

    private static class TestConfig extends BaseConfig {
        @Override
        public String getSupplier() {
            return "test";
        }
    }
}
