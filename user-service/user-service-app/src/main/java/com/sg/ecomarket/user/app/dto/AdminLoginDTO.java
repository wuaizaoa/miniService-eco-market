package com.sg.ecomarket.user.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员登录响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginDTO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * Token
     */
    private String token;
}
