package com.imooc.miaosha.redis;

/**
 * Created by liliwei on 2018/3/2.
 */
public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();
}
