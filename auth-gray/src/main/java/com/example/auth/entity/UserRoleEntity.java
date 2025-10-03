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
@TableName("user_role")
public class UserRoleEntity {
    private Long userId;
    private Long roleId;
}
