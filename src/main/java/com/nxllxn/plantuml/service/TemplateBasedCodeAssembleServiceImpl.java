package com.nxllxn.plantuml.service;

import com.nxllxn.plantuml.java.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

import static com.nxllxn.plantuml.utils.AssembleUtil.*;

/**
 * 基于模板的代码组装服务实现类
 *
 * @author wenchao
 */
public class TemplateBasedCodeAssembleServiceImpl implements CodeAssembleService {
    private static final String PLACE_HOLDER_PATTERN = "\\{}";

    /**
     * Java Doc第一行模板
     */
    private static final String JAVA_DOC_START_LINE_TEMPLATE = "/**";

    /**
     * Java Doc中间行模板
     */
    private static final String JAVA_DOC_MAIN_LINE_TEMPLATE = " * {}";

    /**
     * Java Doc末行模板
     */
    private static final String JAVA_DOC_END_LINE_TEMPLATE = " */";

    /**
     * 注解模板
     */
    private static final String ANNOTATION_TEMPLATE = "{}";

    /**
     * 额外的空格，用于控制代码格式以及美观性
     */
    public static final String EXTRA_SPACE = " ";

    /**
     * 如果不存在某一段代码，那么填充的字符串
     */
    private static final String EMPTY_STRING_FOR_NO_CODE = "";

    /**
     * 多接口实现时使用的分隔符
     */
    private static final String INTERFACE_IMPLEMENT_SEPARATOR = ",";

    private static class Holder {
        private static TemplateBasedCodeAssembleServiceImpl codeAssembleService = new TemplateBasedCodeAssembleServiceImpl();
    }

    /**
     * 单例
     *
     * @return TemplateBasedCodeAssembleServiceImpl对象
     */
    public static TemplateBasedCodeAssembleServiceImpl getSingleInstance() {
        return Holder.codeAssembleService;
    }

    private TemplateBasedCodeAssembleServiceImpl() {
    }

    @Override
    public String assembleJavaDoc(List<String> javaDocs, int indentLevel) {
        if (javaDocs == null || javaDocs.isEmpty()) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        StringBuilder stringBuilder = new StringBuilder();

        indentLevel = normalizeIndentLevel(indentLevel);

        indentWith(stringBuilder, indentLevel);
        stringBuilder.append(JAVA_DOC_START_LINE_TEMPLATE);
        startNewLine(stringBuilder);

        for (String javaDoc : javaDocs) {
            indentWith(stringBuilder, indentLevel);
            stringBuilder.append(JAVA_DOC_MAIN_LINE_TEMPLATE.replaceAll(PLACE_HOLDER_PATTERN, javaDoc));
            startNewLine(stringBuilder);
        }

        indentWith(stringBuilder, indentLevel);
        stringBuilder.append(JAVA_DOC_END_LINE_TEMPLATE);
        startNewLine(stringBuilder);

        return stringBuilder.toString();
    }

    @Override
    public String assembleClassAnnotation(Set<String> annotations, int indentLevel) {
        return assembleAnnotationWithMultipleLine(annotations, indentLevel);
    }

    @Override
    public String assembleMethodAnnotation(Set<String> annotations, int indentLevel) {
        return assembleAnnotationWithMultipleLine(annotations, indentLevel);
    }

    @Override
    public String assembleFieldAnnotation(Set<String> annotations, int indentLevel) {
        return assembleAnnotationWithMultipleLine(annotations, indentLevel);
    }

    /**
     * 按照多行的方式组织注解
     *
     * @param annotations 待组装注解集合
     * @param indentLevel 缩进等级
     * @return 多行注解
     */
    private String assembleAnnotationWithMultipleLine(Set<String> annotations, int indentLevel) {
        if (annotations == null || annotations.isEmpty()) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (String annotation : annotations) {
            indentWith(stringBuilder, indentLevel);
            stringBuilder.append(ANNOTATION_TEMPLATE.replaceAll(PLACE_HOLDER_PATTERN, annotation));
            startNewLine(stringBuilder);
        }

        return stringBuilder.toString();
    }

    @Override
    public String assembleParameterAnnotation(Set<String> annotations, int indentLevel) {
        return assembleAnnotationWithSingleLine(annotations, indentLevel);
    }

    /**
     * 按照单行的方式组织注解
     *
     * @param annotations 待组装注解集合
     * @param indentLevel 缩进等级
     * @return 单行注解
     */
    private String assembleAnnotationWithSingleLine(Set<String> annotations, int indentLevel) {
        if (annotations == null || annotations.isEmpty()) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        StringBuilder stringBuilder = new StringBuilder();

        indentLevel = normalizeIndentLevel(indentLevel);
        for (String annotation : annotations) {
            indentWith(stringBuilder, indentLevel);
            stringBuilder.append(ANNOTATION_TEMPLATE.replaceAll(PLACE_HOLDER_PATTERN, annotation));
            stringBuilder.append(EXTRA_SPACE);
        }

        return stringBuilder.toString();
    }

