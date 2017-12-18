package com.icekredit.uml;

import com.icekredit.entities.*;
import com.icekredit.utils.ConfigUtil;
import com.icekredit.utils.ContactUtil;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.salt.SaltUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by icekredit on 12/23/16.
 */
public class UmlCodeGenerator {
    public static final String FILE_TYPE_SVG = "svg";
    public static final String FILE_TYPE_PNG = "png";
    /**
     * 并渲染uml类图
     *
     * @param outputDir uml类图图片文件输出目录
     * @param outputFileName uml类图图片文件名称
     * @param fileType uml类图图片文件类型 UmlCodeGenerator.FILE_TYPE_SVG  矢量图 UmlCodeGenerator.FILE_TYPE_PNG Png图片
     */
    public void generate(String outputDir,String outputFileName,String fileType) throws Exception {
        generate(null,outputDir,outputFileName,fileType);
    }

    /**
     * 并渲染uml类图
     *
     * @param extraPath 额外路径
     * @param outputDir uml类图图片文件输出目录
     * @param outputFileName uml类图图片文件名称
     * @param fileType uml类图图片文件类型 UmlCodeGenerator.FILE_TYPE_SVG  矢量图 UmlCodeGenerator.FILE_TYPE_PNG Png图片
     */
    public void generate(String extraPath,String outputDir,String outputFileName,String fileType) throws Exception {
        if(fileType == null || (!fileType.trim().equals(FILE_TYPE_PNG) && !fileType.trim().equals(FILE_TYPE_SVG))){
            throw new Exception("Unknown output file type!");
        }

        File outputFile = new File(outputDir + (outputDir.endsWith(File.separator) ? "" : File.separator) + outputFileName + "." + fileType);
        outputFile.getParentFile().mkdirs();

        String entireFileStr = generatePrimitivePlantUmlCodeHelper(extraPath);

        SourceStringReader sourceStringReader = new SourceStringReader(entireFileStr);
        if(fileType.equals(FILE_TYPE_PNG)){
            sourceStringReader.generateImage(outputFile);
        } else {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            sourceStringReader.generateImage(os, new FileFormatOption(FileFormat.SVG));
            os.close();
            final String svgFileString = new String(os.toByteArray(), Charset.forName("UTF-8"));

            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.write(svgFileString.getBytes());
            fileOutputStream.close();
        }
    }

    /**
     * 用于将uml源代码直接输到指定文件
     *
     * @param outputDir 输出文件目录
     * @param outputFileName 输出文件名称
     * @throws Exception
     */
    public void generatePrimitivePlantUmlCode(String outputDir,String outputFileName) throws Exception{
        generatePrimitivePlantUmlCode(null,outputDir,outputFileName);
    }

    /**
     * 用于将uml源代码直接输到指定文件
     *
     * @param extraPath 额外路径
     * @param outputDir 输出文件目录
     * @param outputFileName 输出文件名称
     * @throws Exception
     */
    public void generatePrimitivePlantUmlCode(String extraPath,String outputDir,String outputFileName) throws Exception{
        File outputFile = new File(outputDir + (outputDir.endsWith(File.separator) ? "" : File.separator) + outputFileName + ".pu");
        outputFile.getParentFile().mkdirs();

        String entireFileStr = generatePrimitivePlantUmlCodeHelper(extraPath);

        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

        fileOutputStream.write(entireFileStr.getBytes());

        fileOutputStream.close();
    }

    /**
     * 用于扫描整个项目com包下面的所有类文件并输出plantUml类图代码
     * @return 类图代码字符串
     */
    private String generatePrimitivePlantUmlCodeHelper(String extraPath) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<ClassIdentifier> classIdentifiers = new ArrayList<ClassIdentifier>();

        List<String> fullClassNames = getAllClass(extraPath);

