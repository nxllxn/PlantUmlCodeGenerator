package com.nxllxn.plantuml.service;

import com.nxllxn.plantuml.java.*;

import java.util.List;
import java.util.Set;

/**
 * 代码组装服务接口,用于定义代码组装的接口
 *
 * @author wenchao
 */
public interface CodeAssembleService {
    /**
     * 按照Java Doc內容组装Java Doc代码
     * @param javaDocs Java Doc行
     * @param indentLevel 缩进等级
     * @return 组装好的Java Doc代码
     */
    String assembleJavaDoc(List<String> javaDocs, int indentLevel);

    /**
     * 按照类，接口，枚举等的形式组装注解
     * @param annotations 待组装注解集合
     * @param indentLevel 缩进等级
     * @return 组装好的注解代码
     */
    String assembleClassAnnotation(Set<String> annotations, int indentLevel);

    /**
     * 按照方法注解的形式组装方法注解代码
     * @param annotations 待组装注解集合
     * @param indentLevel 缩进等级
     * @return 组装好的注解代码
     */
    String assembleMethodAnnotation(Set<String> annotations, int indentLevel);

    /**
     * 按照字段注解的形式组装字段注解代码
     * @param annotations 待组装注解集合
     * @param indentLevel 缩进等级
     * @return 组装好的注解代码
     */
    String assembleFieldAnnotation(Set<String> annotations, int indentLevel);

    /**
     * 按照方法入参注解的组织形式组装入参注解代码
     * @param annotations 待组装注解字符串集合
     * @param indentLevel 缩进等级
     * @return 组装后的注解
     */
    String assembleParameterAnnotation(Set<String> annotations, int indentLevel);

    /**
     * 按照可见性的代码组织格式组装代码
     *
     * @param visibility 可见性
     * @return 对应的可见性描述代码
     */
    String assembleVisibility(Visibility visibility);

    /**
     * 按照抽象类或者抽象方法的代码组织格式组装代码
     * @param isAbstract 当前类是否为抽象类
     * @return isAbstract为true:abstract （注意此处空格）,false:空字符串
     */
    String assembleAbstractModifier(boolean isAbstract);

    /**
     * 按照静态关键字代码组织格式组装代码
     * @param isStatic 当前元素是否是静态的
     * @return isStatic为true:static （注意此处空格）,false:空字符串
     */
    String assembleStaticModifier(boolean isStatic);

    /**
     * 按照final关键字的代码组织格式组装代码
     * @param isFinal 当前类型是否是final的
     * @return isFinal为true:final （注意此处空格）,false:空字符串
     */
    String assembleFinalModifier(boolean isFinal);

    /**
     * 按照synchronized关键字的代码组织格式组装代码
     * @param isSynchronized 当前代码块是否需要同步
     * @return isSynchronized为true:synchronized （注意此处空格）,false:空字符串
     */
    String assembleSynchronizeModifier(boolean isSynchronized);

    /**
     * 按照transient关键字的代码组织格式组装代码
     * @param isTransient 当前属性是否需要transient关键字标注
     * @return isTransient为true:transient （注意此处空格）,false:空字符串
     */
    String assembleTransientModifier(boolean isTransient);

    /**
     * 按照volatile关键字的代码组织格式组装代码
     * @param isVolatile 当前属性是否需要volatile关键字标注
     * @return isVolatile为true:volatile （注意此处空格）,false:空字符串
     */
    String assembleVolatileModifier(boolean isVolatile);

    /**
     * 按照default关键字的代码组织格式组装代码
     * @param isDefault 当前元素是否需要default关键字标注
     * @return isDefault为true:default （注意此处空格）,false:空字符串
     */
    String assembleDefaultModifier(boolean isDefault);

    /**
     * 按照native关键字的代码组织格式组装代码
     * @param isNative 当前元素是否需要native关键字标注
     * @return isNative为true:native （注意此处空格）,false:空字符串
     */
    String assembleNativeModifier(boolean isNative);

    /**
     * 按照类型申明的组织格式组装代码
     * @param fullyQualifiedJavaType 全限定类型
     * @return fullyQualifiedType （注意此处空格）
     */
    String assembleTypeDefinition(FullyQualifiedJavaType fullyQualifiedJavaType);


    /**
     * 按照类继承的格式组装代码
     * @param superClass 继承的超类
     * @return extend SuperClass&lt; ? extends XXX &gt; (注意此处空格) or extends SuperClass&lt; ? super XXX &gt; (注意此处空格);
     */
    String assembleClassExtension(FullyQualifiedJavaType superClass);

    /**
     * 按照接口实现的格式组装代码，多个类之间按照逗号分隔
     *
     * @param superInterfaces 实现的接口
     * @return implements SuperClass&lt; ? extends XXX &gt; (注意此处空格) or implements SuperClass&lt; ? super XXX &gt; (注意此处空格);
     */
    String assembleInterfaceImplements(Set<FullyQualifiedJavaType> superInterfaces);

