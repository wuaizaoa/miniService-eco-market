package com.sg.ecomarket.user.domain.repository;

import com.sg.ecomarket.user.domain.entity.User;

/**
 * 用户仓库接口
 */
public interface UserRepository {

    /**
     * 保存用户
     */
    User save(User user);

    /**
     * 根据ID查询用户
     */
    User findById(Long id);

    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
}
