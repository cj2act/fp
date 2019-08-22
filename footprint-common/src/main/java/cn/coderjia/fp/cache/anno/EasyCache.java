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
public @interface EasyCache {

    /**
     * 缓存唯一key前缀
     * 默认命名规则: ClassSimpleName#MethodName
     * 如: A#method
     * class A {
     *     public void method(){}
     * }
     */
    String key() default "";

    /**
     * 数据域表达式
     * 解析规则:
     *  p/coll/arr 代表支持的参数类型
     *  0 1        代表参数下标
     *  对象与属性用[.]标识 对象多个属性间用[&]
     *  例:
     *  基本数据类型: p
     *    @EasyCache(fieldExps = "#p0=?")
     *    public String getCacheByP(String userName);
     *  集合类型: coll
     *    @EasyCache(fieldExps = "#coll0=?")
     *    public List<Long> getCacheByColl(List<Long> ids);
     *  集合类型: arr
     *    @EasyCache(fieldExps = "#coll0=?")
     *    public List<Long> getCacheByArr(Long[] idArr);
     *  上面三种组合在一起:
     *    @EasyCache(fieldExps = "#coll0=?#p1=?#arr2=?")
     *    public List<Long> getCacheByCom(List<Long> ids, String userName, Long[] idArr);
     *  对象类型:
     *    @EasyCache(fieldExps = "#coll0.ids=?&p0.userName=?#arr1=?")
     *    public EasyCacheDemoBean getCacheByBean(EasyCacheDemoBean easyCacheDemoBean, Long[] idArr){
     *        // 这个方法内部需要使用easyCacheDemoBean的ids与userName。
     *    }
     */
    String fieldExps() default "";

    /**
     * 是否过期，设置过期时间的好处是如果EasyCacheEvict漏掉，会在过期时间外保证缓存正确结果。
     * 开启后，默认过期时间为两分钟(easy.cache.expireTime调整默认过期时间)。
     */
    boolean isExpire() default false;

}
