package org.dromara.sms4j.core.proxy;

import org.dromara.sms4j.api.proxy.SmsProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SmsProxyFactoryTest {

    @Test
    void addPreProcessorShouldReplaceProcessorWithSameType() {
        TestProcessor first = new TestProcessor();
        TestProcessor second = new TestProcessor();

        SmsProxyFactory.addPreProcessor(first);
        SmsProxyFactory.addPreProcessor(second);

        long count = SmsProxyFactory.getProcessors().stream()
                .filter(processor -> processor instanceof TestProcessor)
                .count();

        assertEquals(1, count);
        SmsProxyFactory.removePreProcessor(second);
    }

    private static class TestProcessor implements SmsProcessor {
        @Override
        public int getOrder() {
            return 0;
        }
    }
}
