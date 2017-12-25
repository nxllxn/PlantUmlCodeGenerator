package com.nxllxn.entities;

/**
 * Created by icekredit on 12/24/16.
 */
public class AttributeIdentifier implements Identifier {
    protected String attributeModifier;

    public static final String ATTRIBUTE_MODIFIER_PUBLIC = "+";
    public static final String ATTRIBUTE_MODIFIER_PROTECTED = "#";
    public static final String ATTRIBUTE_MODIFIER_PRIVATE = "-";

    protected String attributeType;
    protected String attributeName;

    public AttributeIdentifier(String attributeModifier, String attributeType, String attributeName) {
        this.attributeModifier = attributeModifier;
        this.attributeType = attributeType;
        this.attributeName = attributeName;
    }

    public AttributeIdentifier() {
    }

    public String getAttributeModifier() {
        return attributeModifier;
    }

    public void setAttributeModifier(String attributeModifier) {
        this.attributeModifier = attributeModifier;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    @Override
    public String toString() {
        return "AttributeIdentifier{" +
                "attributeModifier='" + attributeModifier + '\'' +
                ", attributeType='" + attributeType + '\'' +
                ", attributeName='" + attributeName + '\'' +
                '}';
    }

    public String show() {
        return String.format("%s%s:%s",attributeModifier,attributeName,attributeType);
    }
}
