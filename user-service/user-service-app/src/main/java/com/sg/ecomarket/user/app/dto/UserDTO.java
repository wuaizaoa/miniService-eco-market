package com.sg.ecomarket.user.app.dto;

import lombok.Data;

import java.util.Date;

/**
 * 用户响应DTO
 */
@Data
public class UserDTO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 状态：0禁用 1正常
     */
    private Integer status;

    /**
     * 角色：user/admin
     */
    private String role;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;
}
