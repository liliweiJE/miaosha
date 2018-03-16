package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.vo.GoodsDetailVo;
import com.imooc.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by liliwei on 2018/3/6.
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, MiaoShaUser miaoShaUser){
        model.addAttribute("user",miaoShaUser);
        //取缓存
        String html=redisService.get(GoodsKey.getGoodsList,"",String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        List<GoodsVo> goodsList= goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
        //return "goods_list";
        SpringWebContext cxt=new SpringWebContext(request,response,request.getServletContext(),
                request.getLocale(),model.asMap(),applicationContext);
        html=thymeleafViewResolver.getTemplateEngine().process("goods_list",cxt);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }

    //页面缓存
    @RequestMapping(value = "/to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
    public String goodsDetail2(HttpServletRequest request, HttpServletResponse response,Model model, MiaoShaUser miaoShaUser, @PathVariable("goodsId") long goodsId){
        GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
        //取缓存
        String html=redisService.get(GoodsKey.getGoodsDetail,goodsId+"",String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        long startAt=goods.getStartDate().getTime();
        long endAt=goods.getEndDate().getTime();
        long now=System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("user",miaoShaUser);
        model.addAttribute("goods",goods);
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);

        SpringWebContext cxt=new SpringWebContext(request,response,request.getServletContext(),
                request.getLocale(),model.asMap(),applicationContext);
        html=thymeleafViewResolver.getTemplateEngine().process("goods_detail",cxt);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail,goodsId+"",html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> goodsDetail(HttpServletRequest request, HttpServletResponse response, Model model, MiaoShaUser miaoShaUser, @PathVariable("goodsId") long goodsId){
        GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt=goods.getStartDate().getTime();
        long endAt=goods.getEndDate().getTime();
        long now=System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(miaoShaUser);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }


}
