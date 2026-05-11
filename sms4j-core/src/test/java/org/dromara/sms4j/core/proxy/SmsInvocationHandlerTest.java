package org.dromara.sms4j.core.proxy;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.callback.CallBack;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.proxy.SmsProcessor;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertSame;

class SmsInvocationHandlerTest {

    @Test
    void shouldUnwrapInvocationTargetExceptionBeforeExceptionProcessor() throws Throwable {
        CapturingProcessor processor = new CapturingProcessor();
        SmsProxyFactory.addPreProcessor(processor);
        SmsBlend blend = new ThrowingSmsBlend();
        Method method = SmsBlend.class.getMethod("sendMessage", String.class, String.class);

        new SmsInvocationHandler(blend).invoke(blend, method, new Object[]{"13800138000", "test"});

        assertSame(ThrowingSmsBlend.EXCEPTION, processor.exception.get());
        SmsProxyFactory.removePreProcessor(processor);
    }

    private static class CapturingProcessor implements SmsProcessor {
        private final AtomicReference<Exception> exception = new AtomicReference<>();

        @Override
        public Object[] preProcessor(Method method, Object source, Object[] param) {
            return param;
        }

        @Override
        public void exceptionHandleProcessor(Method method, Object source, Object[] param, Exception exception) {
            this.exception.set(exception);
        }

        @Override
        public int getOrder() {
            return 0;
        }
    }

    private static class ThrowingSmsBlend implements SmsBlend {
        private static final SmsBlendException EXCEPTION = new SmsBlendException("expected");

        @Override
        public String getConfigId() {
            return "throwing";
        }

        @Override
        public String getSupplier() {
            return "throwing";
        }

        @Override
        public SmsResponse sendMessage(String phone, String message) {
            throw EXCEPTION;
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
