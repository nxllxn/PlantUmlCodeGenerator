package com.nxllxn.plantuml;

import com.nxllxn.plantuml.codegen.PlantUmlCodeGenerator;
import com.nxllxn.plantuml.config.ClassScannerConfiguration;
import com.nxllxn.plantuml.java.CompilationUnit;
import com.nxllxn.plantuml.java.FullyQualifiedJavaType;
import com.nxllxn.plantuml.writer.PrimitivePlantUmlCodeFileWriter;
import com.nxllxn.plantuml.writer.UmlDiagramWriter;

import java.util.HashSet;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ClassScannerConfiguration classScannerConfiguration = new ClassScannerConfiguration();

        classScannerConfiguration.setRootPackage("java.util.jar");
        classScannerConfiguration.setExcludePackages(new HashSet<>());
        classScannerConfiguration.setIncludePackages(new HashSet<>());
        classScannerConfiguration.setScanSubPackages(true);
        classScannerConfiguration.setScanInnerType(false);

        List<CompilationUnit> compilationUnits = new PlantUmlCodeGenerator(classScannerConfiguration).generate();

        new PrimitivePlantUmlCodeFileWriter("uml.pu", compilationUnits).write();
        new UmlDiagramWriter("uml",UmlDiagramWriter.FILE_TYPE_PNG,compilationUnits).write();
        new UmlDiagramWriter("uml",UmlDiagramWriter.FILE_TYPE_SVG,compilationUnits).write();
    }
}
