package org.dromara.sms4j.comm.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * <p>类名: SmsDateUtils
 * <p>说明： 时间日期工具类
 *
 * @author :bleachtred
 * 2024/6/21  23:59
 **/
public class SmsDateUtils extends DateUtil {

    private SmsDateUtils() {
    }

    /**
     * 格林威治标准时间（GMT）或世界协调时间（UTC）
     */
    private static final String GMT = "GMT";

    /**
     * 东八区
     */
    private static final String GMT_8 = SmsDateUtils.GMT + "+8:00";

    /**
     * 天翼云、七牛云时间格式
     */
    private static final String PURE_DATE_UTC_PATTERN = "yyyyMMdd'T'HHmmss'Z'";

    /**
     * 获取格林威治标准时间（GMT）或世界协调时间（UTC）
     * @return TimeZone
     */
    public static TimeZone gmt(){
        return getTimeZone(GMT);
    }

    /**
     * 获取东八区时区
     * @return TimeZone
     */
    public static TimeZone gmt8(){
        return getTimeZone(GMT_8);
    }

    /**
     * 获取时区
     * @param zoneId zoneId
     * @return TimeZone
     */
    public static TimeZone getTimeZone(String zoneId){
        return TimeZone.getTimeZone(zoneId);
    }

    /**
     * 获取SimpleDateFormat
     * @param pattern 时间格式
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat sdfGmt(String pattern){
        return sdf(pattern, gmt());
    }

    /**
     * 获取SimpleDateFormat
     * @param pattern 时间格式
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat sdfGmt8(String pattern){
        return sdf(pattern, gmt8());
    }

    /**
     * 获取SimpleDateFormat
     * @param pattern 时间格式
     * @param timeZone 时区
     * @return 获取SimpleDateFormat
     */
    public static SimpleDateFormat sdf(String pattern, TimeZone timeZone){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(timeZone);
        return sdf;
    }

    /**
     * 格式化时间
     * @param date 时间
     * @param pattern 时间格式
     * @return String
     */
    public static String formatGmtDateToStr(Date date, String pattern){
        SimpleDateFormat sdf = sdfGmt(pattern);
        return sdf.format(date);
    }

    /**
     * 格式化时间
     * @param date 时间
     * @param pattern 时间格式
     * @return String
     */
    public static String formatGmt8DateToStr(Date date, String pattern){
        SimpleDateFormat sdf = sdfGmt8(pattern);
        return sdf.format(date);
    }

    /**
     * 格式化时间
     * @param date 时间
     * @param pattern 时间格式
     * @param timeZone 时区
     * @return String
     */
    public static String formatDateToStr(Date date, String pattern, TimeZone timeZone){
        SimpleDateFormat sdf = sdf(pattern, timeZone);
        return sdf.format(date);
    }

    /**
     * 日期格式：yyyy-MM-dd'T'HH:mm:ss'Z'
     * @param date 时间
     * @return 时间字符串
     */
    public static String utcGmt(Date date){
        return formatGmtDateToStr(date, DatePattern.UTC_PATTERN);
    }

    /**
     * 日期格式：yyyy-MM-dd'T'HH:mm:ss'Z'
     * @param date 时间
     * @return 时间字符串
     */
    public static String utcGmt8(Date date){
        return formatGmt8DateToStr(date, DatePattern.UTC_PATTERN);
    }

    /**
     * 日期格式：yyyyMMdd
     * @param date 时间
     * @return 时间字符串
     */
    public static String pureDateGmt(Date date){
        return formatGmtDateToStr(date, DatePattern.PURE_DATE_PATTERN);
    }

    /**
     * 日期格式：yyyyMMdd
     * @param date 时间
     * @return 时间字符串
     */
    public static String pureDateGmt8(Date date){
        return formatGmt8DateToStr(date, DatePattern.PURE_DATE_PATTERN);
    }

    /**
     * 天翼云、七牛云时间格式：yyyyMMdd'T'HHmmss'Z'
     * @param date 时间
     * @return 时间字符串
     */
    public static String pureDateUtcGmt(Date date){
        return formatGmtDateToStr(date, PURE_DATE_UTC_PATTERN);
    }

    /**
     * 天翼云、七牛云时间格式：yyyyMMdd'T'HHmmss'Z'
     * @param date 时间
     * @return 时间字符串
     */
    public static String pureDateUtcGmt8(Date date){
        return formatGmt8DateToStr(date, PURE_DATE_UTC_PATTERN);
    }

    /**
     * 日期格式：yyyy-MM-dd
     * @param date 时间
     * @return 时间字符串
     */
    public static String normDateGmt(Date date){
        return formatGmtDateToStr(date, DatePattern.NORM_DATE_PATTERN);
    }

    /**
     * 日期格式：yyyy-MM-dd
     * @param date 时间
     * @return 时间字符串
     */
    public static String normDateGmt8(Date date){
        return formatGmt8DateToStr(date, DatePattern.NORM_DATE_PATTERN);
    }

    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     * @param date 时间
     * @return 时间字符串
     */
    public static String normDatetimeGmt(Date date){
        return formatGmtDateToStr(date, DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     * @param date 时间
     * @return 时间字符串
     */
    public static String normDatetimeGmt8(Date date){
        return formatGmt8DateToStr(date, DatePattern.NORM_DATETIME_PATTERN);
    }
}
