package com.icekredit.entities;

/**
 * Created by icekredit on 12/24/16.
 */
public class RelationIdentifier implements Identifier {
    protected String relationType;

    /** 泛化 is a */
    public static final String RELATION_TYPE_GENERALIZATION = "<|--";

    /** 关联 has a(with p2p) */
    public static final String RELATION_TYPE_ASSOCIATION = "<--";

    /** 组合 contains a */
    public static final String RELATION_TYPE_COMPOSITION = "*--";

    /** 聚合 has a(not in p2p)*/
    public static final String RELATION_TYPE_AGGREGATION = "o--";

    /** 实现 is a */
    public static final String RELATION_TYPE_REALIZATION = "<|..";

    /** 依赖 use a */
    public static final String RELATION_TYPE_DEPENDENCY = "<..";

    protected String currentClassName;
    protected String anotherClassName;

    public RelationIdentifier(String relationType, String currentClassName, String anotherClassName) {
        this.relationType = relationType;
        this.currentClassName = currentClassName;
        this.anotherClassName = anotherClassName;
    }

    public RelationIdentifier() {
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getAnotherClassName() {
        return anotherClassName;
    }

    public void setAnotherClassName(String anotherClassName) {
        this.anotherClassName = anotherClassName;
    }

    public String getCurrentClassName() {
        return currentClassName;
    }

    public void setCurrentClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    @Override
    public String toString() {
        return "RelationIdentifier{" +
                "anotherClassName='" + anotherClassName + '\'' +
                ", currentClassName='" + currentClassName + '\'' +
                ", relationType='" + relationType + '\'' +
                '}';
    }

    public String show() {
        return String.format("%s %s %s",currentClassName,relationType,anotherClassName);
    }
}
