package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.GoodsMapper;
import com.imooc.miaosha.domain.MiaoshaGoods;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liliwei on 2018/3/6.
 */

@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    public List<GoodsVo> listGoodsVo(){
        return goodsMapper.listGoodsvo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId){return goodsMapper.getGoodsVoByGoodsId(goodsId);}

    public boolean reduceStock(GoodsVo goods){
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        int ret=goodsMapper.reduceStock(g);
        return ret>0;
    }



}
