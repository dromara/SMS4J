package org.dromara.email.jakarta.comm.utils;

public class BaseUtil {

    /**
     * getPathName
     * <p>分隔文件路径，并获取文件名
     * @param path 文件路径
     * @author :Wind
     */
    public static String getPathName(String path) {
        String[] split = path.split("/");
        return split[split.length - 1];
    }
}
