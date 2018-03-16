package com.imooc.miaosha.config;

import com.imooc.miaosha.access.UserContext;
import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.service.MiaoShaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by liliwei on 2018/3/5.
 */

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private MiaoShaUserService miaoShaUserService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz=methodParameter.getParameterType();
        return clazz== MiaoShaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        return UserContext.getUser();
    }

}
