package com.zhentao.thrift.pool;

public interface ConnectionProvider<T> {

    /**
     * retrieve a connection from pool
     * @return
     */
    public T getConnection();

    /**
     * return the instance to the pool
     * @param socket
     */
    public void returnObject(T instance);

    /**
     * destroy the pool
     */
    void destroy() ;
}
