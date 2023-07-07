package org.dromara.sms4j.core;

import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {

    /**
     * 反射获取接口对象的原类名
     */
    public static String getObjectName(SupplierConfig parameter) {
        return parameter.getClass().getTypeName();
    }

    /**
     * 将对象的属性和属性值变为map
     * */
    public static Map<String, String> getValues(SupplierConfig parameter) {
        try {
            Map<String ,String> map = new HashMap<>();
            Class<?> clazz = Class.forName(getObjectName(parameter));
            while(clazz != null) {
                Field[] declaredFields = clazz.getDeclaredFields();
                for(Field declaredField : declaredFields) {
                    declaredField.setAccessible(true);
                    map.put(declaredField.getName(), String.valueOf(declaredField.get(parameter)));
                }
                clazz = clazz.getSuperclass();
            }
            return map;
        } catch (Exception e) {
            throw new SmsBlendException(e.toString());
        }
    }
}
