package cn.coderjia.fp.cache.strategy;

import cn.coderjia.fp.cache.UniqueKeyGenerator;
import cn.coderjia.fp.cache.anno.EasyCache;
import cn.coderjia.fp.util.AopCacheUtil;
import cn.coderjia.fp.util.RedisUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * 缓存Redis具体策略角色
 * @author CoderJiA
 */
@Slf4j
@Component
public class EasyCacheRedisStrategy implements EasyCacheStrategy {

    /**
     * 默认过期时间为两分钟
     */
    @Value("${ws.cache.expireTime:120}")
    private Long expireTime;

    @Resource(name = "systemRedis")
    private RedisUtils redisUtils;

    /**
     * 缓存命中Redis具体操作
     */
    @Override
    public Object operate(JoinPoint joinPoint) {
        Object obj = null;
        try {
            // 获取当前注解
            EasyCache easyCache = AopCacheUtil
                    .getMethod(joinPoint)
                    .getAnnotation(EasyCache.class);

            // 获取key
            String key = UniqueKeyGenerator.gen(easyCache.key(), easyCache.fieldExps(), joinPoint);
            log.info("WsCacheRedisStrategy#operate key :{}", key);

            // 如果存在key直接返回
            if (redisUtils.exists(key)) {
                log.info("WsCacheRedisStrategy#operate use redis cache, cur key:{}", key);
                Class<?> methodReturnType = AopCacheUtil.getMethodReturnType(AopCacheUtil.getMethod(joinPoint));
                return JSON.parseObject(redisUtils.get(key), methodReturnType);
            }

            // 执行目标方法
            Method method = AopCacheUtil.getMethod(joinPoint);
            obj = method.invoke(joinPoint.getTarget(), joinPoint.getArgs());
            if (easyCache.isExpire()) {
                // 当前key缓存结果
                redisUtils.save(key, JSON.toJSONString(obj), expireTime);
            } else {
                // 当前key缓存结果
                redisUtils.save(key, JSON.toJSONString(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

}
