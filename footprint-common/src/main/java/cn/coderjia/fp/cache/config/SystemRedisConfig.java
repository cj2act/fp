package cn.coderjia.fp.cache.config;

import cn.coderjia.fp.cache.enums.RedisDBConfigEnum;
import cn.coderjia.fp.util.RedisUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 配置redis客户端
 */
@Configuration
public class SystemRedisConfig implements EnvironmentAware {

    private String hostName;//主机地址
    private String password;//连接密码
    private int port;//连接端口
    private int maxActive;//最大连接数, 默认8个
    private int maxIdle;//最大空闲连接数, 默认8个
    private int minIdle;//最小空闲连接数, 默认0
    private long maxWait;//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1

    @Override
    public void setEnvironment(Environment environment) {
        this.hostName = environment.getProperty("redis.host");
        this.password = environment.getProperty("redis.password");
        this.port = environment.getProperty("redis.port", Integer.class);
        this.maxActive = environment.getProperty("redis.maxActive", Integer.class);
        this.maxIdle = environment.getProperty("redis.maxIdle", Integer.class);
        this.minIdle = environment.getProperty("redis.minIdle", Integer.class);
        this.maxWait = environment.getProperty("redis.maxWait", Integer.class);
    }

    /**
     * 设置key和value的序列化方式
     *
     * @param redisTemplate
     */
    private void setSerializer(RedisTemplate redisTemplate) {
        //value的序列化方式
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        //key的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
    }

    /**
     * 创建连接池
     *
     * @param bdIndex
     * @return
     */
    private RedisConnectionFactory initRedisConnectionFactory(int bdIndex) {
        JedisConnectionFactory jedisFactory = new JedisConnectionFactory(initJedisPoolConfig());
        jedisFactory.setHostName(hostName);
        jedisFactory.setPort(port);
        jedisFactory.setPassword(password);
        jedisFactory.setDatabase(bdIndex);
        jedisFactory.afterPropertiesSet(); // 初始化连接池配置
        return jedisFactory;
    }

    /**
     * 初始化连接池属性
     *
     * @return
     */
    private JedisPoolConfig initJedisPoolConfig() {
        // 进行连接池属性设置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWait);
        return poolConfig;
    }


    /**
     * 系统级的redis客户端，各服务共用，缓存登录用户相关信息
     *
     * @return
     */
    @Bean("systemRedis")
    public RedisUtils initRedisUtil() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(initRedisConnectionFactory(RedisDBConfigEnum.SYSTEM_CLIENT.getDbIndex()));
        setSerializer(redisTemplate);
        redisTemplate.afterPropertiesSet();
        return new RedisUtils(redisTemplate);
    }
    
}