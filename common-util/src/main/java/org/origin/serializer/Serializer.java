package org.origin.serializer;

/**
 * @author roochy
 * @package org.origin.crawler.service.util
 * @since 2018/1/10
 */
public interface Serializer {
    public byte[] serialize(Object obj);

    public Object deserialize(byte[] by);
}
