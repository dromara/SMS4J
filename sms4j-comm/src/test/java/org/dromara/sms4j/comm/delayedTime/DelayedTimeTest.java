package org.dromara.sms4j.comm.delayedTime;

import org.junit.jupiter.api.Test;

import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DelayedTimeTest {

    @Test
    void shouldRunScheduledTask() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        new DelayedTime().schedule(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 10);

        assertTrue(latch.await(1, TimeUnit.SECONDS));
    }
}
