package com.example.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/17
 * *@Version 1.0
 **/

@Data
@TableName("user")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity { //对应数据库

        @TableId(type = IdType.AUTO)
        private Long id;
        private String username;
        private String password;
        private String nickname;
        private String tenant_id;
        // 是否启用  --表为tinyint
        private Integer enabled;

        @TableLogic
        private Integer deleted;

        //用户角色列表（非数据库字段）
        @TableField(exist = false)
        private Set<RoleEntity> roles = new HashSet<>(); // 避免返回null

        /**
         * 用户权限列表（非数据库字段）
         */
        @TableField(exist = false)
        private List<PermissionEntity> permissions;
}
