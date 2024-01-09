package org.dromara.email.jakarta.comm.utils;

import org.dromara.email.jakarta.comm.entity.Parameter;
import org.dromara.email.jakarta.comm.errors.MailException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {

    /**
     * 反射获取接口对象的原类名
     */
    public static String getObjectName(Parameter parameter) {
        return parameter.getClass().getTypeName();
    }

    /**
     * 将对象的属性和属性值变为map
     * */
    public static Map<String, String> getValues(Parameter parameter) {
        try {
            Map<String ,String> map = new HashMap<>();
            Class<?> clazz = Class.forName(getObjectName(parameter));
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                map.put(declaredField.getName(), (String) declaredField.get(parameter));
            }
            return map;
        } catch (Exception e) {
            throw new MailException(e);
        }
    }
}
