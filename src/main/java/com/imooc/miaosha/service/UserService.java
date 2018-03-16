package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.UserMapper;
import com.imooc.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liliwei on 2018/3/2.
 */

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public User getById(int id){
        return userMapper.getByid(id);
    }
}
