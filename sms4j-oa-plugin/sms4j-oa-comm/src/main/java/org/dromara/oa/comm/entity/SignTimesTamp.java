package org.dromara.oa.comm.entity;

import lombok.Data;

@Data
public class SignTimesTamp {
    String sign;

    Long timestamp;

    public SignTimesTamp(String newSign, Long timestamp) {
        this.sign = newSign;
        this.timestamp = timestamp;
    }
}
