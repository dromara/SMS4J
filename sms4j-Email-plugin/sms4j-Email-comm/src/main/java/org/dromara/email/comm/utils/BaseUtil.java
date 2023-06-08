package org.dromara.email.comm.utils;

import java.io.File;

public class BaseUtil {

public static String getPathName(String path) {
    String[] split = path.split(File.separator);
    return split[split.length-1];
}
}
