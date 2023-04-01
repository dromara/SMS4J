package kim.wind.sms.comm.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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

}