    /**
     * 规范化缩进等级，如果缩进等级小于0,那么设置为0
     *
     * @param indentLevel 缩进等级
     * @return 规范后的缩进等级
     */
    private int normalizeIndentLevel(int indentLevel) {
        return indentLevel < 0 ? DEFAULT_INDENT_LEVEL : indentLevel;
    }

    @Override
    public String assembleVisibility(Visibility visibility) {
        if (visibility == null) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        String visibilityStr = visibility.toString();

        if (StringUtils.isBlank(visibilityStr)) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        return visibilityStr + EXTRA_SPACE;
    }

    @Override
    public String assembleAbstractModifier(boolean isAbstract) {
        return assembleAsModifier(isAbstract, "abstract");
    }

    @Override
    public String assembleStaticModifier(boolean isStatic) {
        return assembleAsModifier(isStatic, "static");
    }

    @Override
    public String assembleFinalModifier(boolean isFinal) {
        return assembleAsModifier(isFinal, "final");
    }

    @Override
    public String assembleSynchronizeModifier(boolean isSynchronized) {
        return assembleAsModifier(isSynchronized, "synchronized");
    }

    @Override
    public String assembleTransientModifier(boolean isTransient) {
        return assembleAsModifier(isTransient, "transient");
    }

    @Override
    public String assembleVolatileModifier(boolean isVolatile) {
        return assembleAsModifier(isVolatile, "volatile");
    }

    @Override
    public String assembleDefaultModifier(boolean isDefault) {
        return assembleAsModifier(isDefault, "default");
    }

    @Override
    public String assembleNativeModifier(boolean isNative) {
        return assembleAsModifier(isNative, "native");
    }

    /**
     * 按照修饰符的方式组织代码
     *
     * @param hasModifier 是否由当前修饰符修饰
     * @param modifierStr 修饰符字符串
     * @return 组装后的代码
     */
    private String assembleAsModifier(boolean hasModifier, String modifierStr) {
        return hasModifier ? ("{" + modifierStr + "}" + EXTRA_SPACE) : EMPTY_STRING_FOR_NO_CODE;
    }

    @Override
    public String assembleTypeDefinition(FullyQualifiedJavaType fullyQualifiedJavaType) {
        if (fullyQualifiedJavaType == null) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        String simpleName = fullyQualifiedJavaType.getShortName();
        if (StringUtils.isBlank(simpleName)) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        return simpleName + EXTRA_SPACE;
    }

    @Override
    public String assembleClassExtension(FullyQualifiedJavaType superClass) {
        if (superClass == null) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        return "extends " + superClass.getShortName() + EXTRA_SPACE;
    }

    @Override
    public String assembleInterfaceImplements(Set<FullyQualifiedJavaType> superInterfaces) {
        if (superInterfaces == null || superInterfaces.isEmpty()) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        String[] superInterfaceTypes = new String[superInterfaces.size()];

        int index = 0;
        for (FullyQualifiedJavaType superInterface : superInterfaces) {
            superInterfaceTypes[index++] = superInterface.getShortName();
        }

        return "implements " + StringUtils.join(superInterfaceTypes, INTERFACE_IMPLEMENT_SEPARATOR) + EXTRA_SPACE;
    }

    @Override
    public String assembleElementTypeClass() {
        return "class ";
    }

    @Override
    public String assembleElementTypeInterface() {
        return "interface ";
    }

    @Override
    public String assembleElementTypeEnum() {
        return "enum ";
    }

    @Override
    public String assembleNonStaticImport(String fullyQualifiedNameWithoutTypeParameters) {
        if (StringUtils.isBlank(fullyQualifiedNameWithoutTypeParameters)) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        return "import " + fullyQualifiedNameWithoutTypeParameters + ";" + LINE_SEPARATOR;
    }

    @Override
    public String assembleStaticImport(String staticImport) {
        if (StringUtils.isBlank(staticImport)) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        return "import static " + staticImport + ";" + LINE_SEPARATOR;
    }

    @Override
    public String assemblePackage(String packageName) {
        if (StringUtils.isBlank(packageName)) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        return "package " + packageName + ";";
    }

    @Override
    public String assembleReturnType(FullyQualifiedJavaType returnType) {
        if (returnType != null) {
            return returnType.getShortName() + EXTRA_SPACE;
        }

        return "void" + EXTRA_SPACE;
    }

    @Override
    public String assembleFunctionName(String name) {
        return assembleFunctionName(name, false);
    }

