package com.nxllxn.plantuml.java;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * Field Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 26, 2017</pre>
 */
public class FieldTest {

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
        Field field = new Field();
        assertEquals("",field.getFormattedContent());

        field.setName("fieldName");
        assertEquals("fieldName",field.getFormattedContent());

        field.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("Bar fieldName",field.getFormattedContent());

        field.setStatic(true);
        assertEquals("{static} Bar fieldName",field.getFormattedContent());

        field.setVisibility(Visibility.PRIVATE);
        assertEquals("- {static} Bar fieldName",field.getFormattedContent());
    }

    /**
     * Method: getType()
     */
    @Test
    public void testGetType() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setType(FullyQualifiedJavaType type)
     */
    @Test
    public void testSetType() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: isStatic()
     */
    @Test
    public void testIsStatic() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setStatic(boolean aStatic)
     */
    @Test
    public void testSetStatic() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {
//TODO: Test goes here... 
    }


} 
