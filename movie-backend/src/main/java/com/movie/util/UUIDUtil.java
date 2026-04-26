package com.movie.util;

import java.util.UUID;

/**
 * UUID工具类
 * 用于生成唯一标识符
 */
public class UUIDUtil {

    /**
     * 生成不带分隔符的UUID
     * 
     * @return 32位UUID字符串
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成带分隔符的UUID
     * 
     * @return 36位UUID字符串
     */
    public static String getUUIDWithSeparator() {
        return UUID.randomUUID().toString();
    }
}
