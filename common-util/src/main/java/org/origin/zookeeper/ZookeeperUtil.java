package org.origin.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author roochy
 * @package org.origin.zookeeper
 * @since 2018/1/10
 */
public class ZookeeperUtil extends BaseZookeeper {
    private static final Logger log = LoggerFactory.getLogger(ZookeeperUtil.class);

    public ZookeeperUtil(String servers) {
        super(servers);
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connected(WatchedEvent event) {
        log.info("zookeeper connected.");
    }

    @Override
    public void nodeChildrenChanged(WatchedEvent event) {
        log.info("zookeeper children node changed : {}", event.getPath());
    }

    @Override
    public void nodeDataChanged(WatchedEvent event) {
        log.info("zookeeper node data changed : {}", event.getPath());
    }
}
