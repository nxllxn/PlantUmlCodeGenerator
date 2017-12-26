package com.nxllxn.plantuml.scanner;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * 类文件扫描器，用于扫描指定包下面满足指定条件的全部类
 *
 * @author wenchao
 */
public final class ClassScanner {
    /**
     * 需要显式包含的包
     */
    private final Set<String> includePackages;

    /**
     * 需要显式排除的包
     */
    private final Set<String> excludePackages;

    /**
     * 是否递归扫描子包
     */
    private final boolean isScanSubPackages;

    /**
     * 是否扫描内部内，接口，枚举
     */
    private final boolean isScanInnerType;

    /**
     * Java home
     */
    private static final String JAVA_HOME = System.getProperty("java.home");

    /**
     * rt jar包路径
     */
    private static final String RT_JAR_PATH = JAVA_HOME + File.separatorChar + "lib" + File.separatorChar + "rt.jar";

    /**
     * jar包文件名称后缀
     */
    private static final String JAR_FILE_NAME_SUFFIX = ".jar";

    /**
     * 类文件名称后缀
     */
    private static final String CLASS_FILE_NAME_SUFFIX = ".class";

    /**
     * SLF4J + LOG4J
     */
    private Logger logger = LoggerFactory.getLogger(ClassScanner.class);

    /**
     * 构造方法，由于此处使用builder模式，所以不提供共有的构造方法
     * @param includePackages 待包含的包
     * @param excludePackages 待排除的包
     * @param isScanSubPackages 是否需要扫描子包
     * @param isScanInnerType 是否需要扫描内部类
     */
    private ClassScanner(Set<String> includePackages, Set<String> excludePackages,
                         boolean isScanSubPackages, boolean isScanInnerType) {
        this.includePackages = includePackages;
        this.excludePackages = excludePackages;
        this.isScanSubPackages = isScanSubPackages;
        this.isScanInnerType = isScanInnerType;
    }

