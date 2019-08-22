package cn.coderjia.fp.cache.aop;

import cn.coderjia.fp.cache.strategy.EasyCacheRedisStrategy;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 服务内部缓存注解Aop实现
 * @author CoderJiA
 */
@Aspect
@Component
public class EasyCacheAop {

    @Autowired
    private EasyCacheRedisStrategy easyCacheRedisStrategy;

    /**
     * 定义WsCache缓存注解切入点
     */
    @Pointcut("@annotation(cn.coderjia.fp.cache.anno.EasyCache)")
    public void easyCache() { }

    /**
     * 目标方法的环绕处理
     */
    @Around("easyCache()")
    public Object doAround(JoinPoint joinPoint) {
        /**
         * 缓存策略池中Redis命中缓存策略
         */
        return easyCacheRedisStrategy.operate(joinPoint);
    }

}
