package com.nxllxn;

import com.nxllxn.uml.UmlCodeGenerator;

import static com.nxllxn.uml.UmlCodeGenerator.FILE_TYPE_SVG;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    @org.junit.Test
    public void testGenerateUml() throws Exception{
        new UmlCodeGenerator().generate("/home/icekredit/Documents/workplace/AntiFraudEngineV1/uml","test", FILE_TYPE_SVG);
    }
}
