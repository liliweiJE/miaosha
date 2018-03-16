package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.domain.MiaoShaUser;

/**
 * Created by liliwei on 2018/3/8.
 */
public class MiaoshaMessage {
    private MiaoShaUser miaoShaUser;
    private long goodsId;

    public MiaoShaUser getMiaoShaUser() {
        return miaoShaUser;
    }

    public void setMiaoShaUser(MiaoShaUser miaoShaUser) {
        this.miaoShaUser = miaoShaUser;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
