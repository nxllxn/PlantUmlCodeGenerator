package com.nxllxn.plantuml.java;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 用于描述一个全限定类型的实体类
 *
 * 限定通配符总是包括自己
 * 上界类型通配符：add方法受限
 * 下界类型通配符：get方法受限
 * 如果你想从一个数据类型里获取数据，使用 ? extends 通配符
 * 如果你想把对象写入一个数据结构里，使用 ? super 通配符
 * 如果你既想存，又想取，那就别用通配符
 * 不能同时声明泛型通配符上界和下界
 * < T > 等同于 < T extends Object>
 * < ? > 等同于 < ? extends Object>
 *
 * @author unknown
 */
public class FullyQualifiedJavaType implements Comparable<FullyQualifiedJavaType> {
    /**
     * 不需要显式导入的包--java.lang包
     */
    private static final String JAVA_LANG = "java.lang";

    /**
     * 整型类型的全限定类型描述
     */
    private static FullyQualifiedJavaType intInstance = null;

    /**
     * 字符串类型的全限定类型描述
     */
    private static FullyQualifiedJavaType stringInstance = null;

    /**
     * 布尔类型的全限定类型描述
     */
    private static FullyQualifiedJavaType booleanPrimitiveInstance = null;

    /**
     * Java Object类型的全限定类型描述
     */
    private static FullyQualifiedJavaType objectInstance = null;

    /**
     * java.util.Date类型的全限定类型描述
     */
    private static FullyQualifiedJavaType dateInstance = null;

    private static FullyQualifiedJavaType genericTypeInstance = null;

    /**
     * 不包含泛型参数的simpleName class.getSimpleName()
     */
    private String baseShortName;

    /**
     * 不包含泛型参数的全限定类名 class.getName()
     */
    private String baseQualifiedName;

    /**
     * 是否需要显式导入
     */
    private boolean explicitlyImported;

    /**
     * 当前类型对应的包名称
     */
    private String packageName;

    /**
     * 当前类型是否为基本类型 scala type
     */
    private boolean primitive;

    /**
     * 当前类型是否是数组类型
     */
    private boolean isArray;

    /**
     * 基本类型封装器
     */
    private PrimitiveTypeWrapper primitiveTypeWrapper;

    /**
     * 当前类型包含的TypeArgument 比如&lt;TypeArgumentOne,TypeArgumentTwo,...&gt;
     */
    private List<FullyQualifiedJavaType> typeArguments;

    /**
     * 是否是通配符类型
     */
    private boolean isWildcardType;

    /**
     * 是否为上下边界通配符类型，上界是指 ? extends ABC,下界是指? super ABC
     */
    private boolean isBoundedWildcard;

    /**
     * 是否为下边界通配符
     */
    private boolean isExtendsBoundedWildcard;

    /**
     * 当前系统已知的全限定类名到全限定类型之间的映射Map
     */
    public static final Map<String, FullyQualifiedJavaType> KNOWN_NAME_TO_TYPE_MAP = new HashMap<>();

    /**
     * 基本类型的simpleName
     */
    private static final String SIMPLE_NAME_FOR_BYTE = "byte";
    private static final String SIMPLE_NAME_FOR_SHORT = "short";
    private static final String SIMPLE_NAME_FOR_INT = "int";
    private static final String SIMPLE_ALIAS_FOR_INT = "integer";
    private static final String SIMPLE_NAME_FOR_LONG = "long";
    private static final String SIMPLE_NAME_FOR_CHAR = "char";
    private static final String SIMPLE_NAME_FOR_FLOAT = "float";
    private static final String SIMPLE_NAME_FOR_DOUBLE = "double";
    private static final String SIMPLE_NAME_FOR_BOOLEAN = "boolean";
    private static final String SIMPLE_NAME_FOR_STRING = "string";


    /**
     * 包名称如分隔符
     */
    private static final String PACKAGE_SEPARATOR = ".";

    /**
     * 通配符标识符
     */
    private static final String WILD_CARD_IDENTIFIER = "?";

