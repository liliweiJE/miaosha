package com.imooc.miaosha.dao;

import com.imooc.miaosha.domain.MiaoShaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by liliwei on 2018/3/5.
 */
@Mapper
public interface MiaoShaUserMapper {

    @Select("select * from miaosha_user where id = #{id}")
    public MiaoShaUser getById(@Param("id") long id);
}
