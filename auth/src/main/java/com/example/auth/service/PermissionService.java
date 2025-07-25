package com.example.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auth.dto.PermissionRequestDTO;
import com.example.auth.dto.PermissionResponseDTO;
import com.example.auth.entity.PermissionEntity;
import com.example.auth.mapper.PermissionMapper;
import com.example.auth.mapper.RolePermissionMapper;
import com.example.auth.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/22
 * *@Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;

    public PermissionResponseDTO hasPermission(PermissionRequestDTO permission) {
        // 1. 查询用户的角色ID
        Set<Long> roleIds = userRoleMapper.selectRoleIdsByUsername(permission.getUsername());
        if (CollectionUtils.isEmpty(roleIds)) return new PermissionResponseDTO(false);

        // 2. 查询角色的权限ID
        Set<Long> permissionIds = rolePermissionMapper.selectPermissionIdsByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(permissionIds)) return new PermissionResponseDTO(false);

        // 3. 查询匹配 URI 和 Method 的权限是否存在
        long count = permissionMapper.selectCount(
                new QueryWrapper<PermissionEntity>()
                        .in("id", permissionIds)
                        .eq("uri", permission.getUri())
                        .eq("method", permission.getMethod())
        );
        //
        return new PermissionResponseDTO(count>0.0);
    }
}