        Class cls;
        String className;
        String classType;
        List<AttributeIdentifier> attributeIdentifiers;
        List<FunctionIdentifier> functionIdentifiers;
        List<RelationIdentifier> relationIdentifiers;
        ClassIdentifier classIdentifier;
        String extendClass;
        List<String> implementInterfaces;
        for (String fullClassName : fullClassNames) {
            try {
                //加载指定类
                cls = UmlCodeGenerator.class.getClassLoader().loadClass(fullClassName);

                classType = getClassType(cls);

                //读取类名称
                className = getClassName(cls);

                //读取类的全部属性
                attributeIdentifiers = getAllAttributes(cls);

                //读取类的所有函数
                functionIdentifiers = getAllFunctions(cls);

                //读取类的所有联系
                relationIdentifiers = getAllRelations(cls);

                //读取所有继承关系
                extendClass = getExtendClass(cls);

                //读取所有实现关系
                implementInterfaces = getImplementInterfaces(cls);

                classIdentifier = new ClassIdentifier(classType,className,attributeIdentifiers,functionIdentifiers,relationIdentifiers,extendClass,implementInterfaces);

                classIdentifiers.add(classIdentifier);
            }catch (NoClassDefFoundError e){
                e.printStackTrace();
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }

        return String.format("%s%s%s","@startuml\n", ContactUtil.join(classIdentifiers,"\n"),"@enduml");
    }

    private static String getClassType(Class cls) {
        if((cls.getModifiers() & Modifier.INTERFACE) == Modifier.INTERFACE){
            return ClassIdentifier.CLASS_TYPE_INTERFACE;
        }

        return ClassIdentifier.CLASS_TYPE_CLASS;
    }

    public static List<String> getImplementInterfaces(Class cls) {
        Class [] interfaces = cls.getInterfaces();

        List<String> interfaceNames = new ArrayList<>();

        for(Class implInterface:interfaces){
            if(implInterface.getName().startsWith(ConfigUtil.getRootPath())){
                interfaceNames.add(implInterface.getSimpleName());
            }
        }

        return interfaceNames;
    }

    public static String getExtendClass(Class cls) {
        if(cls.getSuperclass() == null){
            return null;
        }

        if(cls.getSuperclass().getName().startsWith(ConfigUtil.getRootPath())){
            return cls.getSuperclass().getSimpleName();
        }

        return null;
    }

    public static List<RelationIdentifier> getAllRelations(Class cls) throws IllegalAccessException, InstantiationException {
        Field [] fields = cls.getDeclaredFields();

        List<RelationIdentifier> relationIdentifiers = new ArrayList<RelationIdentifier>();

        RelationIdentifier relationIdentifier;
        for(Field field:fields){
            /*if(((field.getModifiers() & Identifier.MODIFIER_TYPE_STATIC) == Identifier.MODIFIER_TYPE_STATIC) ||
                    ((field.getModifiers() & Identifier.MODIFIER_TYPE_FINAL) == Identifier.MODIFIER_TYPE_FINAL *//*||
                            ((field.getModifiers() & Identifier.MODIFIER_TYPE_PRIVATE) == Identifier.MODIFIER_TYPE_PRIVATE)*//*)){
                continue;
            }*/

            if(field.getType().isAssignableFrom(List.class) || field.getType().isAssignableFrom(Set.class)){
                Type genericType = field.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型

                if(genericType == null || !genericType.getTypeName().startsWith(ConfigUtil.getRootPath())) continue;

                if(genericType instanceof ParameterizedType){ // 【3】如果是泛型参数的类型
                    handleParameterizedType(cls, relationIdentifiers, (ParameterizedType) genericType);

                    continue;
                }

                continue;
            }

            if(field.getType().getName().startsWith(ConfigUtil.getRootPath())){
                relationIdentifier = new RelationIdentifier();

                relationIdentifier.setRelationType(RelationIdentifier.RELATION_TYPE_ASSOCIATION);

                relationIdentifier.setCurrentClassName(field.getType().getSimpleName());

                relationIdentifier.setAnotherClassName(cls.getSimpleName());

                relationIdentifiers.add(relationIdentifier);
            }
        }


        /*if(cls.getSuperclass() == null){
            return null;
        }

        if(cls.getSuperclass().getName().startsWith(ConfigUtil.ROOT_PACKAGE)){
            relationIdentifier = new RelationIdentifier();

            relationIdentifier.setRelationType(RelationIdentifier.RELATION_TYPE_GENERALIZATION);

            relationIdentifier.setCurrentClassName(cls.getSimpleName());

            relationIdentifier.setAnotherClassName(cls.getSuperclass().getSimpleName());

            relationIdentifiers.add(relationIdentifier);
        }

        Class [] interfaces = cls.getInterfaces();

        for(Class implInterface:interfaces){
            if(implInterface.getName().startsWith(ConfigUtil.ROOT_PACKAGE)){
                relationIdentifier = new RelationIdentifier();

                relationIdentifier.setRelationType(RelationIdentifier.RELATION_TYPE_REALIZATION);

                relationIdentifier.setCurrentClassName(cls.getSimpleName());

                relationIdentifier.setAnotherClassName(implInterface.getSimpleName());

                relationIdentifiers.add(relationIdentifier);
            }
        }*/

        return relationIdentifiers;
    }

