package com.nxllxn.plantuml.java;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * TopLevelEnumeration Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 26, 2017</pre>
 */
public class TopLevelEnumerationTest {

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
        TopLevelEnumeration topLevelEnumeration = new TopLevelEnumeration();

        assertEquals("",topLevelEnumeration.getFormattedContent());

        topLevelEnumeration.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("enum Bar { \n    ;\n\n} ",topLevelEnumeration.getFormattedContent());

        EnumConstant enumConstant = new EnumConstant();
        enumConstant.setEnumConstantName("enum_constant");
        topLevelEnumeration.addEnumConstant(enumConstant);
        assertEquals("enum Bar { \n    ENUM_CONSTANT;\n\n} ",topLevelEnumeration.getFormattedContent());

        Field field = new Field();
        field.setVisibility(Visibility.PRIVATE);
        field.setType(new FullyQualifiedJavaType("int"));
        field.setName("fieldName");
        topLevelEnumeration.addField(field);
        assertEquals("enum Bar { \n    ENUM_CONSTANT;\n\n    - int fieldName\n} ",topLevelEnumeration.getFormattedContent());

        Method method = new Method();
        method.setVisibility(Visibility.PUBLIC);
        method.setName("toString");
        method.setReturnType(new FullyQualifiedJavaType("java.lang.String"));
        topLevelEnumeration.addMethod(method);
        assertEquals("enum Bar { \n    ENUM_CONSTANT;\n\n    - int fieldName\n\n    + String toString() \n} ",topLevelEnumeration.getFormattedContent());
    }
}
