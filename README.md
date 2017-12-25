# PlantUmlCodeGenerator

本工具用于扫描指定项目下面的所有类文件生成PlantUml源代码或者渲染出相应的类图图片文件

## 使用第三方依赖：
    1.plantuml
        1）maven依赖
        <!-- https://mvnrepository.com/artifact/net.sourceforge.plantuml/plantuml -->
        <dependency>
            <groupId>net.sourceforge.plantuml</groupId>
            <artifactId>plantuml</artifactId>
            <version>8053</version>
        </dependency>
        2）Java Api调用方式
        参见http://plantuml.com/api
    2.graphviz
        linux：apt-get install graphviz
        windows：http://www.graphviz.org/pub/graphviz/stable/windows/graphviz-2.38.msi

## 配置文件：
    application.yml
    
    path_to_scan:
        exclude_path:   #默认扫描com起始的整个文件树，此配置用于配置不需要扫描的路径
            - "some dir excluded"

## 接口调用：

    1）generatePrimitivePlantUmlCode
    /**
     * 用于扫描整个项目com包下面的所有类文件并输出plantUml类图代码
     * @return 类图代码字符串
     */
    public String generatePrimitivePlantUmlCode()
    
    2）generate
    /**
     * 并渲染uml类图
     *
     * @param outputDir uml类图图片文件输出目录
     * @param outputFileName uml类图图片文件名称
     * @param fileType uml类图图片文件类型 UmlCodeGenerator.FILE_TYPE_SVG  矢量图 UmlCodeGenerator.FILE_TYPE_PNG Png图片
     */
    public void generate(String outputDir,String outputFileName,String fileType)
    
## SomeThing Maybe Useful
    1.PlantUML类图
        https://yq.aliyun.com/articles/25405
    2.PlantUml with Atom
        http://blog.csdn.net/fwj380891124/article/details/51781804
    3.学习UML实现、泛化、依赖、关联、聚合、组合 
        http://blog.chinaunix.net/uid-26111972-id-3326225.html
    
