package com.imooc.miaosha.vo;

import com.imooc.miaosha.domain.Goods;

import java.util.Date;

/**
 * Created by liliwei on 2018/3/6.
 */
public class GoodsVo extends Goods {

    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private Double miaoshaPrice;

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getMiaoshaPrice() {
        return miaoshaPrice;
    }

    public void setMiaoshaPrice(Double miaoshaPrice) {
        this.miaoshaPrice = miaoshaPrice;
    }
}
