package com.sg.ecomarket.user.app.cmd;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 管理员更新用户命令
 */
@Data
public class AdminUserUpdateCmd {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
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
     * 状态：0禁用 1正常
     */
    private Integer status;

    /**
     * 角色：user/admin
     */
    private String role;
}
