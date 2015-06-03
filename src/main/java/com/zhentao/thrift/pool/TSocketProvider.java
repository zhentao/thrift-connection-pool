package com.zhentao.thrift.pool;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.transport.TSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TSocketProvider implements ConnectionProvider<TSocket> {
    private static final Logger logger = LoggerFactory.getLogger(TSocketProvider.class);

    private ObjectPool<TSocket> objectPool;

    public TSocketProvider(ThriftPoolableObjectFactory factory) {
        this(factory, null, null);
    }

    public TSocketProvider(ThriftPoolableObjectFactory factory, GenericObjectPoolConfig config,
                                    AbandonedConfig abandonedConfig) {
        // Create an object pool to contain our active connections
        if (config == null) {
            config = new GenericObjectPoolConfig();
        }
        if (abandonedConfig != null
                                        && (abandonedConfig.getRemoveAbandonedOnBorrow() || abandonedConfig
                                                                        .getRemoveAbandonedOnMaintenance())) {
            objectPool = new GenericObjectPool<>(factory, config, abandonedConfig);
        } else {
            objectPool = new GenericObjectPool<>(factory, config);
        }
    }

    /**
     * destroy the connection pool
     */
    @Override
    public void destroy() {
        try {
            objectPool.close();
        } catch (Exception e) {
            logger.warn("unable to close the connection pool", e);
        }
        objectPool = null;
    }

    @Override
    public TSocket getConnection() {
        try {
            return objectPool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException("error getConnection()", e);
        }
    }

    @Override
    public void returnObject(TSocket socket) {
        try {
            objectPool.returnObject(socket);
        } catch (Exception e) {
            logger.warn("unable to return instance to the connection pool", e);
        }
    }
}