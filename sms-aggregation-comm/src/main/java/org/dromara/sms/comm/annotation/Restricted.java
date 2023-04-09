package org.dromara.sms.comm.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>类名: Restricted
 * <p>说明：  发送短信限制
 *
 * @author :Wind
 * 2023/3/26  17:12
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Restricted {
}
