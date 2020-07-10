package org.origin.zookeeper;

import java.util.Properties;

/**
 * @author roochy
 * @package org.origin.crawler.article.config
 * @since 2018/1/14
 */
public class ZookeeperProperties extends Properties {
    private ZookeeperUtil zk;
    private Properties props;

    public ZookeeperProperties() {
        this.props = new Properties();
    }

    public ZookeeperProperties(Properties props, ZookeeperUtil zk) {
        this.props = props;
        this.zk = zk;
    }

    @Override
    public String getProperty(String key) {
        String val = props.getProperty(key);
        return val != null ? val : getPropertyFromZk(key);
    }

    @Override
    public synchronized Object setProperty(String key, String value) {
        return props.setProperty(key, value);
    }


    private String getPropertyFromZk(String key) {
        if (zk == null) {
            return null;
        } else {
            try {
                return zk.getData(key, false);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
