package com.sg.ecomarket.user.adapter.controller;

import com.sg.ecomarket.common.result.Result;
import com.sg.ecomarket.user.app.cmd.AdminLoginCmd;
import com.sg.ecomarket.user.app.dto.AdminLoginDTO;
import com.sg.ecomarket.user.app.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private UserService userService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<AdminLoginDTO> adminLogin(@Validated @RequestBody AdminLoginCmd cmd) {
        AdminLoginDTO adminLoginDTO = userService.adminLogin(cmd);
        return Result.success(adminLoginDTO);
    }
}
