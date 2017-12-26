package com.nxllxn.plantuml.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.nxllxn.plantuml.utils.RegExpUtil.PATTERN_FOR_LAST_SPACE;

/**
 * 代码组装工具类，包含代码组装相关工具函数
 *
 * @author wenchao
 */
public class AssembleUtil {
    /**
     * 默认的缩进等级为0
     */
    public static final int DEFAULT_INDENT_LEVEL = 0;

    /**
     * 每次增加的缩进等级为1
     */
    public static final int INDENT_LEVEL_INCREASED = 1;

    /**
     * 系统相关的行分隔符， 例如 Windows 平台为 ‘\r\n’ linux 平台为‘\n’
     */
    public static final String LINE_SEPARATOR;

    /**
     * Java 自定义的行分隔符属性名称
     */
    private static final String LINE_SEPARATOR_PROPERTY_NAME = "line.separator";

    /**
     * 默认的缩进字符串是四个空格
     */
    public static final String DEFAULT_INDEX_STR = "    ";

    /**
     * 代码块开始标识符，注意后面的空格
     */
    public static final String BLOCK_OPEN_IDENTIFIER = "{ ";

    /**
     * 代码块结束标识符，注意后面的空格
     */
    public static final String BLOCK_CLOSE_IDENTIFIER = "} ";

    /**
     * 包分隔符
     */
    public static final String PACKAGE_SEPERATOR = ".";

    /**
     * 末尾空行匹配模式
     */
    private static final Pattern PATTERN_FOR_EXTRA_EMPTY_LINE = Pattern.compile("\\n(\\s*\\n$)");

    static {
        String systemBasedLineSeparator = System.getProperty(LINE_SEPARATOR_PROPERTY_NAME);

        if (StringUtils.isBlank(systemBasedLineSeparator)) {
            systemBasedLineSeparator = "\n";
        }

        LINE_SEPARATOR = systemBasedLineSeparator;
    }

    /**
     * 开始一个新的行
     *
     * @param stringBuilder 待调整的StringBuilder对象
     */
    public static void startNewLine(StringBuilder stringBuilder) {
        if (stringBuilder == null) {
            return;
        }

        stringBuilder.append(LINE_SEPARATOR);
    }

    /**
     * 向StringBuilder中添加一个额外的空行，控制代码美观性
     * @param stringBuilder 待调整StringBuilder对象
     */
    public static void extraEmptyLine(StringBuilder stringBuilder){
        if (stringBuilder == null){
            return;
        }

        //如果已经存在一个额外的空行，那么放弃继续添加额外的空行，主要是为了避免如果中间有一些代码块没有产生实质的输出，即返回代码片段为空是，产生多余的空行
        if (stringBuilder.toString().endsWith(LINE_SEPARATOR + LINE_SEPARATOR)){
            return;
        }

        //如果之前已经存在一个换行,那么只需要再添加一个换行
        if (stringBuilder.toString().endsWith(LINE_SEPARATOR)){
            stringBuilder.append(LINE_SEPARATOR);

            return;
        }

        stringBuilder.append(LINE_SEPARATOR);

        //额外的空行
        stringBuilder.append(LINE_SEPARATOR);
    }

    /**
     * 为指定StringBuilder添加指定等级的缩进
     * @param stringBuilder 待调整StringBuilder
     * @param indentLevel 指定缩进等级
     */
    public static void indentWith(StringBuilder stringBuilder, int indentLevel) {
        if (stringBuilder == null || indentLevel < 0) {
            return;
        }

        for (int index = 0; index < indentLevel; index++) {
            stringBuilder.append(DEFAULT_INDEX_STR);
        }
    }

    /**
     * 移除代码块最後面的空行
     * @param stringBuilder 代码块代码
     */
    public static void removeExtraEmptyLineInCodeBlockEnd(StringBuilder stringBuilder){
        if (stringBuilder == null){
            return;
        }

        Matcher matcher = PATTERN_FOR_EXTRA_EMPTY_LINE.matcher(stringBuilder.toString());

        if (matcher.find()){
            int length = matcher.group(1).length();

            stringBuilder.setLength(stringBuilder.length() - length);
        }
    }

    /**
     * 回退当前代码builder末尾的空格
     * @param stringBuilder 当前代码builder
     */
    public static void rollbackExtraSpaceInTheEnd(StringBuilder stringBuilder){
        while (PATTERN_FOR_LAST_SPACE.matcher(stringBuilder.toString()).find()){
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
    }
}
