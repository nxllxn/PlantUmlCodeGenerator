package com.nxllxn.plantuml.java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Parameterized Type
 *
 * @author wenchao
 */
public class TypeParameter {
    private List<FullyQualifiedJavaType> extendsTypes;

    private String name;

    public TypeParameter() {
        this(null);
    }

    public TypeParameter(String name) {
        this(name,new ArrayList<>());
    }

    public TypeParameter(String name,List<FullyQualifiedJavaType> extendsTypes) {
        this.name = name;
        this.extendsTypes = extendsTypes;
    }

    public List<FullyQualifiedJavaType> getExtendsTypes() {
        return extendsTypes;
    }

    public void addExtendType(FullyQualifiedJavaType extendType){
        if (extendType == null){
            return;
        }

        this.extendsTypes.add(extendType);
    }

    public void addExtendsTypes(List<FullyQualifiedJavaType> extendsTypes){
        if (extendsTypes == null){
            return;
        }

        this.extendsTypes.addAll(extendsTypes);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TypeParameter{" +
                "extendsTypes=" + extendsTypes +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * 获取格式化的代码内容
     * @return 组装后的代码
     */
    public String getFormattedContent(){
        StringBuilder sb = new StringBuilder();

        sb.append(name);
        if (!extendsTypes.isEmpty()) {

            sb.append(" extends "); 
            boolean addAnd = false;
            for (FullyQualifiedJavaType type : extendsTypes) {
                if (addAnd) {
                    sb.append(" & "); 
                } else {
                    addAnd = true;
                }
                sb.append(type.getShortName());
            }
        }

        return sb.toString();
    }
}
