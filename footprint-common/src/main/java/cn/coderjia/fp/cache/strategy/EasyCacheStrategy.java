package cn.coderjia.fp.cache.strategy;

import org.aspectj.lang.JoinPoint;

/**
 * 抽象缓存策略角色
 * @author CoderJiA
 */
public interface EasyCacheStrategy {

    /**
     * 定义统一缓存操作，在具体的缓存策略角色中实现。
     * @param joinPoint 切入点对象
     */
    Object operate(JoinPoint joinPoint);

}
