package com.sg.ecomarket.order.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sg.ecomarket.order.infrastructure.dataobject.OrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单Mapper接口
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderDO> {
}
