package com.example.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/18
 * *@Version 1.0
 **/

@Data
@TableName("role")
@EqualsAndHashCode(callSuper = true)
public class RoleEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String displayName;
    private String description;

    @TableField(exist = false)
    private List<PermissionEntity> permissions;
}
