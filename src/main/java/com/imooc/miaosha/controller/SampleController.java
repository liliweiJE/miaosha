package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.User;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.UserKey;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liliwei on 2018/3/2.
 */
@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    private MQSender sender;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

   /* @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq(){
        sender.send("hello,imooc");
        return Result.success("hello success");
    }*/

    @RequestMapping("/sayHello")
    public String sayHello(Model model){

        model.addAttribute("name","liliwei");
        return "hello";
    }

    /*@RequestMapping("/redis")
    @ResponseBody
    public Result<String> getById(){
        String  i  = redisService.get("test1", String.class);
        return Result.success(i);
    }*/

    @RequestMapping("/db/gettx")
    @ResponseBody
    public Result getRedis(){
        UserKey uk=UserKey.getById;
        User user=redisService.get(uk,1+"",User.class);
        return Result.success(user);
    }

    @RequestMapping("/db/settx")
    @ResponseBody
    public void setRedis(){
        User user=userService.getById(1);
        UserKey uk=UserKey.getById;
        boolean bu=redisService.set(uk,1+"",user);
    }
}
