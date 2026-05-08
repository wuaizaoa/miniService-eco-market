package com.sg.ecomarket.user.adapter.controller;

import com.sg.ecomarket.common.result.Result;
import com.sg.ecomarket.user.app.cmd.AdminUserUpdateCmd;
import com.sg.ecomarket.user.app.cmd.LoginCmd;
import com.sg.ecomarket.user.app.cmd.RegisterCmd;
import com.sg.ecomarket.user.app.dto.LoginDTO;
import com.sg.ecomarket.user.app.dto.UserDTO;
import com.sg.ecomarket.user.app.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 管理员查询所有用户
     */
    @GetMapping("/admin/list")
    public Result<List<UserDTO>> adminListAllUsers() {
        Long adminUserId = 1L; // TODO: 从Token解析
        List<UserDTO> userList = userService.adminListAllUsers(adminUserId);
        return Result.success(userList);
    }

    /**
     * 管理员创建用户
     */
    @PostMapping("/admin")
    public Result<UserDTO> adminCreateUser(@Validated @RequestBody RegisterCmd cmd) {
        Long adminUserId = 1L; // TODO: 从Token解析
        UserDTO userDTO = userService.adminCreateUser(adminUserId, cmd);
        return Result.success(userDTO);
    }

    /**
     * 管理员更新用户
     */
    @PutMapping("/admin/{id}")
    public Result<UserDTO> adminUpdateUser(@PathVariable Long id, @Validated @RequestBody AdminUserUpdateCmd cmd) {
        Long adminUserId = 1L; // TODO: 从Token解析
        cmd.setId(id);
        UserDTO userDTO = userService.adminUpdateUser(adminUserId, cmd);
        return Result.success(userDTO);
    }

    /**
     * 管理员删除用户
     */
    @DeleteMapping("/admin/{id}")
    public Result<Void> adminDeleteUser(@PathVariable Long id) {
        Long adminUserId = 1L; // TODO: 从Token解析
        userService.adminDeleteUser(adminUserId, id);
        return Result.success();
    }
}
