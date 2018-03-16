package com.imooc.miaosha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by liliwei on 2018/3/2.
 */
@SpringBootApplication
public class MainApplication /*extends SpringBootServletInitializer*/ {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MainApplication.class);
    }*/
}
