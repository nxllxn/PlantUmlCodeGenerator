package com.nxllxn.plantuml.java;

import com.nxllxn.plantuml.service.CodeAssembleService;
import com.nxllxn.plantuml.service.TemplateBasedCodeAssembleServiceImpl;
import com.nxllxn.plantuml.utils.AssembleUtil;

/**
 * 方法形参实体类
 *
 * @author
 */
public class Parameter {
    /**
     * 参数类型
     */
    private FullyQualifiedJavaType type;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 是否变长参数
     */
    private boolean isVarAs;


    /**
     * 获取当前类型对应的代码片段
     *
     * @return 代码片段
     */
    public String getFormattedContent() {
        CodeAssembleService codeAssembleService = TemplateBasedCodeAssembleServiceImpl.getSingleInstance();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(codeAssembleService.assembleTypeDefinition(type));

        if (isVarAs){
            //回退一个类型后面的空格
            AssembleUtil.rollbackExtraSpaceInTheEnd(stringBuilder);

            //填充 `... `
            stringBuilder.append(codeAssembleService.assembleVariableArgs(isVarAs));
        }

        stringBuilder.append(codeAssembleService.assembleParameterName(name));

        return stringBuilder.toString();
    }

    public FullyQualifiedJavaType getType() {
        return type;
    }

    public void setType(FullyQualifiedJavaType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVarAs() {
        return isVarAs;
    }

    public void setVarAs(boolean varAs) {
        isVarAs = varAs;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", isVarAs=" + isVarAs +
                '}';
    }
}
