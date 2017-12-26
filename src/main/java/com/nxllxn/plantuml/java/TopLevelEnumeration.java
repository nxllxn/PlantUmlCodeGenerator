package com.nxllxn.plantuml.java;

import com.nxllxn.plantuml.service.CodeAssembleService;
import com.nxllxn.plantuml.service.TemplateBasedCodeAssembleServiceImpl;
import com.nxllxn.plantuml.utils.AssembleUtil;

import java.util.ArrayList;
import java.util.List;

import static com.nxllxn.plantuml.utils.AssembleUtil.indentWith;
import static com.nxllxn.plantuml.utils.AssembleUtil.startNewLine;

public class TopLevelEnumeration extends AbstractCompilationUnit {
    private List<Field> fields;
    private List<Method> methods;
    private List<EnumConstant> enumConstants;

    public TopLevelEnumeration() {
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.enumConstants = new ArrayList<>();
    }

    @Override
    public String getFormattedContent() {
        if (type == null){
            return "";
        }

        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        StringBuilder formattedContentBuilder = new StringBuilder();

        formattedContentBuilder.append(codeAssembleService.assembleElementTypeEnum());

        formattedContentBuilder.append(codeAssembleService.assembleTypeDefinition(type));

        formattedContentBuilder.append(AssembleUtil.BLOCK_OPEN_IDENTIFIER);

        startNewLine(formattedContentBuilder);

        if (enumConstants != null && !enumConstants.isEmpty()) {
            boolean isFirst = true;
            for (EnumConstant enumConstant : enumConstants) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    //枚举常量后面的逗号
                    formattedContentBuilder.append(",");
                    startNewLine(formattedContentBuilder);
                }

                formattedContentBuilder.append(enumConstant.getFormattedContent(1, fields));
            }

            //最后一行枚举常量后面是逗号
            formattedContentBuilder.append(";");

            startNewLine(formattedContentBuilder);
        }

        //如果没有枚举常量，也必须添加一个空白的分号，并且提供相应的缩进
        if (enumConstants == null || enumConstants.isEmpty()) {
            //添加缩进
            indentWith(formattedContentBuilder, 1);
            //添加一个分号
            formattedContentBuilder.append(";");

            startNewLine(formattedContentBuilder);
        }

        //全部字段
        for (Field field : fields) {
            startNewLine(formattedContentBuilder);

            formattedContentBuilder.append("    ");

            formattedContentBuilder.append(field.getFormattedContent());
        }

        if (!methods.isEmpty()){
            startNewLine(formattedContentBuilder);
        }

        //全部方法
        for (Method method : methods) {
            startNewLine(formattedContentBuilder);

            formattedContentBuilder.append("    ");

            formattedContentBuilder.append(method.getFormattedContent());
        }

        startNewLine(formattedContentBuilder);
        formattedContentBuilder.append(AssembleUtil.BLOCK_CLOSE_IDENTIFIER);

        return formattedContentBuilder.toString();
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public List<EnumConstant> getEnumConstants() {
        return enumConstants;
    }

    public void addField(Field field) {
        if (field == null) {
            return;
        }

        this.fields.add(field);
    }

    public void addMethod(Method method) {
        if (method == null) {
            return;
        }

        methods.add(method);
    }

    public void addEnumConstant(EnumConstant enumConstant) {
        if (enumConstant == null) {
            return;
        }

        enumConstants.add(enumConstant);
    }

    public void addFields(List<Field> fields){
        if (fields == null){
            return;
        }

        this.fields.addAll(fields);
    }

    public void addMethods(List<Method> methods){
        if (methods == null) {
            return;
        }

        this.methods.addAll(methods);
    }

    public void addEnumConstants(List<EnumConstant> enumConstants) {
        if (enumConstants == null) {
            return;
        }

        this.enumConstants.addAll(enumConstants);
    }

    @Override
    public String toString() {
        return "TopLevelEnumeration{" +
                "type=" + type +
                ", typeParameters=" + typeParameters +
                ", fields=" + fields +
                ", methods=" + methods +
                ", enumConstants=" + enumConstants +
                '}';
    }
}