    /**
     * 按照定义的格式组装代码
     *
     * @return `class `（注意此处空格）
     */
    String assembleElementTypeClass();

    /**
     * 按照接口定义的格式组装代码
     *
     * @return `interface `（注意此处空格）
     */
    String assembleElementTypeInterface();

    /**
     * 按照枚举定义的格式组装代码
     *
     * @return `enum `（注意此处空格）
     */
    String assembleElementTypeEnum();

    /**
     * 组装非静态导入部分代码
     * @param fullyQualifiedNameWithoutTypeParameters 非静态导入的全类名
     * @return `import&nbsp;com.xxx.ABC;(换行符)`
     */
    String assembleNonStaticImport(String fullyQualifiedNameWithoutTypeParameters);

    /**
     * 组装静态导入部分代码
     * @param staticImport 静态导入的类全类名
     * @return `import&nbsp;static com.xxx.ABC.STATIC_ATTRIBUTE;(换行符)`
     */
    String assembleStaticImport(String staticImport);

    /**
     * 组装包名称代码
     *
     * @param packageName 包名称
     * @return 包名称代码 `packageName;`
     */
    String assemblePackage(String packageName);

    /**
     * 组装返回值类型
      * @param returnType 返回值全限定类型
     * @return 返回值类型
     */
    String assembleReturnType(FullyQualifiedJavaType returnType);

    /**
     * 按照函数名代码组织格式组装代码
     * @param name 函数名称
     * @return 组装后的代码
     */
    String assembleFunctionName(String name);

    /**
     * 按照TypeParameter的组织形式组装代码
     * @param typeParameters 待组装TypeParameters
     * @return 组装后的代码，`&lt;SomeType&gt;`
     */
    String assembleTypeParameters(List<TypeParameter> typeParameters);

    /**
     * 按照方法入参的代码组织形式组装代码
     * @param parameters 方法入参
     * @return 组装后的代码
     */
    String assembleParameters(List<Parameter> parameters);

    /**
     * 按照方法异常抛出的代码组织形式组装代码
     * @param throwsExceptions 待抛出异常
     * @return 组装后的代码
     */
    String assembleThrowsExceptions(List<FullyQualifiedJavaType> throwsExceptions);

    /**
     * 按照方法体代码组织形式组装代码
     *
     * @param bodyLines 待组装方法体代码
     * @param indentLevel 缩进等级
     * @return 组装后的代码
     */
    String assembleCodeBlockBody(List<String> bodyLines, int indentLevel);

    /**
     * 按照变长参数的组织形式组装代码
     * @param isVarArgs 是否为变长参数
     * @return 组装后的代码
     */
    String assembleVariableArgs(boolean isVarArgs);

    /**
     * 按照参数名称的组织形式组装参数名称代码
     * @param name 参数名称
     * @return 组装后的代码
     */
    String assembleParameterName(String name);

    /**
     * 按照字段名称的组织形式组装字段名称代码
     * @param name 字段名称
     * @return 组装后的代码
     */
    String assembleFieldName(String name);

    /**
     * 按照方法名称的组织形式组装Java代码，并且需要考虑构造方法的特殊情况
     * @param name 方法名称
     * @param isConstructor 当前方法是否是构造方法
     * @return 组装好的代码
     */
    String assembleFunctionName(String name, boolean isConstructor);

    /**
     * 按照枚举常量的代码组织形式组装枚举常量列表
     * @param enumConstantName 当前枚举常量名称
     * @param fieldStringValues 当前枚举常量构造方法列表
     * @param fields 当前枚举常量字段
     * @param indentLevel 缩进等级
     * @return 组装好的代码
     */
    String assembleEnumConstant(String enumConstantName, List<String> fieldStringValues, List<Field> fields, int indentLevel);

    /**
     * 按照xml普通文本节点的代码组织形式组装xml代码
     * @param text text节点文本内容
     *
     * @return 组装好的代码
     */
    String assembleTextNode(String text);

    /**
     * 按照xml属性的代码组织形式组装属性代码
     * @param name 属性名称
     * @param value 属性字符串字面值
     * @return 组装好的代码
     */
    String assembleXmlAttributeNode(String name, String value);

    /**
     * 按照xml文档中publicId和systemId的组织形式装xml代码
     * @param rootNodeName xml文档跟节点名称
     * @param publicId xml文档publicId
     * @param systemId xml文档systemId
     * @return 组装好的代码
     */
    String assemblePublicIdAndSystemId(String rootNodeName, String publicId, String systemId);

    /**
     * 为xml文档添加Header
     *
     * @return xml文档标题
     */
    String assembleXmlHeader();

    String assembleElementTypeAnnotation();

    String assembleTypeAbstractModifier(boolean isAbstract);
}
