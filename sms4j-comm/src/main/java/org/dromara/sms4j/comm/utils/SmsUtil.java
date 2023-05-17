package org.dromara.sms4j.comm.utils;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.dromara.sms4j.comm.exception.SmsSqlException;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

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
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        try {
            Random random = SecureRandom.getInstanceStrong();
            for (int i = 0; i < len; i++) {
                int number = random.nextInt(62);
                sb.append(str.charAt(number));
            }
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
        return sb.toString();
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
        String str = "0123456789";
        StringBuilder sb = new StringBuilder();
        try {
        Random random = SecureRandom.getInstanceStrong();
        for (int i = 0; i < len; i++) {
            int number = random.nextInt(10);
            sb.append(str.charAt(number));
        }
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    /**
     * 指定元素是否为null或者空字符串
     * @param str 指定元素
     * @return 是否为null或者空字符串
     * @author :Wind
     */
    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
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
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                str.append(list.get(i));
            } else {
                str.append(list.get(i));
                str.append(",");
            }
        }
        return str.toString();
    }

    /**
     *  jsonForObject
     * <p>将json字符串转化为指定的对象
     * @author :Wind
    */
    public static <T> T jsonForObject(String json, Class<T> t) {
        try {
            return json == null||"".equals(json)?null: JSONObject.toJavaObject(JSONObject.parseObject(json), t);
        } catch (JSONException e) {
            throw new SmsSqlException("json sequence exception" + e.getMessage());
        }
    }

    /**
     *  copyBean
     * <p>拷贝bean，只有源对象不为null才会拷贝
     * @param t 源对象
     * @param m 目标对象
     * @author :Wind
    */
    public static <T,M>void copyBean(T t,M m){
        if (t != null){
            BeanUtil.copyProperties(t, m);
        }
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
