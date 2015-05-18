package com.zhentao.thrift.pool;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class Example {
    private final TSocketProvider provider;

    public Example(TSocketProvider provider) {
        this.provider = provider;
    }

    public void frameProtocol() {
        TSocket socket = provider.getConnection();
        try {
            TTransport transport = new TFramedTransport(socket);
            TProtocol protocol = new TBinaryProtocol(transport);
            //YourRpcService.Client client = new YourRpcService.Client(protocol);
            //do whatever you want with client
        } finally {
            if (socket != null) {
                provider.returnObject(socket);
            }
        }
    }

    public void binaryProtocol() {
        TSocket socket = provider.getConnection();
        try {
            TProtocol protocol = new TBinaryProtocol(socket);
            //YourRpcService.Client client = new YourRpcService.Client(protocol);
            //do whatever you want with client
        } finally {
            if (socket != null) {
                provider.returnObject(socket);
            }
        }
    }
}
