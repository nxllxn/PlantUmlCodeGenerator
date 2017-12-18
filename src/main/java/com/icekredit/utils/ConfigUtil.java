package com.icekredit.utils;

import org.apache.commons.collections.map.HashedMap;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *  给予yaml文件的配置工具类
 */
public class ConfigUtil {
    /**
     * 默认内部配置文件
     */
    protected static final String DEFAULT_INNER_CONFIG_FILE = "application.yml";

    /**
     * 加载配置文件，先加载内部默认配置文件，然后再用外部配置文件覆盖
     *
     *
     * @return 配置属性
     */
    public static Map<String,Object> loadConfiguration(){
        Map<String,Object> innerConfigMap = new HashMap<String,Object>();

        try {
            Map<String,Object> outerConfigMap;

            innerConfigMap.putAll(new Yaml().loadAs(ConfigUtil.class.getClassLoader().getResourceAsStream(DEFAULT_INNER_CONFIG_FILE), HashMap.class));

            File outerConfigDir = new File(PathUtil.getCurrentRunningPath());
            if(outerConfigDir.exists() && outerConfigDir.isDirectory()){
                File [] outerConfigFiles = outerConfigDir.listFiles();

                for(File outerConfigFile:outerConfigFiles){

                    if(outerConfigFile.isFile() && outerConfigFile.getAbsolutePath().endsWith(".yml")){
                        outerConfigMap = new Yaml().loadAs(new FileInputStream(outerConfigFile), HashedMap.class);

                        innerConfigMap.putAll(outerConfigMap);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return innerConfigMap;
    }

    public static String getRootPath() {
        return (String) ((Map)loadConfiguration().get("path_to_scan")).get("root");
    }
}
