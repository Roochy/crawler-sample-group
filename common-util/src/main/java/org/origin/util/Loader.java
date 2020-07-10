package org.origin.util;

import org.origin.cache.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author roochy
 * @package org.origin.crawler.config
 * @since 2017/12/21
 */
public class Loader {
    private static Logger log = LoggerFactory.getLogger(Loader.class);
    private Properties prop;
    private static Loader instance = new Loader();

    protected Loader() {
        try {
            InputStream in = Loader.class.getResourceAsStream("/app.properties");
            prop = new Properties();
            prop.load(in);
            in.close();
        } catch (Exception e) {
            log.error("load properties error. {}", e);
        }
    }

    public static Loader instance() {
        return instance;
    }

    public String props(String key) {
        return prop.getProperty(key);
    }

    public RedisCache redis() {
        try {
            return new RedisCache(props("redis.host"), Integer.parseInt(props("redis.port")));
        } catch (Exception e) {
            log.info("connect redis error, try to connect local redis server.");
            return new RedisCache("127.0.0.1", 6379);
        }
    }
}
