package com.imooc.miaosha.access;

import com.imooc.miaosha.domain.MiaoShaUser;

/**
 * Created by liliwei on 2018/3/9.
 */
public class UserContext {

    private static ThreadLocal<MiaoShaUser> userHolder = new ThreadLocal<MiaoShaUser>();

    public static void setUser(MiaoShaUser user) {
        userHolder.set(user);
    }

    public static MiaoShaUser getUser() {
        return userHolder.get();
    }

}
