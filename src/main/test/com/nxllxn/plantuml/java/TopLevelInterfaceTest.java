package com.nxllxn.plantuml.java;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * TopLevelInterface Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 26, 2017</pre>
 */
public class TopLevelInterfaceTest {

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
        TopLevelInterface topLevelInterface = new TopLevelInterface();

        assertEquals("",topLevelInterface.getFormattedContent());

        topLevelInterface.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("interface Bar { \n} ",topLevelInterface.getFormattedContent());

        Field field = new Field();
        field.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        field.setName("fieldName");
        topLevelInterface.addField(field);

        assertEquals("interface Bar { \n    Bar fieldName\n} ",topLevelInterface.getFormattedContent());

        Method method = new Method();
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("abstractMethod");
        topLevelInterface.addMethod(method);
        assertEquals("interface Bar { \n    Bar fieldName\n\n    String abstractMethod() \n} ",topLevelInterface.getFormattedContent());
    }
}
