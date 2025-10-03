package com.example.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auth.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/22
 * *@Version 1.0
 **/

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleEntity> {

    @Select("SELECT ur.role_id FROM user_role ur WHERE ur.user_id = (SELECT u.id from user u where u" +
            ".username=#{username} limit 1)")
    Set<Long> selectRoleIdsByUsername(@Param("username") String username);
}
