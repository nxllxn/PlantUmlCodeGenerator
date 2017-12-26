package com.nxllxn.plantuml.java;

import com.nxllxn.plantuml.service.CodeAssembleService;
import com.nxllxn.plantuml.service.TemplateBasedCodeAssembleServiceImpl;
import com.nxllxn.plantuml.utils.AssembleUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nxllxn.plantuml.utils.AssembleUtil.extraEmptyLine;
import static com.nxllxn.plantuml.utils.AssembleUtil.rollbackExtraSpaceInTheEnd;
import static com.nxllxn.plantuml.utils.AssembleUtil.startNewLine;

public class TopLevelClass extends AbstractCompilationUnit {
    private FullyQualifiedJavaType superClass;

    private Set<FullyQualifiedJavaType> superInterfaces;

    private List<Field> fields;

    private List<Method> methods;

    private boolean isAbstract;

    public TopLevelClass() {
        superInterfaces = new HashSet<>();
        fields = new ArrayList<>();
        methods = new ArrayList<>();
    }

    @Override
    public String getFormattedContent() {
        if (type == null){
            return "";
        }

        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        StringBuilder formattedContentBuilder = new StringBuilder();

        formattedContentBuilder.append(codeAssembleService.assembleTypeAbstractModifier(isAbstract));

        formattedContentBuilder.append(codeAssembleService.assembleElementTypeClass());

        formattedContentBuilder.append(codeAssembleService.assembleTypeDefinition(type));

        formattedContentBuilder.append(codeAssembleService.assembleClassExtension(superClass));

        formattedContentBuilder.append(codeAssembleService.assembleInterfaceImplements(superInterfaces));

        formattedContentBuilder.append(AssembleUtil.BLOCK_OPEN_IDENTIFIER);

        //全部字段
        for (Field field : fields) {
            startNewLine(formattedContentBuilder);

            formattedContentBuilder.append("    ");

            formattedContentBuilder.append(field.getFormattedContent());
        }

        if (!fields.isEmpty() && !methods.isEmpty()){
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

    public FullyQualifiedJavaType getSuperClass() {
        return superClass;
    }

    public void setSuperClass(FullyQualifiedJavaType superClass) {
        this.superClass = superClass;
    }

    public Set<FullyQualifiedJavaType> getSuperInterfaces() {
        return superInterfaces;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void addSuperInterface(FullyQualifiedJavaType superInterface){
        if (superInterface == null){
            return;
        }

        this.superInterfaces.add(superInterface);
    }

    public void addField(Field field){
        if (field == null){
            return;
        }

        this.fields.add(field);
    }

    public void addMethod(Method method){
        if (method == null){
            return;
        }

        this.methods.add(method);
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

    public void addSuperInterfaces(List<FullyQualifiedJavaType> superInterfaces){
        if (superInterfaces == null){
            return;
        }

        this.superInterfaces.addAll(superInterfaces);
    }

    @Override
    public String toString() {
        return "TopLevelClass{" +
                "type=" + type +
                ", typeParameters=" + typeParameters +
                ", superClass=" + superClass +
                ", superInterfaces=" + superInterfaces +
                ", isAbstract=" + isAbstract +
                ", fields=" + fields +
                ", methods=" + methods +
                '}';
    }
}
