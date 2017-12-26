package com.nxllxn.plantuml.java;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCompilationUnit implements CompilationUnit {
    protected FullyQualifiedJavaType type;
    protected List<TypeParameter> typeParameters;

    public AbstractCompilationUnit() {
        this.typeParameters = new ArrayList<>();
    }

    public FullyQualifiedJavaType getType() {
        return type;
    }

    public void setType(FullyQualifiedJavaType type) {
        this.type = type;
    }

    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public void addTypeParameter(TypeParameter typeParameter) {
        if (typeParameter == null){
            return;
        }

        this.typeParameters.add(typeParameter);
    }

    @Override
    public String toString() {
        return "AbstractCompilationUnit{" +
                "type=" + type +
                ", typeParameters=" + typeParameters +
                '}';
    }
}
