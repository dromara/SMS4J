package org.dromara.sms4j.comm.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SmsUtilsTest {

    @Test
    void addCodePrefixIfNotShouldNotMutateInputList() {
        List<String> phones = new ArrayList<>(Arrays.asList("13800138000", "+8613900139000"));

        List<String> result = SmsUtils.addCodePrefixIfNot(phones);

        assertEquals(Arrays.asList("13800138000", "+8613900139000"), phones);
        assertEquals(Arrays.asList("+8613800138000", "+8613900139000"), result);
    }
}
