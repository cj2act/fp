package cn.coderjia.fp.cache;

import cn.coderjia.fp.util.AopCacheUtil;
import cn.coderjia.fp.util.StringInnerUtils;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;

import static cn.coderjia.fp.cache.EasyCacheConstants.*;

/**
 * 服务内部缓存唯一key生成
 * @author CoderJiA
 */
@Slf4j
public class UniqueKeyGenerator {

    public static String gen(String key, String fieldExps, JoinPoint joinPoint) {

        try {
            // Cache的默认Key前缀生成策略
            if (StringUtils.isBlank(key)) {
                key = AopCacheUtil.getKey(joinPoint);
            }
            // 按照#分割参数列表
            Iterable<String> params = Splitter.on(FIELD_EXPS_SPLIT_1).split(fieldExps);
            // 拼接数据域key
            StringBuilder fieldStr = new StringBuilder();
            params.forEach(param -> {
                if (StringUtils.isNotBlank(param)) {
                    // 对象赋值情况
                    if (param.contains(FIELD_EXPS_SPLIT_2)) {
                        fieldStr.append(objField(param, joinPoint));
                    } else {
                        // 单数据类型赋值情况
                        fieldStr.append(field(param, joinPoint));
                    }
                }
            });
            String retStr = fieldStr.toString();
            log.info("gen key before key:{}; retStr:{}",key, retStr);
            return StringInnerUtils.genCacheKey(key, retStr);
        } catch (Exception e) {
            log.error("UniqueKeyGenerator#gen err:", e);
            throw new RuntimeException("UniqueKeyGenerator gen failed...");
        }

    }

    /**
     * 对象类型
     */
    private static String objField(String param, JoinPoint joinPoint) {
        StringBuilder objField = new StringBuilder();
        List<String> objParams = Splitter.on(FIELD_EXPS_SPLIT_2).splitToList(param);
        objParams.forEach(objParam -> {
            try {
                List<String> innerStrArr = Splitter.on(FIELD_EXPS_SPLIT_3).splitToList(objParam);
                // 该字段类型
                Matcher matcher = FIELD_EXPS_PATTERN.matcher(innerStrArr.get(0));
                if (!matcher.find()) {
                    log.error("Str:{}", innerStrArr.get(0));
                    throw new RuntimeException("objField matcher err.");
                }
                String type = matcher.group();
                // 该字段下标
                int pos = Integer.parseInt(innerStrArr.get(0).substring(type.length()));
                // 该对象属性
                String objProperty = innerStrArr.get(1).replace(FIELD_EXPS_REPLACE, StringUtils.EMPTY);
                // 获取所有参数
                Object[] args = joinPoint.getArgs();
                Object curObj = args[pos];
                // 获取到当前对象的当前属性的值
                Field field = curObj.getClass().getDeclaredField(objProperty);
                // 当前属性的值
                field.setAccessible(true);
                Object o = field.get(curObj);
                objParam = getString(objParam, type, o);
                objField.append(objParam);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return objField.toString();
    }

    /**
     * 标准数据类型
     */
    private static String field(String param, JoinPoint joinPoint) {
        String objParam;
        // 该字段类型
        Matcher matcher = FIELD_EXPS_PATTERN.matcher(param);
        if (!matcher.find()) {
            log.error("Str:{}", param);
            throw new RuntimeException("objField matcher err.");
        }
        String type = matcher.group();
        // 该字段下标
        int pos = Integer.parseInt(param.substring(type.length(), param.indexOf(FIELD_EXPS_REPLACE)));
        // 获取所有参数
        Object[] args = joinPoint.getArgs();
        Object curObj = args[pos];
        objParam = getString(param, type, curObj);
        return objParam;
    }

    private static String getString(String objParam, String type, Object curObj) {
        if (FIELD_EXPS_PREFIX_P.equals(type)) {
            objParam = objParam.replace("?", String.valueOf(curObj));
        } else if (FIELD_EXPS_PREFIX_PA.equals(type)) {
            objParam = objParam.replace("?", Arrays.toString((Object[]) curObj));
        } else if (FIELD_EXPS_PREFIX_PS.equals(type)) {
            Collection<Object> collectionObj = (Collection<Object>) curObj;
            objParam = objParam.replace("?", Arrays.toString(collectionObj.toArray()));
        } else {
            throw new RuntimeException("objField type not found...");
        }
        return objParam;
    }

}
