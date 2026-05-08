package com.sg.ecomarket.user.app.cmd;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 管理员登录命令
 */
@Data
public class AdminLoginCmd {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