    /**
     * 构造函数，传入一个类型的字符串表示，内部进行相应的转换
     *
     * @param fullTypeSpecification 一个类型的字符串表示
     */
    public FullyQualifiedJavaType(String fullTypeSpecification) {
        typeArguments = new ArrayList<>();

        parse(fullTypeSpecification);

        //保存当前类型的全限定类名到当前类型的映射，做一个缓存
        KNOWN_NAME_TO_TYPE_MAP.put(this.getFullyQualifiedName(),this);
    }

    /**
     * 当前类型是否需要显式导入
     * @return 如果是非java.lang开头的包，需要显式导入，返回true，否则返回false
     */
    public boolean isExplicitlyImported() {
        return explicitlyImported;
    }

    /**
     * 获取当前类型的全限定类名称
     *
     * @return 当前类型的全限定类名称字符串
     */
    public String getFullyQualifiedName() {
        StringBuilder sb = new StringBuilder();

        sb.append(assembleWildCardType(baseQualifiedName));

        if (typeArguments.size() > 0) {
            boolean first = true;
            sb.append('<');
            for (FullyQualifiedJavaType fullyQualifiedJavaType : typeArguments) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append(fullyQualifiedJavaType.getFullyQualifiedName());

            }
            sb.append('>');
        }

        return sb.toString();
    }

    /**
     * 计算当前类型全限定类名，不需要泛型参数
     * @return 当前类型的全限定类名
     */
    public String getFullyQualifiedNameWithoutTypeParameters() {
        return baseQualifiedName;
    }

    /**
     * 递归计算当前类型中使用到的所有需要导入的类型，需要注意TypeArgument中的导入内容
     * @return 需要导入的类型集合
     */
    public List<String> getImportList() {
        List<String> answer = new ArrayList<>();

        if (isExplicitlyImported()) {
            int index = baseShortName.indexOf('.');

            if (index == -1) {
                answer.add(calculateActualImport(baseQualifiedName));
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(packageName);
                sb.append('.');
                sb.append(calculateActualImport(baseShortName.substring(0, index)));
                answer.add(sb.toString());
            }
        }

        //递归计算
        for (FullyQualifiedJavaType fullyQualifiedJavaType : typeArguments) {
            answer.addAll(fullyQualifiedJavaType.getImportList());
        }

        return answer;
    }

    /**
     * 计算实际需要导入的类型，主要是兼容数组类型，如果是数组类型那么去掉后面的中括号
     * @param name 待导入的类型
     * @return 实际需要导入的类型
     */
    private String calculateActualImport(String name) {
        String answer = name;

        if (this.isArray()) {
            int index = name.indexOf("[");
            if (index != -1) {
                answer = name.substring(0, index);
            }
        }

        return answer;
    }

    /**
     * 获取当前类型所在包名
     * @return 当前类型包名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * 计算当前类型的simpleName，包含全部的泛型参数
     * @return 包含泛型参数的SimpleName
     */
    public String getShortName() {
        StringBuilder sb = new StringBuilder();

        sb.append(assembleWildCardType(baseShortName));

        if (typeArguments.size() > 0) {
            boolean first = true;
            sb.append('<');
            for (FullyQualifiedJavaType fullyQualifiedJavaType : typeArguments) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append(fullyQualifiedJavaType.getShortName());

            }
            sb.append('>');
        }

        return sb.toString();
    }

    /**
     * 按照通配符类型的代码组织形式组装通配符类型
     * @param baseQualifiedName 当前类的全限定类名
     */
    private String assembleWildCardType(String baseQualifiedName) {
        StringBuilder sb = new StringBuilder();

        if (isWildcardType) {
            sb.append('?');
            if (isBoundedWildcard) {
                if (isExtendsBoundedWildcard) {
                    sb.append(" extends ");
                } else {
                    sb.append(" super ");
                }

                sb.append(baseQualifiedName);
            }
        } else {
            sb.append(baseQualifiedName);
        }

        return sb.toString();
    }

    /**
     * 计算当前类型的simpleName，不包含泛型参数
     * @return 当前类型的simpleName
     */
    public String getShortNameWithoutTypeArguments() {
        return baseShortName;
    }

    public boolean isPrimitive() {
        return primitive;
    }

    public PrimitiveTypeWrapper getPrimitiveTypeWrapper() {
        return primitiveTypeWrapper;
    }

    /**
     * 当前类型是否是一个数组类型
     * @return 如果当前类型以 [\\s*] pattern结尾，那么认为这是一个数组类型
     */
    public boolean isArray() {
        return isArray;
    }

    /**
     * 为当前类型添加一个泛型参数
     * @param type 泛型类型
     */
    public void addTypeArgument(FullyQualifiedJavaType type) {
        typeArguments.add(type);
    }

    /**
     * 解析指定全限定类名或者类型SimpleName以及基本类型名称如int为全限定类型
     * @param fullTypeSpecification 全限定类名或者类型SimpleName以及基本类型名称如int
     */
    public void parse(String fullTypeSpecification) {
        String spec = fullTypeSpecification.trim();

        if (spec.startsWith(WILD_CARD_IDENTIFIER)) {
            isWildcardType = true;
            spec = spec.substring(1).trim();
            if (spec.startsWith("extends ")) {
                isBoundedWildcard = true;
                isExtendsBoundedWildcard = true;
                spec = spec.substring(8);  // "extends ".length()
            } else if (spec.startsWith("super ")) {
                isBoundedWildcard = true;
                isExtendsBoundedWildcard = false;
                spec = spec.substring(6);  // "super ".length()
            } else {
                isBoundedWildcard = false;
            }
            parse(spec);
        } else {
            int index = fullTypeSpecification.indexOf('<');
            if (index == -1) {
                simpleParse(fullTypeSpecification);
            } else {
                simpleParse(fullTypeSpecification.substring(0, index));
                int endIndex = fullTypeSpecification.lastIndexOf('>');
                if (endIndex == -1) {
                    throw new RuntimeException("Unknown type argument!");
                }
                genericParse(fullTypeSpecification.substring(index, endIndex + 1));
            }

            // this is far from a perfect test for detecting arrays, but is close
            // enough for most cases.  It will not detect an improperly specified
            // array type like byte], but it will detect byte[] and byte[   ]
            // which are both valid
            isArray = fullTypeSpecification.endsWith("]");
        }
    }

    /**
     * 不带泛型的类型解析
     * @param typeSpecification 限定类名或者类型SimpleName以及基本类型名称如int
     */
    private void simpleParse(String typeSpecification) {
        baseQualifiedName = typeSpecification.trim();
        if (baseQualifiedName.contains(PACKAGE_SEPARATOR)) {
            packageName = getPackageFromFullyQualifiedName(baseQualifiedName);
            baseShortName = baseQualifiedName
                    .substring(packageName.length() + 1);
            int index = baseShortName.lastIndexOf('.');
            if (index != -1) {
                baseShortName = baseShortName.substring(index + 1);
            }

            explicitlyImported = !JAVA_LANG.equals(packageName);
        } else {
            baseShortName = baseQualifiedName;
            explicitlyImported = false;
            packageName = "";

            if (SIMPLE_NAME_FOR_BYTE.equals(baseQualifiedName)) {
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getByteInstance();
            } else if (SIMPLE_NAME_FOR_SHORT.equals(baseQualifiedName)) {
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getShortInstance();
            } else if (SIMPLE_NAME_FOR_INT.equals(baseQualifiedName)) {
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper
                        .getIntegerInstance();
            } else if (SIMPLE_NAME_FOR_LONG.equals(baseQualifiedName)) {
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getLongInstance();
            } else if (SIMPLE_NAME_FOR_CHAR.equals(baseQualifiedName)) {
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper
                        .getCharacterInstance();
            } else if (SIMPLE_NAME_FOR_FLOAT.equals(baseQualifiedName)) {
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getFloatInstance();
            } else if (SIMPLE_NAME_FOR_DOUBLE.equals(baseQualifiedName)) {
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getDoubleInstance();
            } else if (SIMPLE_NAME_FOR_BOOLEAN.equals(baseQualifiedName)) {
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper
                        .getBooleanInstance();
            } else {
                primitive = false;
                primitiveTypeWrapper = null;
            }
        }
    }

    /**
     * 带泛型以及泛型通配符的类型解析
     * @param genericSpecification 限定类名或者类型SimpleName以及基本类型名称如int
     */
    private void genericParse(String genericSpecification) {
        int lastIndex = genericSpecification.lastIndexOf('>');
        if (lastIndex == -1) {
            // shouldn't happen - should be caught already, but just in case...
            throw new RuntimeException("Unknown type argument!");
        }
        String argumentString = genericSpecification.substring(1, lastIndex);
        // need to find "," outside of a <> bounds
        StringTokenizer st = new StringTokenizer(argumentString, ",<>", true);
        int openCount = 0;
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if ("<".equals(token)) {
                sb.append(token);
                openCount++;
            } else if (">".equals(token)) {
                sb.append(token);
                openCount--;
            } else if (",".equals(token)) {
                if (openCount == 0) {
                    typeArguments
                            .add(new FullyQualifiedJavaType(sb.toString()));
                    sb.setLength(0);
                } else {
                    sb.append(token);
                }
            } else {
                sb.append(token);
            }
        }

        if (openCount != 0) {
            throw new RuntimeException("Sorry！Open identifier '<; is not match to close identifier '>'!");
        }

        String finalType = sb.toString();
        if (!StringUtils.isBlank(finalType)) {
            typeArguments.add(new FullyQualifiedJavaType(finalType));
        }
    }

    /**
     * 从一个类的全限定类名中提取包名
     *
     * @param baseQualifiedName 全限定类名
     * @return 当前全限定类名中对应的包名，如果不是全限定类名。可能返回空
     */
    private static String getPackageFromFullyQualifiedName(String baseQualifiedName) {
        if (StringUtils.isBlank(baseQualifiedName)) {
            return "";
        }

        int index = baseQualifiedName.lastIndexOf('.');

        if (index == -1) {
            return "";
        }

        return baseQualifiedName.substring(0, index);
    }

    /**
     * 获取当前类型的泛型参数列表
     * @return 当前类型的泛型参数列表
     */
    public List<FullyQualifiedJavaType> getTypeArguments() {
        return typeArguments;
    }

    /**
     * 根据当前类型以及字符串字面值得到变量实际的值
     * @param strValue 字符串字面值
     * @return 实际的值
     */
    public String getCorrespondingValue(String strValue) {
        if (this.getShortName().equalsIgnoreCase(SIMPLE_NAME_FOR_BYTE)
                || this.getShortName().equalsIgnoreCase(SIMPLE_NAME_FOR_INT)
                || this.getShortName().equalsIgnoreCase(SIMPLE_ALIAS_FOR_INT)
                || this.getShortName().equalsIgnoreCase(SIMPLE_NAME_FOR_LONG)) {
            try {
                return Long.valueOf(strValue) + "";
            } catch (NumberFormatException ok) {
                //no need to process,we will return the default value
            }

            return "0";
        }

        if (this.getShortName().equalsIgnoreCase(SIMPLE_NAME_FOR_FLOAT)) {
            try {
                return Float.valueOf(strValue) + "F";
            } catch (NumberFormatException ok) {
                //no need to process,we will return the default value
            }

            return "0.0F";
        }

        if (this.getShortName().equalsIgnoreCase(SIMPLE_NAME_FOR_DOUBLE)) {
            try {
                return Double.valueOf(strValue) + "";
            } catch (NumberFormatException ok) {
                //no need to process,we will return the default value
            }

            return "0.0";
        }

        if (this.getShortName().equalsIgnoreCase(SIMPLE_NAME_FOR_BOOLEAN)) {
            return Boolean.valueOf(strValue) + "";
        }

        if (this.getShortName().equalsIgnoreCase(SIMPLE_NAME_FOR_STRING)) {
            return "\"" + strValue + "\"";
        }

        //可以继续扩展，比如Date类型的初始化等，不过没什么必要

        //当作初始化代码块处理
        return strValue;
    }

    @Override
    public int compareTo(FullyQualifiedJavaType other) {
        return getFullyQualifiedName().compareTo(other.getFullyQualifiedName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof FullyQualifiedJavaType)) {
            return false;
        }

        FullyQualifiedJavaType other = (FullyQualifiedJavaType) obj;

        return getFullyQualifiedName().equals(other.getFullyQualifiedName());
    }

    @Override
    public int hashCode() {
        return getFullyQualifiedName().hashCode();
    }

    @Override
    public String toString() {
        return getFullyQualifiedName();
    }

    public static FullyQualifiedJavaType getIntegerInstance(){
        return PrimitiveTypeWrapper.getIntegerInstance();
    }

    public static FullyQualifiedJavaType getLongInstance(){
        return PrimitiveTypeWrapper.getLongInstance();
    }

    /**
     * 查找指定全限定类名对应的对应的全限定类型
     * @param fullyQualifiedName 全限定类名
     * @return 当前全限定类名对应的全限定类型
     */
    public static FullyQualifiedJavaType getFullyQualifiedType(String fullyQualifiedName){
        if (KNOWN_NAME_TO_TYPE_MAP.containsKey(fullyQualifiedName)){
            return KNOWN_NAME_TO_TYPE_MAP.get(fullyQualifiedName);
        }

        //此行代码会进行类型解析并保存到缓存Map中，并且会返回当前解析得到的全限定类名
        return new FullyQualifiedJavaType(fullyQualifiedName);
    }

    public static FullyQualifiedJavaType getIntInstance() {
        if (intInstance == null) {
            intInstance = new FullyQualifiedJavaType("int");
        }

        return intInstance;
    }

    public static FullyQualifiedJavaType getNewMapInstance() {
        return new FullyQualifiedJavaType("java.util.Map");
    }

    public static FullyQualifiedJavaType getNewListInstance() {
        return new FullyQualifiedJavaType("java.util.List");
    }

    public static FullyQualifiedJavaType getNewHashMapInstance() {
        return new FullyQualifiedJavaType("java.util.HashMap");
    }

    public static FullyQualifiedJavaType getNewArrayListInstance() {
        return new FullyQualifiedJavaType("java.util.ArrayList");
    }

    public static FullyQualifiedJavaType getCollectionInstance() {
        return new FullyQualifiedJavaType("java.util.Collection");
    }

    public static FullyQualifiedJavaType getNewIteratorInstance() {
        return new FullyQualifiedJavaType("java.util.Iterator");
    }

    public static FullyQualifiedJavaType getStringInstance() {
        if (stringInstance == null) {
            stringInstance = new FullyQualifiedJavaType("java.lang.String");
        }

        return stringInstance;
    }

    public static FullyQualifiedJavaType getBooleanPrimitiveInstance() {
        if (booleanPrimitiveInstance == null) {
            booleanPrimitiveInstance = new FullyQualifiedJavaType("boolean");
        }

        return booleanPrimitiveInstance;
    }

    public static FullyQualifiedJavaType getObjectInstance() {
        if (objectInstance == null) {
            objectInstance = new FullyQualifiedJavaType("java.lang.Object");
        }

        return objectInstance;
    }

    public static FullyQualifiedJavaType getDateInstance() {
        if (dateInstance == null) {
            dateInstance = new FullyQualifiedJavaType("java.util.Date");
        }

        return dateInstance;
    }

    public static FullyQualifiedJavaType getClassInstance() {
        return new FullyQualifiedJavaType("java.lang.Class");
    }

    public static FullyQualifiedJavaType getGenericTypeInstance(){
        if (genericTypeInstance == null){
            genericTypeInstance = new FullyQualifiedJavaType("T");
        }

        return genericTypeInstance;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;

        this.baseQualifiedName = packageName + '.' + this.baseShortName;

        this.explicitlyImported = !JAVA_LANG.equalsIgnoreCase(packageName);
    }
}
