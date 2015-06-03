# thrift-connection-pool

An implementation for thrift connection pool using Apache commons-pool2.  This implementation creates instances of TSocket and pool them. The following code snippet shows how to create an instance of TSocketProvider

```java
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

        //create TSocketProvider
        TSocketProvider provider = new TSocketProvider(new ThriftPoolableObjectFactory("127.0.0.1", 5396, 0),
                                        config, abandonedConfig);
  ```
  See [Example.java](src/test/java/com/zhentao/thrift/pool/Example.java) for more details
