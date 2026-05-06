package com.sg.ecomarket.user.app.cmd;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 注册请求命令
 */
@Data
public class RegisterCmd {

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

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;
}
