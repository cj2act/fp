package cn.coderjia.fp.cache.enums;


import lombok.Getter;

/**
 * redis的客户端与服务的对应关系
 */
@Getter
public enum RedisDBConfigEnum {
    SYSTEM_CLIENT("systemRedis", 0),//系统级的redis客户端

    ;
    String redisName;//自定义redis客户端名称
    int dbIndex;//指定该redis客户端使用的数据库编号

    RedisDBConfigEnum(String redisName, int dbIndex) {
        this.dbIndex = dbIndex;
        this.redisName = redisName;
    }

}
