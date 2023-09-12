package org.dromara.sms4j.comm.utils;

import cn.hutool.core.io.file.FileReader;

import java.util.Objects;

/**
 * SettingUtil
 * <p> 用于读取json配置文件
 * @author :Wind
 * 2023/4/8  14:29
 **/
public class SettingUtils {

    /** 读取配置文件*/
    public static String getSetting(String path){
       return new FileReader(path).readString();
    }

    public static String getSetting(){
       return getSetting(Objects.requireNonNull(SettingUtils.class.getResource("/smsConfig.json")).getPath());
    }
}