    /**
     * 如果一个类型是一个ParameterizedType。那么它的ActualTypeArguments可能是ParameterizedType（Collection类型），也可能是class（普通类型）
     * @param cls
     * @param relationIdentifiers
     * @param parameterizedType
     */
    private static void handleParameterizedType(Class cls, List<RelationIdentifier> relationIdentifiers, ParameterizedType parameterizedType) {
        RelationIdentifier relationIdentifier;

        for(Object object:parameterizedType.getActualTypeArguments()){
            if(object instanceof ParameterizedType){
                handleParameterizedType(cls,relationIdentifiers, (ParameterizedType) object);
            } else if(object instanceof Class){
                Class genericClazz = (Class)object;

                if(!genericClazz.getName().startsWith(ConfigUtil.getRootPath())){
                    return;
                }

                relationIdentifier = new RelationIdentifier();

                relationIdentifier.setRelationType(RelationIdentifier.RELATION_TYPE_COMPOSITION);

                relationIdentifier.setCurrentClassName(genericClazz.getSimpleName());

                relationIdentifier.setAnotherClassName(cls.getSimpleName());

                relationIdentifiers.add(relationIdentifier);
            }
        }
    }

    public static List<FunctionIdentifier> getAllFunctions(Class cls) {
        Method[] methods = cls.getDeclaredMethods();

        List<FunctionIdentifier> functionIdentifiers = new ArrayList<FunctionIdentifier>();

        for(Method method:methods){
            if(method.getName().startsWith("lambda")){
                continue;
            }

            /*if(((method.getModifiers() & Identifier.MODIFIER_TYPE_STATIC) == Identifier.MODIFIER_TYPE_STATIC) ||
                    ((method.getModifiers() & Identifier.MODIFIER_TYPE_FINAL) == Identifier.MODIFIER_TYPE_FINAL) ||
                    ((method.getModifiers() & Identifier.MODIFIER_TYPE_PRIVATE) == Identifier.MODIFIER_TYPE_PRIVATE)){
                continue;
            }*/

            FunctionIdentifier functionIdentifier = new FunctionIdentifier();

            if((method.getModifiers() & Identifier.MODIFIER_TYPE_PUBLIC) == Identifier.MODIFIER_TYPE_PUBLIC){
                functionIdentifier.setFunctionModifier(FunctionIdentifier.FUNCTION_MODIFIER_PUBLIC);
            } else if((method.getModifiers() & Identifier.MODIFIER_TYPE_PROTECTED) == Identifier.MODIFIER_TYPE_PROTECTED){
                functionIdentifier.setFunctionModifier(FunctionIdentifier.FUNCTION_MODIFIER_PROTECTED);
            } else if((method.getModifiers() & Identifier.MODIFIER_TYPE_PRIVATE) == Identifier.MODIFIER_TYPE_PRIVATE){
                functionIdentifier.setFunctionModifier(FunctionIdentifier.FUNCTION_MODIFIER_PRIVATE);
            } else {
                functionIdentifier.setFunctionModifier(FunctionIdentifier.FUNCTION_MODIFIER_PRIVATE);
            }

            functionIdentifier.setFunctionName(method.getName());

            List<ArgumentIdentifier> argumentIdentifiers = new ArrayList<ArgumentIdentifier>();
            ArgumentIdentifier argumentIdentifier;
            Parameter [] parameters = method.getParameters();
            for(Parameter parameter:parameters){
                argumentIdentifier = new ArgumentIdentifier();

                argumentIdentifier.setArgumentName((parameter.getType().getSimpleName().substring(0,1).toLowerCase() +
                        parameter.getType().getSimpleName().substring(1)).replace("[]","s"));
                argumentIdentifier.setArgumentType(parameter.getType().getSimpleName());

                argumentIdentifiers.add(argumentIdentifier);
            }

            functionIdentifier.setArgumentIdentifiers(argumentIdentifiers);

            functionIdentifiers.add(functionIdentifier);
        }

        return functionIdentifiers;
    }

