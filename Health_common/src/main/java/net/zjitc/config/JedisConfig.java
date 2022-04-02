package net.zjitc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig {
    /**
     * redis--IP地址
     */
    @Value("${spring.redis.host}")
    private String host;
    /**
     * redis--端口
     */
    @Value("${spring.redis.port}")
    private int port;
    /**
     * redis--密码
     */
//    @Value("${spring.redis.password}")
//    private String password;
    /**
     * redis--逻辑库选择
     */
//    @Value("${spring.redis.database}")
//    private int database;
    /**
     * redis--连接池最大空闲
     */
//    @Value("${spring.redis.jedis.pool.max-idle}")
//    private int maxIdle;
    /**
     * redis--连接池最小空闲
     */
//    @Value("${spring.redis.jedis.pool.min-idle}")
//    private int minIdle;
    /**
     * redis--连接池最大等待时间
     */
//    @Value("${spring.redis.jedis.pool.max-wait}")
//    private long maxWait;
    /**
     * redis--超时时间
     */
    @Value("${spring.redis.timeout}")
    private int timeout;

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
        return jedisPool;
    }
}
