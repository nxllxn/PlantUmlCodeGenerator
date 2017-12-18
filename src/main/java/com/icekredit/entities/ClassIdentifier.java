package com.icekredit.entities;

import com.icekredit.utils.ContactUtil;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by icekredit on 12/24/16.
 */
public class ClassIdentifier implements Identifier {
    protected String classType;

    public static final String CLASS_TYPE_CLASS = "class";
    public static final String CLASS_TYPE_INTERFACE = "interface";

    protected String className;
    protected List<AttributeIdentifier> attributeIdentifiers;
    protected List<FunctionIdentifier> functionIdentifiers;
    protected List<RelationIdentifier> relationIdentifiers;
    protected String superClass;
    protected List<String> superInterfaces;

    public ClassIdentifier() {
    }

    public ClassIdentifier(String classType, String className, List<AttributeIdentifier> attributeIdentifiers, List<FunctionIdentifier> functionIdentifiers, List<RelationIdentifier> relationIdentifiers, String superClass, List<String> superInterfaces) {
        this.classType = classType;
        this.className = className;
        this.attributeIdentifiers = attributeIdentifiers;
        this.functionIdentifiers = functionIdentifiers;
        this.relationIdentifiers = relationIdentifiers;
        this.superClass = superClass;
        this.superInterfaces = superInterfaces;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public List<String> getSuperInterfaces() {
        return superInterfaces;
    }

    public void setSuperInterfaces(List<String> superInterfaces) {
        this.superInterfaces = superInterfaces;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<AttributeIdentifier> getAttributeIdentifiers() {
        return attributeIdentifiers;
    }

    public void setAttributeIdentifiers(List<AttributeIdentifier> attributeIdentifiers) {
        this.attributeIdentifiers = attributeIdentifiers;
    }

    public List<FunctionIdentifier> getFunctionIdentifiers() {
        return functionIdentifiers;
    }

    public void setFunctionIdentifiers(List<FunctionIdentifier> functionIdentifiers) {
        this.functionIdentifiers = functionIdentifiers;
    }

    public List<RelationIdentifier> getRelationIdentifiers() {
        return relationIdentifiers;
    }

    public void setRelationIdentifiers(List<RelationIdentifier> relationIdentifiers) {
        this.relationIdentifiers = relationIdentifiers;
    }

    @Override
    public String toString() {
        return "ClassIdentifier{" +
                "className='" + className + '\'' +
                ", attributeIdentifiers=" + attributeIdentifiers +
                ", functionIdentifiers=" + functionIdentifiers +
                ", relationIdentifiers=" + relationIdentifiers +
                '}';
    }

    public String show() {
        String attributesStr = assembleAttributes();
        String functionsStr = assembleFunctions();
        String relationStr = assembleRelations();
        String superClassStr = assembleSuperClass();
        String superInterfaceClassStr = assembleSuperInterface();

        return String.format("%s %s%s%s{\n%s%s\n}\n\n%s",
                classType,
                className,
                StringUtils.isEmpty(superClassStr) ? "" : String.format(" extends %s",superClassStr),
                StringUtils.isEmpty(superInterfaceClassStr) ? "" : String.format(" implements %s",superInterfaceClassStr),
                StringUtils.isEmpty(attributesStr) ? "" : String.format("    %s\n\n",attributesStr),
                StringUtils.isEmpty(functionsStr) ? "" : String.format("    %s",functionsStr),
                StringUtils.isEmpty(relationStr) ? "" : String.format("%s\n\n",relationStr));
    }

    private String assembleSuperInterface() {
        return String.join(",m",superInterfaces);
    }

    private String assembleSuperClass() {
        return superClass;
    }

    private String assembleAttributes() {
        return ContactUtil.join(attributeIdentifiers,"\n    ");
    }

    private String assembleFunctions() {
        return ContactUtil.join(functionIdentifiers,"\n    ");
    }

    private String assembleRelations() {
        return ContactUtil.join(relationIdentifiers,"\n");
    }
}
