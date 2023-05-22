package org.dromara.sms4j.comm.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author wind
 */
public class SmsUtil {
    private SmsUtil() {
    }   //私有构造防止实例化

    /**
     * <p>说明：生成一个指定长度的随机字符串，包含大小写英文字母和数字但不包含符号
     *
     * @param len 要生成的字符串的长度
     * getRandomString
     * @author :Wind
     */
    public static String getRandomString(int len) {
        return RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER + RandomUtil.BASE_CHAR.toUpperCase(), len);
    }

    /**
     * <p>说明：获取一个长度为6的随机字符串
     *getRandomString
     * @author :Wind
     */
    public static String getRandomString() {
        return getRandomString(6);
    }

    /**
     * <p>说明：生成一个指定长度的只有数字组成的随机字符串
     * @param len 要生成的长度
     * getRandomInt
     * @author :Wind
     */
    public static String getRandomInt(int len) {
        return RandomUtil.randomString(RandomUtil.BASE_NUMBER, len);
    }

    /**
     * 指定元素是否为null或者空字符串
     * @param str 指定元素
     * @return 是否为null或者空字符串
     * @author :Wind
     */
    public static boolean isEmpty(Object str) {
        return ObjectUtil.isEmpty(str);
    }

    /**
     * 指定元素是否不为 (null或者空字符串)
     * @param str 指定元素
     * @return 是否为null或者空字符串
     * @author :Wind
     */
    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    /**
     *  listToString
     * <p>将list转化为string，元素之间使用逗号分隔，此方法只支持list内部元素为String类型的
     * @param list 要转换的list
     * @author :Wind
    */
    public static String listToString(List<String> list) {
        return CollUtil.join(list, ",");
    }

    /**
     *  jsonForObject
     * <p>将json字符串转化为指定的对象
     * @author :Wind
    */
    public static <T> T jsonForObject(String json, Class<T> t) {
        return JSONUtil.toBean(json,t);
    }

    /**
     *  copyBean
     * <p>拷贝bean，只有源对象不为null才会拷贝
     * @param t 源对象
     * @param m 目标对象
     * @author :Wind
    */
    public static <T,M>void copyBean(T t,M m){
        BeanUtil.copyProperties(t, m);
    }

    /**
     *  getNewMap
     * <p>获取一个新的空LinkedHashMap
     * @return 空的 LinkedHashMap 实例
     * @author :Wind
    */
    public static LinkedHashMap<String,String> getNewMap(){
        return new LinkedHashMap<>();
    }

}
