package cn.coderjia.fp.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


/**
 * @Author CoderJiA
 * @Description StringInnerUtils: 工程内部字符串通用方法提供
 **/
@Slf4j
public class StringInnerUtils {

    private static final String INNER_CHAR = "@";

    public static <T> T typeChange(Object obj, Class<T> targetClazz) {
        return JSONObject.parseObject(String.valueOf(obj), targetClazz);
    }

    /**
     * 要素生成相关业务code
     * @param type 类型
     * @param content 值
     * @param prefix 前缀
     */
    public static String genOpCode(String type, String content, String prefix) {
        return prefix + "_" + Sha1Util.hex(type + content);
    }

    /**
     * 缓存key生成
     * @param filedRet 数据域表达式拼接结果
     */
    public static String genCacheKey(String prefix, String filedRet) {
        return prefix + "_" + Sha1Util.hex(filedRet);
    }

    /**
     * 统计@字符
     */
    public static int countChar(String str) {
        int count = 0;
        if (StringUtils.isBlank(str)) {
            return count;
        }
        for (char inc: str.toCharArray()) {
            if (String.valueOf(inc).equals(INNER_CHAR)) {
                count ++;
            }
        }
        return count;
    }

    /**
     * 统计字符数量
     */
    public static int countChar(String str, String singleStr) {
        int count = 0;
        if (StringUtils.isBlank(str)) {
            return count;
        }
        for (char inc: str.toCharArray()) {
            if (String.valueOf(inc).equals(singleStr)) {
                count ++;
            }
        }
        return count;
    }

}
