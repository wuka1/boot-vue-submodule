package com.example.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auth.entity.PermissionEntity;
import com.example.auth.entity.RoleEntity;
import com.example.auth.entity.UserEntity;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/18
 * *@Version 1.0
 **/

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    @Select("SELECT r.* FROM role r " +
            "JOIN user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<RoleEntity> findRolesByUserId(Long userId);

    @Select("SELECT p.* FROM permission p " +
            "JOIN role_permission rp ON p.id = rp.permission_id " +
            "JOIN user_role ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<PermissionEntity> findPermissionsByUserId(Long userId);

}
