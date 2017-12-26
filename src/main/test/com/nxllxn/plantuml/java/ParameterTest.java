package test.com.nxllxn.plantuml.java;

import com.nxllxn.plantuml.java.FullyQualifiedJavaType;
import com.nxllxn.plantuml.java.Parameter;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * Parameter Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 26, 2017</pre>
 */
public class ParameterTest {

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
        Parameter parameter = new Parameter();
        assertEquals("",parameter.getFormattedContent());

        parameter.setName("parameterName");
        assertEquals("parameterName",parameter.getFormattedContent());

        parameter.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        assertEquals("Bar parameterName",parameter.getFormattedContent());

        parameter.setVarAs(true);
        assertEquals("Bar... parameterName",parameter.getFormattedContent());
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
     * Method: getName()
     */
    @Test
    public void testGetName() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setName(String name)
     */
    @Test
    public void testSetName() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: isVarAs()
     */
    @Test
    public void testIsVarAs() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setVarAs(boolean varAs)
     */
    @Test
    public void testSetVarAs() throws Exception {
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
