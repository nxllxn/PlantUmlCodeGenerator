package com.nxllxn.plantuml.java;

import com.nxllxn.plantuml.service.CodeAssembleService;
import com.nxllxn.plantuml.service.TemplateBasedCodeAssembleServiceImpl;
import com.nxllxn.plantuml.utils.AssembleUtil;

import java.util.ArrayList;
import java.util.List;

import static com.nxllxn.plantuml.utils.AssembleUtil.startNewLine;

public class TopLevelAnnotation extends AbstractCompilationUnit {
    private List<Method> methods;

    public TopLevelAnnotation() {
        this.methods = new ArrayList<>();
    }

    @Override
    public String getFormattedContent() {
        if (type == null){
            return "";
        }

        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        StringBuilder formattedContentBuilder = new StringBuilder();

        formattedContentBuilder.append(codeAssembleService.assembleElementTypeAnnotation());

        formattedContentBuilder.append(codeAssembleService.assembleTypeDefinition(type));

        formattedContentBuilder.append(codeAssembleService.assembleTypeParameters(typeParameters));

        formattedContentBuilder.append(AssembleUtil.BLOCK_OPEN_IDENTIFIER);

        //全部方法
        for (Method method : methods) {
            startNewLine(formattedContentBuilder);

            formattedContentBuilder.append("    ");

            formattedContentBuilder.append(method.getFormattedContent());
        }

        formattedContentBuilder.append(AssembleUtil.BLOCK_CLOSE_IDENTIFIER);

        return formattedContentBuilder.toString();
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void addMethod(Method method) {
        this.methods.add(method);
    }

    public void addMethods(List<Method> methods){
        if (methods == null) {
            return;
        }

        this.methods.addAll(methods);
    }

    @Override
    public String toString() {
        return "TopLevelAnnotation{" +
                "type=" + type +
                ", typeParameters=" + typeParameters +
                ", methods=" + methods +
                '}';
    }
}
