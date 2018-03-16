package com.imooc.miaosha.dao;

import com.imooc.miaosha.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by liliwei on 2018/3/2.
 */
@Mapper
public interface UserMapper {

    @Select("select * from miaosha_user where id =#{id}")
    public User getByid(@Param("id")int id);
}
