package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.MiaoShaUserMapper;
import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liliwei on 2018/3/5.
 */
@Service
public class MiaoShaUserService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private MiaoShaUserMapper miaoShaUserMapper;

    public static final String COOKI_NAME_TOKEN = "token";

    public MiaoShaUser getById(long id){
        MiaoShaUser user=redisService.get(MiaoshaUserKey.getById,""+id,MiaoShaUser.class);
        if(null!=user){
            return user;
        }

        user=miaoShaUserMapper.getById(id);
        if(null!=null){
            redisService.set(MiaoshaUserKey.getById,""+id,user);
        }
        return  user;
    }

    public MiaoShaUser getByToken(HttpServletResponse response,String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }

        MiaoShaUser user=redisService.get(MiaoshaUserKey.token,token,MiaoShaUser.class);
        //延长cookie时间
        if(null!=user){
            addCookie(response,token,user);
        }
        return user;
    }

    public String login(HttpServletResponse response,LoginVo loginVo){
        if(null==loginVo){
           throw  new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile=loginVo.getMobile();
        String formPass=loginVo.getPassword();

        MiaoShaUser user=getById(Long.parseLong(mobile));
        if(null==user){
            throw  new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPass=user.getPassword();
        String dbSalt=user.getSalt();
        String calcPass=MD5Util.formPassToDBPass(formPass,dbSalt);
        if(!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token=UUIDUtil.uuid();
        addCookie(response,token,user);
        return token;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoShaUser user){
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie=new Cookie(COOKI_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
