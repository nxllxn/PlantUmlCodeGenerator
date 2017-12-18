package com.icekredit.utils;

/**
 * 用于获取当前项目或者jar包执行的路径的工具类
 *
 * @author wenchao
 * @version 1.0, 16/11/01
 */
public class PathUtil {
    /**
     * 获取当前项目或者jar包执行的路径
     *
     * @return 当前项目或者jar包执行的路径
     */
    public static String getCurrentRunningPath() {
        return System.getProperty("user.dir");
    }
}