    @Override
    public String assembleFunctionName(String name, boolean isConstructor) {
        if (StringUtils.isBlank(name)) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        return isConstructor ? upperFirstCharacter(name) : name;
    }

    /**
     * 将指定字符串首字母大写后返回
     *
     * @param str 指定字符串
     * @return 首字母大写的字符串
     */
    private String upperFirstCharacter(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return String.valueOf(str.charAt(0)).toUpperCase() + str.substring(1);
    }

    @Override
    public String assembleTypeParameters(List<TypeParameter> typeParameters) {
        if (typeParameters == null || typeParameters.isEmpty()) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        String[] codeParts = new String[typeParameters.size()];
        int index = 0;
        for (TypeParameter typeParameter : typeParameters) {
            codeParts[index++] = typeParameter.getFormattedContent();
        }

        return "<" + StringUtils.join(codeParts, ",") + ">" + EXTRA_SPACE;
    }

    @Override
    public String assembleParameters(List<Parameter> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return "()" + EXTRA_SPACE;
        }

        String[] codeParts = new String[parameters.size()];

        int index = 0;
        for (Parameter parameter : parameters) {
            codeParts[index++] = parameter.getFormattedContent();
        }

        return "(" + StringUtils.join(codeParts, ", ") + ")" + EXTRA_SPACE;
    }

    @Override
    public String assembleThrowsExceptions(List<FullyQualifiedJavaType> throwsExceptions) {
        if (throwsExceptions == null || throwsExceptions.isEmpty()) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        String[] codeParts = new String[throwsExceptions.size()];

        int index = 0;
        for (FullyQualifiedJavaType throwsException : throwsExceptions) {
            codeParts[index++] = throwsException.getShortName();
        }

        return "throws" + EXTRA_SPACE + StringUtils.join(codeParts, ",") + EXTRA_SPACE;
    }

    @Override
    public String assembleCodeBlockBody(List<String> bodyLines, int indentLevel) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String bodyLine : bodyLines) {
            indentWith(stringBuilder, indentLevel);
            stringBuilder.append(bodyLine);

            //此处直接在代码行后面添加分号有点想当然了
            if (!bodyLine.endsWith(";")) {
                //stringBuilder.append(";");
            }

            startNewLine(stringBuilder);
        }

        return stringBuilder.toString();
    }

    @Override
    public String assembleVariableArgs(boolean isVarArgs) {
        if (!isVarArgs) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        return "... ";
    }

    @Override
    public String assembleParameterName(String name) {
        if (StringUtils.isBlank(name)) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        return name;
    }

    @Override
    public String assembleFieldName(String name) {
        if (StringUtils.isBlank(name)) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        return name;
    }

    @Override
    public String assembleEnumConstant(
            String enumConstantName, List<String> fieldStringValues,
            List<Field> fields, int indentLevel) {
        if (StringUtils.isBlank(enumConstantName)) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        StringBuilder assembledArgumentsStringBuilder = new StringBuilder();

        indentWith(assembledArgumentsStringBuilder, indentLevel);

        assembledArgumentsStringBuilder.append(enumConstantName.toUpperCase());

        assembledArgumentsStringBuilder.append(assembleArgumentsList(fieldStringValues, fields));

        return assembledArgumentsStringBuilder.toString();
    }

    /**
     * 组装枚举常量列表 TODO need a better solution
     *
     * @param fieldStringValues 字符串字面值列表
     * @param fields            字段类型列表
     * @return 参数类表
     */
    private String assembleArgumentsList(List<String> fieldStringValues, List<Field> fields) {
        if (fieldStringValues == null || fields.isEmpty()) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        return "(" + StringUtils.join(fieldStringValues.toArray(), ",") + ")";
    }

    @Override
    public String assembleTextNode(String text) {
        return text;
    }

    @Override
    public String assembleXmlAttributeNode(String name, String value) {
        if (StringUtils.isBlank(name)
                || StringUtils.isBlank(value)) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        return name + "=\"" + value + "\"";
    }

    @Override
    public String assemblePublicIdAndSystemId(String rootNodeName, String publicId, String systemId) {
        if (StringUtils.isBlank(rootNodeName)) {
            return EMPTY_STRING_FOR_NO_CODE;
        }

        String template = "<!DOCTYPE %s PUBLIC \"%s\" \"%s\">";

        return String.format(template, rootNodeName, publicId == null ? "" : publicId, systemId == null ? "" : systemId);
    }

    @Override
    public String assembleXmlHeader() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    }

    @Override
    public String assembleElementTypeAnnotation() {
        return "@interface" + EXTRA_SPACE;
    }

    @Override
    public String assembleTypeAbstractModifier(boolean isAbstract) {
        return isAbstract ? ("abstract" + EXTRA_SPACE) : EMPTY_STRING_FOR_NO_CODE;
    }
}