    /**
     * 扫描指定包下的全部类
     * @return 类集合
     */
    public List<Class<?>> scan() {
        List<Class<?>> classes = new ArrayList<>();

        URL[] urls = ((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs();

        String urlPath;
        for (URL url : urls) {
            urlPath = url.getPath();

            if (StringUtils.isBlank(urlPath)) {
                continue;
            }

            //取出掉JavaHome下除rt.jar自带类库
            if (urlPath.contains(JAVA_HOME)
                    && !urlPath.equals(RT_JAR_PATH)) {
                continue;
            }

            //如果指定URL表示一个jar文件
            if (isJarFile(urlPath)) {
                String filePath = getFilePathFromUrl(url);

                if (StringUtils.isBlank(filePath)) {
                    continue;
                }

                scanJarFile(filePath, classes);

                continue;
            } else {
                String filePath = getFilePathFromUrl(url);

                if (StringUtils.isBlank(filePath)) {
                    continue;
                }

                scanClassesDir(filePath, classes);
            }
        }

        return classes;
    }

    /**
     * 判断指定路径对应文件是否是一个Jar文件
     * @param urlPath 指定路径
     * @return 路径不为空且以.jar后缀结尾返回true，否则返回false
     */
    private boolean isJarFile(String urlPath) {
        return StringUtils.isNotBlank(urlPath) && urlPath.endsWith(JAR_FILE_NAME_SUFFIX);
    }

    /**
     * 扫描指定class文件目录
     * @param filePath 指定目录
     * @param classes class文件集合
     */
    private void scanClassesDir(String filePath, List<Class<?>> classes) {
        File classesDir;

        try {
            classesDir = new File(filePath);

            if (!classesDir.exists() || !classesDir.isDirectory()) {
                logger.error("Sorry!The target dir is not exists!");

                return;
            }
        } catch (Exception e) {
            logger.error("Failed to open class dir:{}", filePath);

            return;
        }

        File[] classFiles = classesDir.listFiles(file -> file.isDirectory() || isClassFile(file));
        if (classFiles == null) {
            return;
        }

        for (File classFile : classFiles) {
            scanClassesDirHelper(null, classFile, classes);
        }
    }

    /**
     * 判断指定文件是否是一个类文件
     * @param file 指定文件
     * @return 如果文件不为null，且文件后缀名为.class，返回true
     */
    private boolean isClassFile(File file) {
        return file != null && file.getPath().endsWith(CLASS_FILE_NAME_SUFFIX);
    }

    /**
     * 判断指定路径是否是一个类文件
     * @param filePath 指定文件路径
     * @return 如果文件路径不为空，且文件后缀名为.class，返回true
     */
    private boolean isClassFile(String filePath) {
        return StringUtils.isNotBlank(filePath) && filePath.endsWith(CLASS_FILE_NAME_SUFFIX);
    }

    /**
     * 辅助方法，用于递归的扫描指定目录下的全部类文件
     * @param packageName 包名，从根目录开始时为null，或者“”
     * @param classFile 类文件
     * @param classes 类集合
     */
    private void scanClassesDirHelper(String packageName, File classFile, List<Class<?>> classes) {
        if (classFile.isDirectory()) {
            File[] classFiles = classFile.listFiles(file -> file.isDirectory() || isClassFile(file));

            if (classFiles == null) {
                return;
            }

            packageName = StringUtils.isBlank(packageName) ? classFile.getName() : (packageName + '.' + classFile.getName());
            for (File subFile : classFiles) {
                scanClassesDirHelper(packageName, subFile, classes);
            }
        } else if (classFile.isFile() && isClassFile(classFile)) {
            //6 == ".class".length()
            String className = packageName + '.' + classFile.getName().substring(0, classFile.getName().length() - 6);

            if (!isNeedToScan(packageName, includePackages, excludePackages)) {
                return;
            }

            try {
                Class<?> cls = Class.forName(className);

                //如果不需要扫描内部类，而当前类是一个内部类，那么直接返回
                if (!isScanInnerType && ClassUtils.isInnerClass(cls)) {
                    return;
                }

                classes.add(cls);
            } catch (ClassNotFoundException e) {
                logger.error("Unable to find class with class name:{}", className);
            }
        }
    }

    /**
     * 扫描指定Jar文件中全部类
     * @param filePath 指定jar文件路径
     * @param classes 类集合
     */
    private void scanJarFile(String filePath, List<Class<?>> classes) {
        JarFile jarFile;

        try {
            jarFile = new JarFile(filePath);
        } catch (IOException e) {
            logger.error("Failed to load the jar file with file path:{}", filePath);

            return;
        }

        Enumeration<JarEntry> entries = jarFile.entries();

        JarEntry currentEntry;
        String packageNameForCurrentEntry;
        String classNameForCurrentEntry = null;
        while (entries.hasMoreElements()) {
            currentEntry = entries.nextElement();

            String entryName = currentEntry.getName();
            int indexOfPathSeparator = entryName.lastIndexOf('/');
            if (indexOfPathSeparator == -1) {
                return;
            }

            packageNameForCurrentEntry = entryName.substring(0, indexOfPathSeparator).replace('/', '.');
            if (!isNeedToScan(packageNameForCurrentEntry, includePackages, excludePackages)) {
                continue;
            }

            if (isClassFile(entryName) && !currentEntry.isDirectory()) {
                String className = entryName.substring(packageNameForCurrentEntry.length() + 1, entryName.length() - 6);

                try {
                    classNameForCurrentEntry = packageNameForCurrentEntry + '.' + className;

                    Class<?> cls = Class.forName(classNameForCurrentEntry);

                    //如果不需要扫描内部类，而当前类是一个内部类，那么跳过
                    if (!isScanInnerType && ClassUtils.isInnerClass(cls)) {
                        continue;
                    }

                    classes.add(cls);
                } catch (ClassNotFoundException e) {
                    logger.error("Unable to find class with class name:{}", classNameForCurrentEntry);
                }
            }
        }
    }

    /**
     * 判定指定包是否需要被扫描
     * @param currentPackageName 当前包名
     * @param includePackages 待包含的包集合
     * @param excludePackages 待排除的包集合
     * @return 指定包是否需要被扫描
     */
    private boolean isNeedToScan(String currentPackageName,
                                 Set<String> includePackages, Set<String> excludePackages) {
        if (StringUtils.isBlank(currentPackageName)) {
            return false;
        }

        //先匹配需要排除的包，如果当前包在待排除包集合中，那么再看当前包是否在待包含的包集合中
        if (excludePackages != null) {
            Stream<String> excludeStream = excludePackages.stream()
                    .filter(excludePackage -> StringUtils.startsWith(currentPackageName, excludePackage));

            if (excludeStream.findAny().isPresent()) {
                return false;
            }
        }

        if (includePackages != null) {
            Stream<String> includeStream = includePackages.stream()
                    .filter(includePackage -> isScanSubPackages && StringUtils.startsWith(currentPackageName, includePackage)
                            || !isScanSubPackages && StringUtils.equals(currentPackageName,includePackage));

            if (!includeStream.findAny().isPresent()){
                return false;
            }
        }

        return true;
    }

    /**
     * 从知道Url中读取对应的文件路径名称
     * @param url 指定url
     * @return 文件路径名称
     */
    private String getFilePathFromUrl(URL url) {
        String encoding = "UTF-8";

        try {
            return URLDecoder.decode(url.getFile(), encoding);
        } catch (UnsupportedEncodingException e) {
            logger.error("getFilePathFromUrl unsupported encoding:{}", encoding);
        }

        return null;
    }

    /**
     * builder for class scanner
     *
     * @return ScannerBuilder instance
     */
    public static ScannerBuilder builder() {
        return new ScannerBuilder();
    }

    /**
     * Builder for class scanner
     */
    @SuppressWarnings("unused")
    public static class ScannerBuilder {
        /**
         * 待扫描根包
         */
        private String rootPackage;

        /**
         * 需要显式包含的包
         */
        private Set<String> includePackages;

        /**
         * 需要显式排除的包
         */
        private Set<String> excludePackages;

        /**
         * 是否递归扫描子包
         */
        private boolean isScanSubPackages;

        /**
         * 是否扫描内部内，接口，枚举
         */
        private boolean isScanInnerType;

        private ScannerBuilder() {
        }

        public ScannerBuilder withRootPackage(String rootPackage) {
            this.rootPackage = rootPackage;

            return this;
        }

        public ScannerBuilder withIncludePackages(Set<String> includePackages) {
            this.includePackages = includePackages;

            return this;
        }

        public ScannerBuilder withExcludePackages(Set<String> excludePackages) {
            this.excludePackages = excludePackages;

            return this;
        }

        public ScannerBuilder withScanSubPackages(boolean scanSubPackages) {
            isScanSubPackages = scanSubPackages;

            return this;
        }

        public ScannerBuilder withScanInnerType(boolean scanInnerType) {
            isScanInnerType = scanInnerType;

            return this;
        }

        public ClassScanner build() {
            if (includePackages == null) {
                includePackages = new HashSet<>();
            }

            //此处为了实现更加方便，不对rootPackage进行单独的处理，而是把他当作一个includePackage
            if (StringUtils.isNotBlank(rootPackage)){
                includePackages.add(rootPackage);
            }

            if (CollectionUtils.isEmpty(includePackages)
                    && CollectionUtils.isEmpty(excludePackages)){
                throw new RuntimeException("Sorry！Too many packages need to scan!You would better specified a includePackages manually!");
            }

            return new ClassScanner(includePackages, excludePackages, isScanSubPackages, isScanInnerType);
        }
    }
}
