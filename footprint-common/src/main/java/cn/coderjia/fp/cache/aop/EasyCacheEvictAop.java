package cn.coderjia.fp.cache.aop;

import cn.coderjia.fp.cache.strategy.EasyCacheEvictRedisStrategy;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
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
public class EasyCacheEvictAop {

    @Autowired
    private EasyCacheEvictRedisStrategy easyCacheEvictRedisStrategy;

    /**
     * 定义EasyCache缓存注解切入点
     */
    @Pointcut("@annotation(cn.coderjia.fp.cache.anno.EasyCacheEvict)")
    public void easyCacheEvict() { }

    /**
     * 目标方法的后置处理，清除缓存
     */
    @After("easyCacheEvict()")
    public void doAfter(JoinPoint joinPoint) {
        /**
         * 缓存策略池中Redis清除缓存策略
         */
        easyCacheEvictRedisStrategy.operate(joinPoint);
    }

}
