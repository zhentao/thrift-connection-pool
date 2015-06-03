package com.zhentao.thrift.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThriftPoolableObjectFactory extends BasePooledObjectFactory<TSocket> {
    private static final Logger logger = LoggerFactory.getLogger(ThriftPoolableObjectFactory.class);
    private final String serviceHost;
    private final int servicePort;
    private final int timeOut;

    /**
     * @param serviceIP
     * @param servicePort
     * @param timeOut
     */
    public ThriftPoolableObjectFactory(String serviceHost, int servicePort, int timeOut) {
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
        this.timeOut = timeOut;
    }

    @Override
    public void destroyObject(PooledObject<TSocket> arg0) throws Exception {
        TSocket socket = arg0.getObject();
        if (socket.isOpen()) {
            socket.close();
        }
        logger.debug("destroy {}", socket);

    }

    @Override
    public TSocket create() throws TTransportException {
        TSocket socket = new TSocket(this.serviceHost, this.servicePort, this.timeOut);
        socket.open();
        logger.debug("create pooled socket: {}", socket);
        return socket;
    }

    @Override
    public PooledObject<TSocket> wrap(TSocket obj) {
        return new DefaultPooledObject<TSocket>(obj);
    }

    @Override
    public boolean validateObject(PooledObject<TSocket> arg0) {
        try {
            return arg0.getObject().isOpen();
        } catch (Exception e) {
            logger.error("error validating TSocket", e);
            return false;
        }
    }
}