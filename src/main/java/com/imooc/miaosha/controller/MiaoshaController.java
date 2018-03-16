package com.imooc.miaosha.controller;

import com.imooc.miaosha.access.AccessLimit;
import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.rabbitmq.MiaoshaMessage;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liliwei on 2018/3/6.
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private MQSender mqSender;

    private HashMap<Long,Boolean> localOverMap=new HashMap<Long, Boolean>();

    @RequestMapping(value = "/{path}//do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(Model model, MiaoShaUser miaoShaUser,
                                @RequestParam("goodsId") long goodsId, @PathVariable("path")String path){

        model.addAttribute("user",miaoShaUser);
        if(null==miaoShaUser){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        boolean check=miaoshaService.checkPath(miaoShaUser,goodsId,path);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if(over) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        long stock=redisService.decr(GoodsKey.getMiaoshaGoodsStock,""+goodsId);
        if(stock<0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(miaoShaUser.getId(), goodsId);
        if(null!=order){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        MiaoshaMessage msg=new MiaoshaMessage();
        msg.setGoodsId(goodsId);
        msg.setMiaoShaUser(miaoShaUser);
        mqSender.sendMiaoshaMessage(msg);
        return Result.success(0);
       /* //判断库存
        GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
        int stock=goods.getGoodsStock();
        if(stock<=0){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(miaoShaUser.getId(), goodsId);
        if(null!=order){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(miaoShaUser, goods);
        return Result.success(orderInfo);*/
    }

    @AccessLimit(seconds=5, maxCount=5, needLogin=true)
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(Model model,HttpServletRequest request,MiaoShaUser user
            ,@RequestParam("goodsId")long goodsId,@RequestParam(value = "verifyCode",defaultValue = "0")int verifyCode){
        model.addAttribute("user",user);
        if(null==user){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean check=miaoshaService.checkVerifyCode(user,goodsId,verifyCode);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path=miaoshaService.createMiaoshaPath(user,goodsId);
        return Result.success(path);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        if(null==goodsVos){
            return;
        }
        for(GoodsVo goodsVo:goodsVos){
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goodsVo.getId(),goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(), false);
        }
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoShaUser user, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        if(null==user){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result  =miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @RequestMapping(value="/verifyCode", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCod(HttpServletResponse response, MiaoShaUser user,
                                              @RequestParam("goodsId")long goodsId){
        if(null==user){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image  = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out=response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();;
            out.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }

    }

}
