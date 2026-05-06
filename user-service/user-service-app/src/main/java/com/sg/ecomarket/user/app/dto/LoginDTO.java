package com.sg.ecomarket.user.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    /**
     * 用户信息
     */
    private UserDTO user;

    /**
     * Token
     */
    private String token;
}
