package org.dromara.sms4j.example.zhangjun;

import org.dromara.sms4j.core.factory.SmsFactory;
import org.noear.solon.Solon;

/**
 * 自定义广州掌骏短信实现
 *
 * @author 4n
 */
public class ZhangJunApp {

    public static void main(String[] args) {
        Solon.start(ZhangJunApp.class, args);
        SmsFactory.getBySupplier("zhangjun").sendMessage("17*****598", "154468");
    }

}
