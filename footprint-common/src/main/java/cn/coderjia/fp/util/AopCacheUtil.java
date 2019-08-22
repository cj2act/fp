package cn.coderjia.fp.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 大数据分析研判系统内部Aop相关工具类
 * @author CoderJiA
 */
public class AopCacheUtil {

    /**
     * 获取目标类名称#方法名称
     */
    public static String getKey(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getSimpleName() + "#" + getMethod(joinPoint).getName();
    }

    /**
     * 获取目标方法
     */
    public static Method getMethod(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }

    /**
     * 获取目标方法返回值类型
     */
    public static Class<?> getMethodReturnType(Method method) {
        return method.getReturnType();
    }

    /**
     * 获取目标方法所有参数名字
     * <p>File-->Settings-->Build,Execution,Deployment-->Compiler-->Java Compiler中
     * 设置 Addition command paraters：为 -parameters </p>
     * @since jdk.1.8
     */
    public static String[] getMethodParamName(Method method) {
        Parameter[] parameters = method.getParameters();
        int paramLen = parameters.length;
        String[] ret = new String[paramLen];
        for (int i = 0; i < paramLen; i++) {
            ret[i] = parameters[i].getName();
        }
        return ret;
    }

    /**
     * 获取目标方法参数
     */
    public static Object[] getMethodArgs(JoinPoint joinPoint) {
        return joinPoint.getArgs();
    }

    /**
     * 调用目标方法
     */
    public static Object invoke(JoinPoint joinPoint) throws InvocationTargetException, IllegalAccessException {
        return getMethod(joinPoint)
                .invoke(joinPoint.getTarget(), joinPoint.getArgs());
    }


}
