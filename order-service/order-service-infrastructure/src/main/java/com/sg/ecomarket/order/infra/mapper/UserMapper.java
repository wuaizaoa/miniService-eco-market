package com.sg.ecomarket.order.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sg.ecomarket.order.infra.dataobject.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口（仅用于权限验证）
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}
