package cn.coderjia.fp.cache.strategy;

import cn.coderjia.fp.cache.anno.EasyCacheEvict;
import cn.coderjia.fp.util.AopCacheUtil;
import cn.coderjia.fp.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * 缓存清空Redis具体策略角色
 * @author CoderJiA
 */
@Slf4j
@Component
public class EasyCacheEvictRedisStrategy implements EasyCacheStrategy {

    @Resource(name = "systemRedis")
    private RedisUtils redisUtils;

    /**
     * 缓存清空Redis具体操作
     * <p>注解{@link cn.coderjia.fp.cache.anno.EasyCache}的作用是某个方法的执行数据变更，导致对应的缓存失效。
     *     <ul>
     *     例如：
     *         <li> public List<User> query(String name);   // 查询方法</li>
     *         <li> public void update(String name);        // 更新方法，导致查询方法的缓存失效，那么就将redis中包含查询方法定义的key前缀内容全部清空。</li>
     *     </ul>
     * </p>
     * @param joinPoint 切入点对象
     */
    @Override
    public Object operate(JoinPoint joinPoint) {
        /*
         * 获取当前注解参数
         */
        EasyCacheEvict easyCacheEvict = AopCacheUtil
                .getMethod(joinPoint)
                .getAnnotation(EasyCacheEvict.class);
        /*
         * 获取影响到所有key前缀数组，并执行删除操作。
         */
        String[] keys = easyCacheEvict.keys();
        Optional.of(keys).ifPresent(ks -> {
            log.info("WsCacheEvictRedisStrategy#operate keys:{}", Arrays.toString(ks));
            IntStream.range(0, ks.length).forEach(i -> redisUtils.removeByPrefix(ks[i]));
        });
        return null;
    }

}
