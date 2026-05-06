package com.sg.ecomarket.user.app.service;

import com.sg.ecomarket.common.enums.ErrorCode;
import com.sg.ecomarket.common.exception.BizException;
import com.sg.ecomarket.user.app.cmd.LoginCmd;
import com.sg.ecomarket.user.app.cmd.RegisterCmd;
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

        return new LoginDTO(toDTO(user), token);
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
