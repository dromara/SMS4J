package org.dromara.sms4j.core.factory;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.callback.CallBack;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SmsFactoryTest {

    @Test
    void registerIfAbsentShouldOnlyRegisterOnceAndUnregisterSafely() {
        SmsBlend blend = new StubSmsBlend("test-config-id");

        assertTrue(SmsFactory.registerIfAbsent(blend));
        assertFalse(SmsFactory.registerIfAbsent(blend));
        assertSame(blend, SmsFactory.getSmsBlend("test-config-id"));
        assertTrue(SmsFactory.unregister("test-config-id"));
        assertFalse(SmsFactory.unregister("test-config-id"));
        assertNull(SmsFactory.getSmsBlend("test-config-id"));
    }

    private static class StubSmsBlend implements SmsBlend {
        private final String configId;

        private StubSmsBlend(String configId) {
            this.configId = configId;
        }

        @Override
        public String getConfigId() {
            return configId;
        }

        @Override
        public String getSupplier() {
            return "test";
        }

        @Override
        public SmsResponse sendMessage(String phone, String message) {
            return new SmsResponse();
        }

        @Override
        public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
            return new SmsResponse();
        }

        @Override
        public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
            return new SmsResponse();
        }

        @Override
        public SmsResponse massTexting(List<String> phones, String message) {
            return new SmsResponse();
        }

        @Override
        public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
            return new SmsResponse();
        }

        @Override
        public void sendMessageAsync(String phone, String message, CallBack callBack) {
        }

        @Override
        public void sendMessageAsync(String phone, String message) {
        }

        @Override
        public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, CallBack callBack) {
        }

        @Override
        public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages) {
        }

        @Override
        public void delayedMessage(String phone, String message, Long delayedTime) {
        }

        @Override
        public void delayedMessage(String phone, String templateId, LinkedHashMap<String, String> messages, Long delayedTime) {
        }

        @Override
        public void delayMassTexting(List<String> phones, String message, Long delayedTime) {
        }

        @Override
        public void delayMassTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime) {
        }
    }
}
