package com.nxllxn.plantuml.java;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * TopLevelAnnotation Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 26, 2017</pre>
 */
public class TopLevelAnnotationTest {

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
        TopLevelAnnotation topLevelAnnotation = new TopLevelAnnotation();

        assertEquals("",topLevelAnnotation.getFormattedContent());

        topLevelAnnotation.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("@interface Bar { } ",topLevelAnnotation.getFormattedContent());

        Method method = new Method();
        method.setName("value");
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        assertEquals("String value() ",method.getFormattedContent());

        topLevelAnnotation.addMethod(method);
        assertEquals("@interface Bar { \n    String value() } ",topLevelAnnotation.getFormattedContent());
    }
}
