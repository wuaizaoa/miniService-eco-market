package com.sg.ecomarket.user.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sg.ecomarket.user.infra.dataobject.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}
