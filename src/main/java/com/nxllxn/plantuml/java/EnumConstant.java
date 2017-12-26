package com.nxllxn.plantuml.java;

import com.nxllxn.plantuml.service.CodeAssembleService;
import com.nxllxn.plantuml.service.TemplateBasedCodeAssembleServiceImpl;

import java.util.List;

/**
 * 枚举常量定义
 *
 * @author wenchao
 */
public class EnumConstant {
    /**
     * 枚举常量名称
     */
    private String enumConstantName;

    /**
     * 枚举常量属性（由fields指定）字符串字面值
     */
    private List<String> fieldStringValues;

    public String getEnumConstantName() {
        return enumConstantName;
    }

    public void setEnumConstantName(String enumConstantName) {
        this.enumConstantName = enumConstantName;
    }

    public List<String> getFieldStringValues() {
        return fieldStringValues;
    }

    public void setFieldStringValues(List<String> fieldStringValues) {
        this.fieldStringValues = fieldStringValues;
    }

    @Override
    public String toString() {
        return "EnumConstant{" +
                "enumConstantName='" + enumConstantName + '\'' +
                ", fieldStringValues=" + fieldStringValues +
                '}';
    }

    public String getFormattedContent(int indentLevel, List<Field> fields) {
        //TODO 此处需要根据Field的类型，将fieldStringValues转换为对应的值，然后组装成枚举常量字符串

        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(codeAssembleService.assembleEnumConstant(enumConstantName,fieldStringValues,fields,indentLevel));

        return stringBuilder.toString();
    }
}
