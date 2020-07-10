package org.origin.zookeeper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author roochy
 * @package org.origin.zookeeper
 * @since 2018/1/10
 */
public class ZookeeperFactory {
    private String location;
    private Properties properties;
    private final String LOCAL_SERVER = "127.0.0.1:2181";

    public ZookeeperFactory(String location) {
        this.location = location;
        loadConfig();
    }

    private void loadConfig() {
        InputStream in = ZookeeperFactory.class.getResourceAsStream(location);
        properties = new Properties();
        try {
            properties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ZookeeperUtil newZookeeperUtil() {
        String servers = properties.getProperty("zk.server");
        if (servers == null) {
            throw new RuntimeException("Can not read property \"zk.server\" from location " + location);
        }
        if (servers.startsWith("${")) {
            servers = LOCAL_SERVER;
        }
        return new ZookeeperUtil(servers);
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
