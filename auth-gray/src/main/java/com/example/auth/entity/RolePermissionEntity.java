package com.example.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/22
 * *@Version 1.0
 **/

@Data
@TableName("role_permission")
public class RolePermissionEntity {
    private Long roleId;
    private Long permissionId;
}
