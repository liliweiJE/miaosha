package com.imooc.miaosha.vo;

import com.imooc.miaosha.domain.MiaoShaUser;

/**
 * Created by liliwei on 2018/3/7.
 */
public class GoodsDetailVo {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods ;
    private MiaoShaUser user;
    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }
    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }
    public int getRemainSeconds() {
        return remainSeconds;
    }
    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }
    public GoodsVo getGoods() {
        return goods;
    }
    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }
    public MiaoShaUser getUser() {
        return user;
    }
    public void setUser(MiaoShaUser user) {
        this.user = user;
    }
}