    public static List<AttributeIdentifier> getAllAttributes(Class cls) {
        Field [] fields = cls.getDeclaredFields();

        List<AttributeIdentifier> attributeIdentifiers = new ArrayList<AttributeIdentifier>();

        for(Field field:fields){
            /*if(((field.getModifiers() & Identifier.MODIFIER_TYPE_STATIC) == Identifier.MODIFIER_TYPE_STATIC) ||
                    ((field.getModifiers() & Identifier.MODIFIER_TYPE_FINAL) == Identifier.MODIFIER_TYPE_FINAL) *//*||
                    ((field.getModifiers() & Identifier.MODIFIER_TYPE_PRIVATE) == Identifier.MODIFIER_TYPE_PRIVATE)*//*){
                continue;
            }*/

            AttributeIdentifier attributeIdentifier = new AttributeIdentifier();

            if((field.getModifiers() & Identifier.MODIFIER_TYPE_PUBLIC) == Identifier.MODIFIER_TYPE_PUBLIC){
                attributeIdentifier.setAttributeModifier(AttributeIdentifier.ATTRIBUTE_MODIFIER_PUBLIC);
            } else if((field.getModifiers() & Identifier.MODIFIER_TYPE_PRIVATE) == Identifier.MODIFIER_TYPE_PRIVATE){
                attributeIdentifier.setAttributeModifier(AttributeIdentifier.ATTRIBUTE_MODIFIER_PRIVATE);
            } else if((field.getModifiers() & Identifier.MODIFIER_TYPE_PROTECTED) == Identifier.MODIFIER_TYPE_PROTECTED){
                attributeIdentifier.setAttributeModifier(AttributeIdentifier.ATTRIBUTE_MODIFIER_PROTECTED);
            } else {
                attributeIdentifier.setAttributeModifier(AttributeIdentifier.ATTRIBUTE_MODIFIER_PRIVATE);
            }

            attributeIdentifier.setAttributeType(field.getType().getSimpleName());

            attributeIdentifier.setAttributeName(field.getName());

            attributeIdentifiers.add(attributeIdentifier);
        }

        return attributeIdentifiers;
    }

    public static String getClassName(Class cls) {
        return cls.getSimpleName();
    }

    public static List<String> getAllClass(String extraPath){
        Map<String,Object> configurationMap = ConfigUtil.loadConfiguration();
        List<String> excludeList = (List<String>) ((Map)configurationMap.get("path_to_scan")).get("exclude_path");

        File rootDir = new File(assembleScanDir(extraPath));

        List<String> codeFileNames = new ArrayList<>();

        assembleAndCollectAllClassHelper(rootDir, "", codeFileNames,excludeList);

        return codeFileNames;
    }

    private static String assembleScanDir(String extraPath) {
        StringBuilder extraPathBuilder = new StringBuilder(System.getProperty("user.dir"));

        if(!StringUtils.isEmpty(extraPath)){
            extraPath = extraPath.trim();

            if(!extraPath.startsWith(File.separator)){
                extraPathBuilder.append(File.separator);
            }

            if(!extraPath.endsWith(File.separator)){
                extraPathBuilder.append(extraPath.substring(0,extraPath.length()));
            } else {
                extraPathBuilder.append(extraPath);
            }
        }

        extraPathBuilder.append("/src/main/java");

        return extraPathBuilder.toString();
    }

    protected static void assembleAndCollectAllClassHelper(File currentFile, String parentPath, List<String> codeFileNames, List<String> excludeList) {
        if (!currentFile.exists() || !currentFile.isDirectory()) {
            return;
        }

        File[] subFiles = currentFile.listFiles();

        for (File subFile : subFiles) {
            if (subFile.isFile()) {
                if(!subFile.getName().endsWith(".java") && !subFile.getName().endsWith(".class")){
                    continue;
                }

                if(isExistsInExcludeList(subFile.getAbsolutePath(),excludeList)){
                    continue;
                }

                codeFileNames.add((parentPath + File.separator + subFile.getName().substring(0, subFile.getName().indexOf("."))).replace(File.separator, "."));
            } else if (subFile.isDirectory()) {
                if(isExistsInExcludeList(subFile.getAbsolutePath(),excludeList)){
                    continue;
                }

                assembleAndCollectAllClassHelper(
                        subFile,
                        (parentPath == null || parentPath.trim().length() == 0 ? "" : (parentPath + File.separator)) + subFile.getName(),
                        codeFileNames, excludeList);
            }
        }
    }

    private static boolean isExistsInExcludeList(String absolutePath, List<String> excludeList) {
        if(excludeList == null || excludeList.size() == 0){
            return false;
        }

        for(String exclude:excludeList){
            if(absolutePath.contains(exclude)){
                return true;
            }
        }

        return false;
    }
}
