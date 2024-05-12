package org.dromara.sms4j.comm.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author wind
 */
public class SmsUtils {
    private SmsUtils() {
    }   //私有构造防止实例化

    /**
     * <p>说明：生成一个指定长度的随机字符串，包含大小写英文字母和数字但不包含符号
     *
     * @param len 要生成的字符串的长度
     *            getRandomString
     * @author :Wind
     */
    public static String getRandomString(int len) {
        return RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER + RandomUtil.BASE_CHAR.toUpperCase(), len);
    }

    /**
     * <p>说明：获取一个长度为6的随机字符串
     * getRandomString
     *
     * @author :Wind
     */
    public static String getRandomString() {
        return getRandomString(6);
    }

    /**
     * <p>说明：生成一个指定长度的只有数字组成的随机字符串
     *
     * @param len 要生成的长度
     *            getRandomInt
     * @author :Wind
     */
    public static String getRandomInt(int len) {
        return RandomUtil.randomString(RandomUtil.BASE_NUMBER, len);
    }

    /**
     * 指定元素是否为null或者空字符串
     *
     * @param str 指定元素
     * @return 是否为null或者空字符串
     * @author :Wind
     */
    public static boolean isEmpty(Object str) {
        return ObjectUtil.isEmpty(str);
    }

    /**
     * 指定元素是否不为 (null或者空字符串)
     *
     * @param str 指定元素
     * @return 是否为null或者空字符串
     * @author :Wind
     */
    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    /**
     * jsonForObject
     * <p>将json字符串转化为指定的对象
     *
     * @author :Wind
     */
    public static <T> T jsonForObject(String json, Class<T> t) {
        return JSONUtil.toBean(json, t);
    }

    /**
     * copyBean
     * <p>拷贝bean，只有源对象不为null才会拷贝
     *
     * @param t 源对象
     * @param m 目标对象
     * @author :Wind
     */
    public static <T, M> void copyBean(T t, M m) {
        BeanUtil.copyProperties(t, m);
    }

    /**
     * getNewMap
     * <p>获取一个新的空LinkedHashMap
     *
     * @return 空的 LinkedHashMap 实例
     * @author :Wind
     */
    public static LinkedHashMap<String, String> getNewMap() {
        return new LinkedHashMap<>();
    }

    /**
     * listToString
     * <p>将list转化为string，元素之间使用逗号分隔，此方法只支持list内部元素为String类型的
     *
     * @param list 要转换的list
     * @author :Wind
     */
    public static String listToString(List<String> list) {
        return CollUtil.join(list, ",");
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串
     *
     * @param list 集合
     * @return 结果字符串
     */
    public static String arrayToString(List<String> list) {
        return CollUtil.join(list, ",", str -> StrUtil.addPrefixIfNot(str, "+86"));
    }

    /**
     * List +86后转 数组
     *
     * @param list 集合
     * @return 结果字符串
     */
    public static String[] listToArray(List<String> list) {
        List<String> toStr = new ArrayList<>();
        for (String s : list) {
            toStr.add(StrUtil.addPrefixIfNot(s, "+86"));
        }
        return toStr.toArray(new String[list.size()]);
    }

    /**
     * 将Map中所有key的分隔符转换为新的分隔符
     * @param map map对象
     * @param separator 旧分隔符
     * @param newSeparator 新分隔符
     */
    public static void replaceKeysSeparator(Map<String, Object> map, String separator, String newSeparator) {
        if(CollUtil.isEmpty(map)) {
            return;
        }
        List<String> keySet = new ArrayList<>(map.keySet());
        for(String key : keySet) {
            if(StrUtil.isEmpty(key) || !key.contains(separator)) {
                continue;
            }
            String value = String.valueOf(map.get(key));
            String newKey = key.replaceAll(separator, newSeparator);
            map.putIfAbsent(newKey, value);
            map.remove(key);
        }
    }

    public static boolean isClassExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static LinkedHashMap<String, String> buildMessageByAmpersand(String message) {
        if (isEmpty(message)){
            return new LinkedHashMap<>();
        }
        String[] split = message.split("&");
        LinkedHashMap<String, String> map = new LinkedHashMap<>(split.length);
        for (int i = 0; i < split.length; i++) {
            map.put(String.valueOf(i), split[i]);
        }
        return map;
    }

    /**
     * 将任意类型集合转成想要的数组
     * @param list 需要转换的集合
     * @param predicate 过滤条件
     * @param mapper 对此流的元素执行函数
     * @param array 想要的数组
     * @return 数组
     * @param <T> 集合泛型
     * @param <E> 想要的数组类型
     */
    public static <E, T> E[] toArray(Collection<T> list, Predicate<T> predicate, Function<? super T, ? extends E> mapper, E[] array) {
        if (isEmpty(list)) {
            return array.clone();
        }
        return list.stream().filter(predicate).map(mapper).toArray(size -> array.clone());
    }
}