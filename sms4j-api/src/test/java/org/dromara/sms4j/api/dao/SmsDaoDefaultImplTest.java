package org.dromara.sms4j.api.dao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SmsDaoDefaultImplTest {

    @Test
    void shouldReturnStoredValueBeforeExpiration() {
        SmsDaoDefaultImpl dao = SmsDaoDefaultImpl.getInstance();
        dao.clean();

        dao.set("code", "1234", 60);

        assertEquals("1234", dao.get("code"));
        dao.clean();
    }

    @Test
    void shouldNotReturnExpiredValue() {
        SmsDaoDefaultImpl dao = SmsDaoDefaultImpl.getInstance();
        dao.clean();

        dao.set("code", "1234", 0);

        assertNull(dao.get("code"));
        dao.clean();
    }
}
