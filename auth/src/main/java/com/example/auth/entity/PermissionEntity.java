package com.example.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/18
 * *@Version 1.0
 **/

@Data
@TableName("permission")
@EqualsAndHashCode(callSuper = true) //会将父类的字段也纳入比较和hashCode
public class PermissionEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String code;
    private Long parentID;
    private String uri;
    private String method;
    /**
     * 权限类型（1-菜单，2-按钮，3-接口）
     */
    private Integer type;
    private String description;
    @TableLogic
    private Integer deleted;
}
