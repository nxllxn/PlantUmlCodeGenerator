package com.nxllxn.plantuml.java;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * TopLevelClass Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 26, 2017</pre>
 */
public class TopLevelClassTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getFormattedContent()
     */
    @Test
    public void testGetFormattedContent() throws Exception {
        TopLevelClass topLevelClass = new TopLevelClass();

        assertEquals("",topLevelClass.getFormattedContent());

        topLevelClass.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("class Bar { \n} ",topLevelClass.getFormattedContent());

        topLevelClass.setAbstract(true);
        assertEquals("{abstract} class Bar { \n} ",topLevelClass.getFormattedContent());

        topLevelClass.setSuperClass(new FullyQualifiedJavaType("com.foo.SuperClass"));
        assertEquals("{abstract} class Bar extends SuperClass { \n} ",topLevelClass.getFormattedContent());

        topLevelClass.addSuperInterface(new FullyQualifiedJavaType("com.foo.FirstInterface"));
        assertEquals("{abstract} class Bar extends SuperClass implements FirstInterface { \n} ",topLevelClass.getFormattedContent());

        topLevelClass.addSuperInterface(new FullyQualifiedJavaType("com.foo.SecondInterface"));
        assertEquals("{abstract} class Bar extends SuperClass implements FirstInterface,SecondInterface { \n} ",topLevelClass.getFormattedContent());

        Field field = new Field();
        field.setName("fieldName");
        field.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        field.setStatic(true);
        field.setVisibility(Visibility.PRIVATE);
        topLevelClass.addField(field);

        assertEquals("{abstract} class Bar extends SuperClass implements FirstInterface,SecondInterface { \n    - {static} Bar fieldName\n} ",topLevelClass.getFormattedContent());

        Method method = new Method();
        method.setName("methodName");
        method.setVisibility(Visibility.PRIVATE);
        method.setAbstract(true);
        method.setStatic(true);
        Parameter parameter = new Parameter();
        parameter.setName("parameterName");
        parameter.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        parameter.setVarAs(false);
        method.addParameter(parameter);
        parameter = new Parameter();
        parameter.setName("parameterAnother");
        parameter.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        parameter.setVarAs(true);
        method.addParameter(parameter);
        assertEquals("- {static} {abstract} void methodName(Bar parameterName, Bar... parameterAnother) ",method.getFormattedContent());

        topLevelClass.addMethod(method);
        assertEquals("{abstract} class Bar extends SuperClass implements FirstInterface,SecondInterface { \n    - {static} Bar fieldName\n\n    - {static} {abstract} void methodName(Bar parameterName, Bar... parameterAnother) \n} ",topLevelClass.getFormattedContent());
    }
}
