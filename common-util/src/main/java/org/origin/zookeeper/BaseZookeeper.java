package org.origin.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.origin.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author roochy
 * @package org.origin.crawler.service.util
 * @since 2018/1/10
 */
public abstract class BaseZookeeper {
    private static final Logger log = LoggerFactory.getLogger(BaseZookeeper.class);
    private ZooKeeper zk;
    private Stat stat = new Stat();
    private int retryTimes = 0;
    private static final int MAX_RETRY = 3;
    private static final int TIMEOUT = 30000;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private Serializer serializer;
    private String servers;

    public BaseZookeeper(String servers) {
        this.servers = servers;
    }

    /**
     * 创建zookeeper连接
     * @throws InterruptedException
     * @throws IOException
     */
    public void connect() throws InterruptedException, IOException {
        CountDownLatch latch = new CountDownLatch(1);
        zk = new ZooKeeper(servers, TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    if (watchedEvent.getType() == Event.EventType.None) {
                        log.info("zookeeper connected.");
                        retryTimes = 0;
                        latch.countDown();
                    } else if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                        log.info("zookeeper node changed : {}", watchedEvent.getPath());
                    }
                } else if (watchedEvent.getState() == Event.KeeperState.Disconnected) {
                    log.error("zookeeper disconnected.");
                } else if (watchedEvent.getState() == Event.KeeperState.Expired) {
                    log.warn("zookeeper expired, reconnecting..");
                    reconnect();
                }
            }
        });
        latch.await();
    }

    /**
     * 重新连接
     */
    public void reconnect(){
        if (retryTimes >= MAX_RETRY) {
            log.error("zookeeper reconnect failed.");
            return;
        }
        retryTimes++;
        try {
            this.connect();
        } catch (Exception e) {
           log.error(e.getMessage());
        }
    }

    /**
     * 创建连接成功事件
     * @param event
     */
    public abstract void connected(WatchedEvent event);

    /**
     * 子节点变化事件
     * @param event
     */
    public abstract void nodeChildrenChanged(WatchedEvent event);

    /**
     * 节点数据变化事件
     * @param event
     */
    public abstract void nodeDataChanged(WatchedEvent event);

    /**
     * 节点删除事件
     * @param event
     */
    public void nodeDeleted(WatchedEvent event) {
        log.info("zookeeper node deleted : {}", event.getPath());
    }

    /**
     * 获取节点数据
     * @param path
     * @param watch
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     */
    public String getData(String path, boolean watch) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        byte[] data = zk.getData(path, watch, stat);
        return data != null ? new String(data, "UTF-8") : null;
    }

    /**
     * 获取子节点 path 集合
     * @param path
     * @param watch
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public List<String> getChildren(String path, boolean watch) throws KeeperException, InterruptedException{
        List<String> subList = zk.getChildren(path, watch);
        return subList;
    }

    /**
     * 设置节点数据
     * @param path
     * @param data
     * @param version
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void setData(String path, byte[] data, Integer version) throws KeeperException, InterruptedException{
        zk.setData(path, data, version);
    }

    /**
     * 创建永久节点
     * @param path
     * @param data
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String createPersistentNode(String path, byte[] data) throws KeeperException, InterruptedException {
        if (zk.exists(path, null) == null){
            String createdPath = zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            return createdPath;
        }else{
            return path;
        }
    }

    /**
     * 创建永久有序节点
     * @param path
     * @param data
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String createPersistentSeqNode(String path, byte[] data) throws KeeperException, InterruptedException {
        if (zk.exists(path, null) == null){
            String createdPath = zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            return createdPath;
        }else{
            return path;
        }
    }

    /**
     * 创建临时节点
     * @param path
     * @param data
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String createEphemeralNode(String path, byte[] data) throws KeeperException, InterruptedException {
        if (zk.exists(path, null) == null){
            String createdPath = zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            return createdPath;
        }else{
            return path;
        }
    }

    /**
     * 创建临时有序节点
     * @param path
     * @param data
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String createEphemeralSseqNode(String path, byte[] data) throws KeeperException, InterruptedException {
        if (zk.exists(path, null) == null){
            String createdPath = zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            return createdPath;
        }else{
            return path;
        }
    }

    /**
     * 删除节点
     * @param path
     * @param version
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void delete(String path, Integer version) throws InterruptedException, KeeperException{
        zk.delete(path, version);
    }

    /**
     * 判断节点是否存在
     * @param path
     * @param watch
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public boolean exists(String path, boolean watch) throws KeeperException, InterruptedException{
        return zk.exists(path, watch) != null;
    }

    /**
     * 断开连接
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        if (zk != null){
            zk.close();
        }
    }

    public static void main(String[] args) {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 30000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        if (watchedEvent.getType() == Event.EventType.None) {
                            System.out.println("zookeeper connected.");
                            latch.countDown();
                        } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                            System.out.println("zookeeper node changed.");
                        }
                    } else if (watchedEvent.getState() == Event.KeeperState.Disconnected) {
                        System.err.println("zookeeper disconnected.");
                    } else if (watchedEvent.getState() == Event.KeeperState.Expired) {
                        System.out.println("zookeeper expired, reconnecting..");
                    }
                }
            });
            latch.await();
            zk.create("/sample", "hello zookeeper".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            byte[] value = zk.getData("/sample", true, new Stat());
            System.out.println(new String(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
