package com.sg.ecomarket.user.adapter.controller;

import com.sg.ecomarket.common.result.Result;
import com.sg.ecomarket.user.app.cmd.LoginCmd;
import com.sg.ecomarket.user.app.cmd.RegisterCmd;
import com.sg.ecomarket.user.app.dto.LoginDTO;
import com.sg.ecomarket.user.app.dto.UserDTO;
import com.sg.ecomarket.user.app.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserDTO> register(@Validated @RequestBody RegisterCmd cmd) {
        UserDTO userDTO = userService.register(cmd);
        return Result.success(userDTO);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginDTO> login(@Validated @RequestBody LoginCmd cmd) {
        LoginDTO loginDTO = userService.login(cmd);
        return Result.success(loginDTO);
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Result<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return Result.success(userDTO);
    }
}
