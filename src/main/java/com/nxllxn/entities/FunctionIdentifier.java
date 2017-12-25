package com.nxllxn.entities;

import java.util.List;

/**
 * Created by icekredit on 12/24/16.
 */
public class FunctionIdentifier implements Identifier {
    protected String functionModifier;

    public static final String FUNCTION_MODIFIER_PUBLIC = "+";
    public static final String FUNCTION_MODIFIER_PROTECTED = "#";
    public static final String FUNCTION_MODIFIER_PRIVATE = "-";

    protected String functionName;
    protected List<ArgumentIdentifier> argumentIdentifiers;

    public FunctionIdentifier(String functionModifier, String functionName, List<ArgumentIdentifier> argumentIdentifiers) {
        this.functionModifier = functionModifier;
        this.functionName = functionName;
        this.argumentIdentifiers = argumentIdentifiers;
    }

    public FunctionIdentifier() {
    }

    public String getFunctionModifier() {
        return functionModifier;
    }

    public void setFunctionModifier(String functionModifier) {
        this.functionModifier = functionModifier;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public List<ArgumentIdentifier> getArgumentIdentifiers() {
        return argumentIdentifiers;
    }

    public void setArgumentIdentifiers(List<ArgumentIdentifier> argumentIdentifiers) {
        this.argumentIdentifiers = argumentIdentifiers;
    }

    @Override
    public String toString() {
        return "FunctionIdentifier{" +
                "functionModifier='" + functionModifier + '\'' +
                ", functionName='" + functionName + '\'' +
                ", argumentIdentifiers=" + argumentIdentifiers +
                '}';
    }

    public String show() {
        return String.format("%s%s(%s)",functionModifier,functionName,assembleFunctionArguments());
    }

    private String assembleFunctionArguments() {
        if(argumentIdentifiers == null || argumentIdentifiers.size() == 0){
            return "";
        }

        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for(ArgumentIdentifier argumentIdentifier:argumentIdentifiers){
            if(!isFirst){
                builder.append(",");
            }

            builder.append(argumentIdentifier.show());

            isFirst = false;
        }

        return builder.toString();
    }
}
