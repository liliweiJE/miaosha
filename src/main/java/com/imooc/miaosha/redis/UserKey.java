package com.imooc.miaosha.redis;

/**
 * Created by liliwei on 2018/3/5.
 */
public class UserKey extends BasePrefix {
    private UserKey(String prefix) {
        super(prefix);
    }
    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");

}
