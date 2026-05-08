package com.sg.ecomarket.user.infra.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sg.ecomarket.user.domain.entity.User;
import com.sg.ecomarket.user.domain.repository.UserRepository;
import com.sg.ecomarket.user.infra.dataobject.UserDO;
import com.sg.ecomarket.user.infra.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户仓库实现
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    @Resource
    private UserMapper userMapper;

    @Override
    public User save(User user) {
        UserDO userDO = toDO(user);
        if (userDO.getId() == null) {
            userMapper.insert(userDO);
        } else {
            userMapper.updateById(userDO);
        }
        return toEntity(userDO);
    }

    @Override
    public User findById(Long id) {
        UserDO userDO = userMapper.selectById(id);
        return toEntity(userDO);
    }

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUsername, username);
        UserDO userDO = userMapper.selectOne(wrapper);
        return toEntity(userDO);
    }

    @Override
    public boolean existsByUsername(String username) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUsername, username);
        return userMapper.selectCount(wrapper) > 0;
    }

    @Override
    public List<User> findAll() {
        List<UserDO> userDOList = userMapper.selectList(null);
        return userDOList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    private UserDO toDO(User user) {
        if (user == null) {
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(user, userDO);
        return userDO;
    }

    private User toEntity(UserDO userDO) {
        if (userDO == null) {
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(userDO, user);
        return user;
    }
}
