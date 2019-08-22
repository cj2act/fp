package cn.coderjia.fp.util;

import com.alibaba.fastjson.JSON;
import io.lettuce.core.RedisException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis操作工具类
 */
public class RedisUtils {

    private RedisTemplate redisTemplate;

    public RedisUtils(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public <T> void save(final String key, T value, Long expireTime) {
        try {
            redisTemplate.opsForValue().set(key, value);
            //设置key的有效期
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        } catch (Exception ex) {
            throw new RedisException("向redis写入数据【" + key + ":" + JSON.toJSONString(value) + "】失败", ex);
        }
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public <T> void save(final String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception ex) {
            throw new RedisException("向redis写入数据【" + key + ":" + JSON.toJSONString(value) + "】失败", ex);
        }
    }

    /**
     * 向map写数据
     *
     * @param key
     * @param mapKey
     * @param value
     * @param <T>
     */
    public <T> void saveHash(final String key, final String mapKey, T value) {
        try {
            redisTemplate.opsForHash().put(key, mapKey, value);
        } catch (Exception ex) {
            throw new RedisException("向redisMap写入数据【" + key + ":" + mapKey + ":" + JSON.toJSONString(value) + "】失败", ex);
        }
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public <T> T get(final String key) {
        try {
            return (T) redisTemplate.opsForValue().get(key);
        } catch (Exception ex) {
            throw new RedisException("从redis中获取key等于【" + key + "】的值失败", ex);
        }
    }

    /**
     * 根据key值从map内获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    public <T> T getValueFromHash(final String key, final String hashKey) {
        try {
            if (redisTemplate.opsForHash().hasKey(key, hashKey)) {
                return (T) redisTemplate.opsForHash().get(key, hashKey);
            }
        } catch (Exception ex) {
            throw new RedisException("key值为【" + key + ":" + hashKey + "】的value获取失败", ex);
        }
        return null;
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            try {
                redisTemplate.delete(key);
            } catch (Exception ex) {
                throw new RedisException("删除key失败，key：" + key);
            }
        }
    }

    /**
     * 清理当前数据库全部key
     * 慎用！！！！！
     */
    public void removeAll() {
        try {
            redisTemplate.execute(new RedisCallback() {
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.flushDb();
                    return "ok";
                }
            });
        } catch (Exception ex) {
            throw new RedisException("清理当前数据库全部key值失败", ex);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception ex) {
            throw new RedisException("判断key是否存在时发生错误，key：" + key);
        }
    }

    public boolean hmset(String key, Map<String, String> value) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().putAll(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, String> hmget(String key) {
        Map<String, String> result = null;
        try {
            result = redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 按规则批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 按规则批量包含前缀的key
     *
     * @param prefix 前缀
     */
    public void removeByPrefix(final String prefix) {
        removePattern(prefix + "*");
    }

}
