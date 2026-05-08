package com.sg.ecomarket.user.app.service;

import com.sg.ecomarket.common.enums.ErrorCode;
import com.sg.ecomarket.common.exception.BizException;
import com.sg.ecomarket.user.app.cmd.AdminLoginCmd;
import com.sg.ecomarket.user.app.cmd.AdminUserUpdateCmd;
import com.sg.ecomarket.user.app.cmd.LoginCmd;
import com.sg.ecomarket.user.app.cmd.RegisterCmd;
import com.sg.ecomarket.user.app.dto.AdminLoginDTO;
import com.sg.ecomarket.user.app.dto.LoginDTO;
import com.sg.ecomarket.user.app.dto.UserDTO;
import com.sg.ecomarket.user.app.util.JwtUtil;
import com.sg.ecomarket.user.domain.entity.User;
import com.sg.ecomarket.user.domain.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户应用服务
 */
@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private JwtUtil jwtUtil;

    private static final String SALT = "ecomarket";

    /**
     * 用户注册
     */
    public UserDTO register(RegisterCmd cmd) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(cmd.getUsername())) {
            throw new BizException(ErrorCode.USER_ALREADY_EXISTS);
        }

        // 创建用户
        User user = new User();
        user.setUsername(cmd.getUsername());
        user.setPassword(encryptPassword(cmd.getPassword()));
        user.setEmail(cmd.getEmail());
        user.setPhone(cmd.getPhone());
        user.setStatus(1);
        user.setRole("user");
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        // 保存用户
        User savedUser = userRepository.save(user);
        return toDTO(savedUser);
    }

    /**
     * 用户登录
     */
    public LoginDTO login(LoginCmd cmd) {
        // 根据用户名查询用户
        User user = userRepository.findByUsername(cmd.getUsername());
        if (user == null) {
            throw new BizException(ErrorCode.USER_NOT_FOUND);
        }

        // 验证密码
        if (!encryptPassword(cmd.getPassword()).equals(user.getPassword())) {
            throw new BizException(ErrorCode.USER_PASSWORD_ERROR);
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new BizException(ErrorCode.FORBIDDEN, "用户已被禁用");
        }

        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        return new LoginDTO(user.getId(), user.getUsername(), token);
    }

    /**
     * 管理员登录
     */
    public AdminLoginDTO adminLogin(AdminLoginCmd cmd) {
        // 根据用户名查询用户
        User user = userRepository.findByUsername(cmd.getUsername());
        if (user == null) {
            throw new BizException(ErrorCode.USER_NOT_FOUND);
        }

        // 验证密码
        if (!encryptPassword(cmd.getPassword()).equals(user.getPassword())) {
            throw new BizException(ErrorCode.USER_PASSWORD_ERROR);
        }

        // 检查是否为管理员
        if (!"admin".equals(user.getRole())) {
            throw new BizException(ErrorCode.ADMIN_REQUIRED);
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new BizException(ErrorCode.FORBIDDEN, "用户已被禁用");
        }

        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        return new AdminLoginDTO(user.getId(), user.getUsername(), token);
    }

    /**
     * 管理员查询所有用户
     */
    public List<UserDTO> adminListAllUsers(Long adminUserId) {
        // 验证管理员权限
        checkAdminPermission(adminUserId);

        List<User> userList = userRepository.findAll();
        return userList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * 管理员创建用户
     */
    public UserDTO adminCreateUser(Long adminUserId, RegisterCmd cmd) {
        // 验证管理员权限
        checkAdminPermission(adminUserId);

        return register(cmd);
    }

    /**
     * 管理员更新用户
     */
    public UserDTO adminUpdateUser(Long adminUserId, AdminUserUpdateCmd cmd) {
        // 验证管理员权限
        checkAdminPermission(adminUserId);

        User user = userRepository.findById(cmd.getId());
        if (user == null) {
            throw new BizException(ErrorCode.USER_NOT_FOUND);
        }

        // 更新字段
        if (cmd.getUsername() != null) {
            user.setUsername(cmd.getUsername());
        }
        if (cmd.getEmail() != null) {
            user.setEmail(cmd.getEmail());
        }
        if (cmd.getPhone() != null) {
            user.setPhone(cmd.getPhone());
        }
        if (cmd.getStatus() != null) {
            user.setStatus(cmd.getStatus());
        }
        if (cmd.getRole() != null) {
            user.setRole(cmd.getRole());
        }
        user.setUpdatedAt(new Date());

        User savedUser = userRepository.save(user);
        return toDTO(savedUser);
    }

    /**
     * 管理员删除用户
     */
    public void adminDeleteUser(Long adminUserId, Long userId) {
        // 验证管理员权限
        checkAdminPermission(adminUserId);

        User user = userRepository.findById(userId);
        if (user == null) {
            throw new BizException(ErrorCode.USER_NOT_FOUND);
        }

        userRepository.delete(userId);
    }

    /**
     * 根据ID查询用户
     */
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new BizException(ErrorCode.USER_NOT_FOUND);
        }
        return toDTO(user);
    }

    /**
     * 密码加密
     */
    private String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex((password + SALT).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 验证管理员权限
     */
    private void checkAdminPermission(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null || !"admin".equals(user.getRole())) {
            throw new BizException(ErrorCode.ADMIN_REQUIRED);
        }
    }

    /**
     * 转换为DTO
     */
    private UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}
