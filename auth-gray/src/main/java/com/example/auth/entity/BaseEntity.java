package com.example.auth.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/22
 * *@Version 1.0
 **/

/**
 * 基础实体类（包含公共字段）
 */
@Data
public class BaseEntity implements Serializable {
    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 删除标志（0-未删除，1-已删除）
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
