package com.nxllxn.plantuml.java;

import com.nxllxn.plantuml.service.CodeAssembleService;
import com.nxllxn.plantuml.service.TemplateBasedCodeAssembleServiceImpl;

public class Field extends AbstractElement{
    private FullyQualifiedJavaType type;
    private boolean isStatic;

    @Override
    public String getFormattedContent() {
        if (name == null){
            return "";
        }

        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        StringBuilder formattedContentBuilder = new StringBuilder();

        formattedContentBuilder.append(codeAssembleService.assembleVisibility(visibility));

        formattedContentBuilder.append(codeAssembleService.assembleStaticModifier(isStatic));

        formattedContentBuilder.append(codeAssembleService.assembleTypeDefinition(type));

        formattedContentBuilder.append(codeAssembleService.assembleFieldName(name));

        return formattedContentBuilder.toString();
    }

    public FullyQualifiedJavaType getType() {
        return type;
    }

    public void setType(FullyQualifiedJavaType type) {
        this.type = type;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    @Override
    public String toString() {
        return "Field{" +
                "visibility=" + visibility +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", isStatic=" + isStatic +
                '}';
    }
}
