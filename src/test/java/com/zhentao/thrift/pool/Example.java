package com.zhentao.thrift.pool;

import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Example {
    private static final Logger logger = LoggerFactory.getLogger(Example.class);
    private final TSocketProvider provider;

    public static void main(String[] args) throws TException, InterruptedException {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        //The followings are the minimum required settings to configure a pool
        config.setTestOnBorrow(true);
        config.setTestOnCreate(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        config.setLifo(false);

        config.setMaxTotal(5);
        config.setMaxIdle(5);
        config.setMaxIdle(5);
        config.setMinIdle(5);

        //when a idled connection is eligible for eviction
        config.setMinEvictableIdleTimeMillis(1000 * 60);
        //how often the evict thread to evict the idled connection
        config.setTimeBetweenEvictionRunsMillis(15000);

        //remove abandoned connections
        AbandonedConfig abandonedConfig = new AbandonedConfig();
        abandonedConfig.setRemoveAbandonedOnBorrow(true);
        abandonedConfig.setRemoveAbandonedOnMaintenance(true);
        TSocketProvider provider = new TSocketProvider(new ThriftPoolableObjectFactory("192.168.56.102", 5396, 0),
                                        config, abandonedConfig);

        Example ex = new Example(provider);
        ex.frameProtocol();
    }

    public Example(TSocketProvider provider) {
        this.provider = provider;
    }

    public void frameProtocol() {
        TSocket socket = null;
        try {
            socket = provider.getConnection();
            TTransport transport = new TFramedTransport(socket);
            TProtocol protocol = new TBinaryProtocol(transport);
            // YourRpcService.Client client = new YourRpcService.Client(protocol);
            // do whatever you want with client
        } catch (Exception e) {
            //make sure the socket is closed so it will be removed from pool
            //if connection reset exception happens, socket is not closed by default
            if (socket != null) {
                socket.close();
            }
            throw e;
        } finally {
            if (socket != null) {
                provider.returnObject(socket);
            }
        }
    }

    public void binaryProtocol() {
        TSocket socket = null;
        try {
            socket = provider.getConnection();
            TProtocol protocol = new TBinaryProtocol(socket);
            // YourRpcService.Client client = new YourRpcService.Client(protocol);
            // do whatever you want with client
        } catch (Exception e) {
            if (socket != null) {
                socket.close();
            }
            throw e;
        } finally {
            if (socket != null) {
                provider.returnObject(socket);
            }
        }
    }
}
