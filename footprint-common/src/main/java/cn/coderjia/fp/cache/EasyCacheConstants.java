package cn.coderjia.fp.cache;

import java.util.regex.Pattern;

/**
 * 服务内部缓存常量。
 * @author CoderJiA
 */
public class EasyCacheConstants {

    /**
     * 数据域表达式常量，支持的数据类型
     */
    // 基础类型
    public static final String FIELD_EXPS_PREFIX_P = "p";
    // 数组类型
    public static final String FIELD_EXPS_PREFIX_PA = "arr";
    // 集合类型
    public static final String FIELD_EXPS_PREFIX_PS = "coll";

    public static final Pattern FIELD_EXPS_PATTERN = Pattern.compile("(p|arr|coll)");

    // 参数分割
    public static final String FIELD_EXPS_SPLIT_1 = "#";
    // 对象多个参数分割
    public static final String FIELD_EXPS_SPLIT_2 = "&";
    // 对象与属性关联符
    public static final String FIELD_EXPS_SPLIT_3 = ".";

    public static final String FIELD_EXPS_REPLACE = "=?";


}
