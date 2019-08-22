package cn.coderjia.fp.cache.anno;

import java.lang.annotation.*;

/**
 * 服务内部缓存注解，方法级别注解。
 * @author CoderJiA
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EasyCacheEvict {

    /**
     * 缓存唯一key前缀列表
     * 如果默认的前缀，请参见{@link cn.coderjia.fp.cache.anno.EasyCache} key的默认生成规则。
     *     @WsCacheEvict(keys = {
     *             "WsCacheDemoService#getCache",
     *             "WsCacheDemoService#getCacheByColl",
     *             "cj_test_cache_key",  // 这种是自定义key前缀。
     *             "WsCacheDemoService#getCacheByCom"
     *     })
     *     public void cancelCacheCom();
     */
    String[] keys();

}
