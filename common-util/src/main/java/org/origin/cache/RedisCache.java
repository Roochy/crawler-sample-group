package org.origin.cache;

import org.origin.serializer.HessianSerializer;
import org.origin.serializer.Serializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author roochy
 * @package org.origin.crawler.util
 * @since 2017/12/24
 */
public class RedisCache {
    private JedisPool jedisPool;

    /** 最大连接数, 此处不做限制 */
    private static final int MAX_ACTIVE = -1;
    /** 最大空闲连接数, 此处为 redis 默认值 */
    private static final int MAX_IDLE = 8;
    /** 获取连接实例时的超时时间 3min */
    private static final int MAX_WAIT = 180000;
    /** 和redis服务建立连接时的超时时间 */
    private static final int TIMEOUT = 180;
    /** 获取实例前验证, 确保每个获取的实例都是可用的 */
    private static final boolean TEST_ON_BORROW = true;
    private Serializer serializer = new HessianSerializer();

    public RedisCache(String host, int port) {
        /**
         * 配置连接参数
         */
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(MAX_ACTIVE);
        config.setMaxIdle(MAX_IDLE);
        config.setMaxWaitMillis(MAX_WAIT);
        config.setTestOnBorrow(TEST_ON_BORROW);
        this.jedisPool = new JedisPool(config, host, port, TIMEOUT);
    }

    public byte[] serialize(Object object) {
        return serializer.serialize(object);
    }

    public byte[][] serialize(Object[] object) {
        List<byte[]> data = new ArrayList<>();
        for (Object obj : object) {
            data.add(serialize(obj));
        }
        return data.toArray(new byte[][]{});
    }

    public Object deserialize(byte[] object) {
        return serializer.deserialize(object);
    }

    public List<Object> deserialize(List<byte[]> object) {
        List<Object> data = new ArrayList<>();
        for (byte[] obj : object) {
            data.add(deserialize(obj));
        }
        return data;
    }

    public long incr(String key) {
        Jedis conn = null;
        long id = 1L;
        try {
            conn = jedisPool.getResource();
            id = conn.incr(serialize(key));
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return id;
    }

    public void set(String key, Serializable value) {
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            conn.set(serialize(key), serialize(value));
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public Object get(String key) {
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            byte[] object  = conn.get(serialize(key));
            if (object == null) {
                return object;
            }
            return deserialize(object);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public boolean exists(String key) {
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            return conn.exists(serialize(key));
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void expire(String key, int seconds) {
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            conn.expire(serialize(key), seconds);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void sadd(String key, Serializable... value) {
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            conn.sadd(serialize(key), serialize(value));
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public Object spop(String key) {
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            Object ret = null;
            byte[] raw = conn.spop(serialize(key));
            if (raw != null) {
                ret = deserialize(raw);
            }
            return ret;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public Set<Object> spop(String key, long count) {
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            Set<Object> data = new HashSet<>();
            Set<byte[]> raw = conn.spop(serialize(key), count);
            if (raw != null) {
                raw.forEach(r -> data.add(deserialize(r)));
            }
            return data;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public boolean sismember(String key, Serializable value) {
        Jedis conn = null;
        int ret = 0;
        try {
            conn = jedisPool.getResource();
            return conn.sismember(serialize(key), serialize(value));
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public long scard(String key) {
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            return conn.scard(serialize(key));
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void srem(String key, Serializable value) {
        Jedis conn = null;
        int ret = 0;
        try {
            conn = jedisPool.getResource();
            conn.srem(serialize(key), serialize(value));
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void sdiffstore(String dest, String a, String b) {
        Jedis conn = null;
        int ret = 0;
        try {
            conn = jedisPool.getResource();
            conn.sdiffstore(serialize(dest), serialize(a), serialize(b));
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public long llen(String key) {
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            return conn.llen(serialize(key));
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void lpush(String key, Serializable value) {
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            conn.lpush(serialize(key), serialize(value));
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Insert all the specified values at the tail of the list stored at key.
     * @param key
     * @param value
     */
    public void rpush(String key, Serializable... value) {
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            conn.rpush(serialize(key), serialize(value));
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public Object lpop(String key) {
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            Object raw = deserialize(conn.lpop(serialize(key)));
            if (raw instanceof byte[][]) {
                byte[][] rawData = (byte[][]) raw;
                List<Object> data = new ArrayList<>();
                for (byte[] byteData : rawData){
                    data.add(deserialize(byteData));
                }
                return data.toArray();
            }
            return raw;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public Object rpop(String key) {
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            return deserialize(conn.rpop(serialize(key)));
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public List<Object> lrange(String key, long start, long end) {
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            return deserialize(conn.lrange(serialize(key), start, end));
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
