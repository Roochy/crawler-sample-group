package org.origin.zookeeper;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * @author roochy
 * @package org.origin.crawler.service.config
 * @since 2018/1/10
 */
public class ZookeeperPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    private ZookeeperUtil zookeeperUtil;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, new ZookeeperProperties(props, zookeeperUtil));
    }

    public void setZookeeperUtil(ZookeeperUtil zookeeperUtil) {
        this.zookeeperUtil = zookeeperUtil;
    }
}
