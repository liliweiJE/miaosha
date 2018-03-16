package com.imooc.miaosha.controller;

import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.MiaoShaUserService;
import com.imooc.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by liliwei on 2018/3/5.
 */

@Controller
@RequestMapping("/login")
public class LoginController {

    public static Logger logger=LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MiaoShaUserService miaoShaUserService;

    @RequestMapping("/to_login")
    public String toLogin(){return "login";}

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        logger.info(loginVo.toString());
        /*if(StringUtils.isEmpty(loginVo.getMobile())){
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        if(StringUtils.isEmpty(loginVo.getPassword())){
            return Result.error(CodeMsg.PASSWORD_EMPTY);
        }*/
        String token=miaoShaUserService.login(response,loginVo);
       return Result.success(token);
    }
}
