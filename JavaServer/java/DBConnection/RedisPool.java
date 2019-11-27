package DBConnection;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class RedisPool {


    private static final String HOST = "localhost";
    private static final JedisPoolConfig poolConfig = buildPoolConfig();
    private static final JedisPool REDIS_POOL = new JedisPool(poolConfig, HOST, 6379, Protocol.DEFAULT_TIMEOUT);

    private static JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100);
        poolConfig.setMaxIdle(100);
        poolConfig.setMinIdle(10);
        return poolConfig;
    }

    public static Jedis getResource() {
        return REDIS_POOL.getResource();
    }

//    public static void main(String[] args) {
//        RedisPool redisPool = new RedisPool();
//        Jedis jedis = RedisPool.getResource();
//        jedis.hset("a","b","c");
//        System.out.println(jedis.hgetAll("a"));
//
//    }
}