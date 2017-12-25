package com.nxllxn.entities;

/**
 * Created by icekredit on 12/24/16.
 */
public class ArgumentIdentifier implements Identifier{
    protected String argumentType;
    protected String argumentName;

    public ArgumentIdentifier(String argumentType, String argumentName) {
        this.argumentType = argumentType;
        this.argumentName = argumentName;
    }

    public ArgumentIdentifier() {
    }

    public String getArgumentType() {
        return argumentType;
    }

    public void setArgumentType(String argumentType) {
        this.argumentType = argumentType;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public void setArgumentName(String argumentName) {
        this.argumentName = argumentName;
    }

    @Override
    public String toString() {
        return "ArgumentIdentifier{" +
                "argumentName='" + argumentName + '\'' +
                ", argumentType='" + argumentType + '\'' +
                '}';
    }

    public String show() {
        return String.format("%s:%s",argumentName,argumentType);
    }
}
