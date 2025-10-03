package com.example.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auth.entity.RolePermissionEntity;
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
public interface RolePermissionMapper extends BaseMapper<RolePermissionEntity> {
    @Select({"<script>" +
            "SELECT permission_id FROM role_permission WHERE role_id IN " +
            "<foreach collection='roleIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>"})
    Set<Long> selectPermissionIdsByRoleIds(@Param("roleIds") Set<Long> roleIds);
}
