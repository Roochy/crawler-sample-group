package org.origin.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author roochy
 * @package org.origin.crawler.util
 * @since 2017/12/27
 */
public class HessianSerializer implements Serializer{
    private static final Logger log = LoggerFactory.getLogger(HessianSerializer.class);

    @Override
    public byte[] serialize(Object obj) {
        try {
            return serialize0(obj);
        } catch (NullPointerException e) {
            // ignore
        } catch (Exception e) {
            log.error("hessian serialize error : {}", e.getMessage());
        }
        return null;
    }

    @Override
    public Object deserialize(byte[] by) {
        try {
            return deserialize0(by);
        } catch (NullPointerException e) {
            // ignore
        } catch (Exception e) {
            log.error("hessian deserialize error : {}", e.getMessage());
        }
        return null;
    }

    public byte[] serialize0(Object obj) throws Exception {
        if(obj==null) throw new NullPointerException();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(os);
        ho.writeObject(obj);
        return os.toByteArray();
    }

    public Object deserialize0(byte[] by) throws Exception {
        if(by==null) throw new NullPointerException();

        ByteArrayInputStream is = new ByteArrayInputStream(by);
        HessianInput hi = new HessianInput(is);
        return hi.readObject();
    }
}
