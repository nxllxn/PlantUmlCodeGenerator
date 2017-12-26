package com.nxllxn.plantuml.utils;

import java.util.regex.Pattern;

/**
 * 正则表达式工具
 */
public class RegExpUtil {
    /**
     * 用于匹配字符串末尾空格的表达式
     */
    public static final Pattern PATTERN_FOR_LAST_SPACE = Pattern.compile("\\s$");

    /**
     * 用于匹配一个代码块开始的模式
     */
    public static final Pattern PATTERN_FOR_CODE_BLOCK_OPEN_IDENTIFIER = Pattern.compile("\\{(\\s+)$");
}
