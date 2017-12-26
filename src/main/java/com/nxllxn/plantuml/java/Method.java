package com.nxllxn.plantuml.java;

import com.nxllxn.plantuml.service.CodeAssembleService;
import com.nxllxn.plantuml.service.TemplateBasedCodeAssembleServiceImpl;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Method extends AbstractElement {
    private boolean isStatic;

    private boolean isAbstract;

    private boolean isConstructor;

    private FullyQualifiedJavaType returnType;

    private List<Parameter> parameters;

    public Method() {
        this.parameters = new ArrayList<>();
    }

    @Override
    public String getFormattedContent() {
        if (StringUtils.isBlank(name)){
            return "";
        }

        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        StringBuilder formattedContentBuilder = new StringBuilder();

        formattedContentBuilder.append(codeAssembleService.assembleVisibility(visibility));

        //static 修饰符
        formattedContentBuilder.append(codeAssembleService.assembleStaticModifier(isStatic));

        //abstract 修饰符
        formattedContentBuilder.append(codeAssembleService.assembleAbstractModifier(isAbstract));

        //方法返回值
        if (!isConstructor){
            formattedContentBuilder.append(codeAssembleService.assembleReturnType(returnType));
        }

        //方法名称
        formattedContentBuilder.append(codeAssembleService.assembleFunctionName(name,isConstructor));

        //方法参数列表
        formattedContentBuilder.append(codeAssembleService.assembleParameters(parameters));

        return formattedContentBuilder.toString();
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public boolean isConstructor() {
        return isConstructor;
    }

    public void setConstructor(boolean constructor) {
        isConstructor = constructor;
    }

    public FullyQualifiedJavaType getReturnType() {
        return returnType;
    }

    public void setReturnType(FullyQualifiedJavaType returnType) {
        this.returnType = returnType;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void addParameter(Parameter parameter) {
        if (parameter == null){
            return;
        }

        this.parameters.add(parameter);
    }

    @Override
    public String toString() {
        return "Method{" +
                "visibility=" + visibility +
                ", name='" + name + '\'' +
                ", isStatic=" + isStatic +
                ", isAbstract=" + isAbstract +
                ", isConstructor=" + isConstructor +
                ", returnType=" + returnType +
                ", parameters=" + parameters +
                '}';
    }
}
