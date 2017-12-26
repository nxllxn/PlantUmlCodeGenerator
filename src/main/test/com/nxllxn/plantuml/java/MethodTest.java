package test.com.nxllxn.plantuml.java;

import com.nxllxn.plantuml.java.FullyQualifiedJavaType;
import com.nxllxn.plantuml.java.Method;
import com.nxllxn.plantuml.java.Parameter;
import com.nxllxn.plantuml.java.Visibility;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * Method Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 26, 2017</pre>
 */
public class MethodTest {

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
        Method method = new Method();
        assertEquals("",method.getFormattedContent());

        method.setName("methodName");
        assertEquals("void methodName() ",method.getFormattedContent());

        method.setVisibility(Visibility.PRIVATE);
        assertEquals("- void methodName() ",method.getFormattedContent());

        method.setAbstract(true);
        assertEquals("- {abstract} void methodName() ",method.getFormattedContent());

        method.setStatic(true);
        assertEquals("- {static} {abstract} void methodName() ",method.getFormattedContent());

        Parameter parameter = new Parameter();
        parameter.setName("parameterName");
        parameter.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        parameter.setVarAs(false);
        method.addParameter(parameter);
        assertEquals("- {static} {abstract} void methodName(Bar parameterName) ",method.getFormattedContent());

        parameter = new Parameter();
        parameter.setName("parameterAnother");
        parameter.setType(new FullyQualifiedJavaType("com.foo.Bar"));
        parameter.setVarAs(true);
        method.addParameter(parameter);
        assertEquals("- {static} {abstract} void methodName(Bar parameterName, Bar... parameterAnother) ",method.getFormattedContent());
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
     * Method: isAbstract()
     */
    @Test
    public void testIsAbstract() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setAbstract(boolean anAbstract)
     */
    @Test
    public void testSetAbstract() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: isConstructor()
     */
    @Test
    public void testIsConstructor() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setConstructor(boolean constructor)
     */
    @Test
    public void testSetConstructor() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getReturnType()
     */
    @Test
    public void testGetReturnType() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setReturnType(FullyQualifiedJavaType returnType)
     */
    @Test
    public void testSetReturnType() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getParameters()
     */
    @Test
    public void testGetParameters() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: addParameter(Parameter parameter)
     */
    @Test
    public void testAddParameter() throws Exception {
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
