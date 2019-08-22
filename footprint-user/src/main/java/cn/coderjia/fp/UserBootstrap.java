package cn.coderjia.fp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author CoderJiA
 * @Description UserBootstrap
 * @Date 12/5/19 上午10:35
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class UserBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(UserBootstrap.class, args);
    }

}
