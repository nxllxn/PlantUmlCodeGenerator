package com.nxllxn.plantuml.codegen;

import com.nxllxn.plantuml.config.ClassScannerConfiguration;
import com.nxllxn.plantuml.java.*;
import com.nxllxn.plantuml.scanner.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * PlantUml泪类图源码生成工具
 *
 * @author wenchao
 */
public class PlantUmlCodeGenerator {
    private ClassScanner classScanner;

    private ClassScannerConfiguration classScannerConfiguration;

    private Logger logger = LoggerFactory.getLogger(PlantUmlCodeGenerator.class);

    public PlantUmlCodeGenerator(ClassScannerConfiguration classScannerConfiguration) {
        this.classScannerConfiguration = classScannerConfiguration;

        logger.info("The root packages to scan:{}",this.classScannerConfiguration.getRootPackage());
        logger.info("The included packages specified manually:{}",this.classScannerConfiguration.getIncludePackages());
        logger.info("The excluded packages specified manually:{}",this.classScannerConfiguration.getExcludePackages());
        logger.info("Will {}scan the sub packages",this.classScannerConfiguration.isScanSubPackages() ? "" : "not ");
        logger.info("Will {}scan the inner type",this.classScannerConfiguration.isScanInnerType() ? "" : "not ");

        classScanner = ClassScanner.builder()
                .withRootPackage(classScannerConfiguration.getRootPackage())
                .withExcludePackages(classScannerConfiguration.getExcludePackages())
                .withIncludePackages(classScannerConfiguration.getIncludePackages())
                .withScanSubPackages(classScannerConfiguration.isScanSubPackages())
                .withScanInnerType(classScannerConfiguration.isScanInnerType())
                .build();
    }

    public List<CompilationUnit> generate() {
        List<Class<?>> classes = classScanner.scan();

        logger.info("Class count scanned:{}",classes.size());

        List<CompilationUnit> compilationUnits = new ArrayList<>();
        for (Class cls : classes) {
            if (cls.isInterface()) {
                TopLevelInterface topLevelInterface = new TopLevelInterface();

                topLevelInterface.setType(new FullyQualifiedJavaType(cls.getName()));

                topLevelInterface.addSuperInterfaces(getAllSuperInterfaces(cls));

                topLevelInterface.addFields(getAllDeclaredFields(cls));

                topLevelInterface.addMethods(getAllDeclaredMethods(cls));

                compilationUnits.add(topLevelInterface);
            } else if (cls.isEnum()) {
                TopLevelEnumeration topLevelEnumeration = new TopLevelEnumeration();

                topLevelEnumeration.setType(new FullyQualifiedJavaType(cls.getName()));

                topLevelEnumeration.addFields(getAllDeclaredFields(cls));

                topLevelEnumeration.addMethods(getAllDeclaredMethods(cls));

                topLevelEnumeration.addEnumConstants(getAllEnumConstants(cls));

                compilationUnits.add(topLevelEnumeration);
            } else if (cls.isAnnotation()) {
                TopLevelAnnotation topLevelAnnotation = new TopLevelAnnotation();

                topLevelAnnotation.setType(new FullyQualifiedJavaType(cls.getName()));

                topLevelAnnotation.addMethods(getAllDeclaredMethods(cls));

                compilationUnits.add(topLevelAnnotation);
            } else {
                TopLevelClass topLevelClass = new TopLevelClass();

                topLevelClass.setAbstract(Modifier.isAbstract(cls.getModifiers()));

                topLevelClass.setType(new FullyQualifiedJavaType(cls.getName()));

                if (cls.getSuperclass() != null && !cls.getSuperclass().equals(Object.class)){
                    topLevelClass.setSuperClass(new FullyQualifiedJavaType(cls.getSuperclass().getName()));
                }

                topLevelClass.addSuperInterfaces(getAllSuperInterfaces(cls));

                topLevelClass.addFields(getAllDeclaredFields(cls));

                topLevelClass.addMethods(getAllDeclaredMethods(cls));

                compilationUnits.add(topLevelClass);
            }
        }

        return compilationUnits;
    }

    private List<EnumConstant> getAllEnumConstants(Class cls) {
        List<EnumConstant> declaredEnumConstants = new ArrayList<>();
        EnumConstant declaredEnumConstant;
        for (Object enumConstant:cls.getEnumConstants()){
            declaredEnumConstant = new EnumConstant();
            declaredEnumConstant.setEnumConstantName(enumConstant.toString());
            declaredEnumConstants.add(declaredEnumConstant);
        }
        return declaredEnumConstants;
    }

    private List<FullyQualifiedJavaType> getAllSuperInterfaces(Class cls) {
        List<FullyQualifiedJavaType> superInterfaces = new ArrayList<>();
        for (Class superInterface:cls.getInterfaces()){
            superInterfaces.add(new FullyQualifiedJavaType(superInterface.getName()));
        }
        return superInterfaces;
    }

    private List<Method> getAllDeclaredMethods(Class cls) {
        List<Method> declaredMethods = new ArrayList<>();
        Method declaredMethod;
        for (java.lang.reflect.Method method:cls.getDeclaredMethods()){
            declaredMethod = new Method();
            declaredMethod.setVisibility(Visibility.from(method.getModifiers()));
            declaredMethod.setStatic(Modifier.isStatic(method.getModifiers()));
            declaredMethod.setAbstract(Modifier.isAbstract(method.getModifiers()));
            declaredMethod.setReturnType(new FullyQualifiedJavaType(method.getReturnType().getName()));
            declaredMethod.setName(method.getName());

            Parameter declaredParameter;
            for (java.lang.reflect.Parameter parameter:method.getParameters()){
                declaredParameter = new Parameter();
                declaredParameter.setType(new FullyQualifiedJavaType(parameter.getType().getName()));
                declaredParameter.setName(parameter.getName());
                declaredParameter.setVarAs(parameter.isVarArgs());

                declaredMethod.addParameter(declaredParameter);
            }

            declaredMethods.add(declaredMethod);
        }
        return declaredMethods;
    }

    private List<Field> getAllDeclaredFields(Class cls) {
        List<Field> declaredFields = new ArrayList<>();
        Field declaredField;
        for (java.lang.reflect.Field field:cls.getDeclaredFields()){
            declaredField = new Field();
            declaredField.setVisibility(Visibility.from(field.getModifiers()));
            declaredField.setName(field.getName());
            declaredField.setType(new FullyQualifiedJavaType(field.getType().getName()));
            declaredField.setStatic(Modifier.isStatic(field.getModifiers()));

            declaredFields.add(declaredField);
        }
        return declaredFields;
    }
}
