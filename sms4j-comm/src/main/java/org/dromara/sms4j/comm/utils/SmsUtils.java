package org.dromara.sms4j.comm.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
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
    public static String joinComma(List<String> list) {
        return CollUtil.join(list, StrUtil.COMMA);
    }

    /**
     * 切分字符串
     *
     * @param str 被切分的字符串
     * @return 分割后的数据列表
     */
    public static List<String> splitTrimComma(String str) {
        return StrUtil.splitTrim(str, StrUtil.COMMA);
    }

    /**
     * 将手机号码 添加+86中国的电话国际区号前缀
     *
     * @param phones 手机号码集合
     * @return 结果字符串
     */
    public static String addCodePrefixIfNot(List<String> phones) {
        return CollUtil.join(phones, StrUtil.COMMA, SmsUtils::addCodePrefixIfNot);
    }

    /**
     * 将手机号码 添加+86电话区号前缀
     *
     * @param phone 手机号码
     * @return 结果字符串
     */
    public static String addCodePrefixIfNot(String phone) {
        return StrUtil.addPrefixIfNot(phone, "+86");
    }

    /**
     * List +86后转 数组
     *
     * @param list 集合
     * @return 结果字符串
     */
    public static String[] addCodePrefixIfNotToArray(List<String> list) {
        List<String> toStr = new ArrayList<>();
        for (String s : list) {
            toStr.add(addCodePrefixIfNot(s));
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

    /**
     * 将map的value转成数组
     * @param map Map
     * @return 数组
     */
    public static String[] toArray(Map<String, String> map){
        if (isEmpty(map)) {
            return new String[0];
        }
        return toArray(map.values(), SmsUtils::isNotEmpty, s -> s, new String[map.size()]);
    }

    /**
     * 将所有提交的参数升序排列，并排除部分key字段后，将key与value用"="连接起来 组成"key=value" + "&"（连接符）+ "key=value" 的方式
     * @param params 参数Map
     * @param excludes 排除的key
     * @return String
     */
    public static String sortedParamsAsc(Map<String, Object> params, String... excludes) {
        if (MapUtil.isEmpty(params)){
            return StrUtil.EMPTY;
        }
        List<String> keys = new ArrayList<>(params.keySet());
        if (CollUtil.isEmpty(keys)){
            return StrUtil.EMPTY;
        }
        if (ArrayUtil.isNotEmpty(excludes)){
            ArrayList<String> excludeKeys = CollUtil.toList(excludes);
            keys.removeIf(key -> excludeKeys.stream().anyMatch(exclude -> exclude.equals(key)));
            if (CollUtil.isEmpty(keys)){
                return StrUtil.EMPTY;
            }
        }
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append("=").append(Convert.toStr(params.get(key))).append("&");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1); // Remove the last '&'
        }
        return sb.toString();
    }
}