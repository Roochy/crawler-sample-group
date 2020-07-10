package org.origin.util;

import com.alibaba.fastjson.JSON;
import org.origin.cache.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author roochy
 * @package org.origin.util
 * @since 2019/10/16
 */
public class ConfigLoader {
    private static Logger log = LoggerFactory.getLogger(ConfigLoader.class);
    private static ConfigLoader instance = new ConfigLoader();

    protected ConfigLoader() {}

    public static ConfigLoader instance() {
        return instance;
    }

    private String readFile(String path) {
        InputStream in = null;
        try {
            in = ConfigLoader.class.getResourceAsStream(path);
        } catch (Exception e) {
            log.error("Initiating input stream failed. {}", e.getMessage());
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        byte[] data = new byte[255];
        int cursor;
        try {
            while ((cursor = in.read(data)) != -1) {
                buffer.append(new String(data, 0, cursor));
            }
        } catch (IOException e) {
            log.error("Reading file failed. {}", e.getMessage());
        }
        try {
            in.close();
        } catch (IOException e) {
            log.error("Closing input stream failed. {}", e.getMessage());
        }
        return buffer.toString();
    }

    /**
     * 解析 JSON 类型的配置文件
     * @param path 配置文件路径，以 resource 文件夹为根目录
     * @param configClass 配置类的 class 对象
     * @param <T> 配置泛型类
     * @return
     */
    public<T> T loadJsonConfig(String path, Class<T> configClass) {
        String configStr = readFile(path);
        if (StringUtils.isEmpty(configStr)) {
            return null;
        }
        return JSON.parseObject(configStr, configClass);
    }

    /**
     * 解析 property 类型的配置文件
     * @param path
     * @return
     */
    public Properties loadProperties(String path) {
        Properties prop;
        try {
            InputStream in = Loader.class.getResourceAsStream(path);
            prop = new Properties();
            prop.load(in);
            in.close();
        } catch (Exception e) {
            log.error("Loading properties error. {}", e);
            return null;
        }
        return prop;
    }
}
