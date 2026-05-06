package com.sg.ecomarket.order.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sg.ecomarket.order.infra.dataobject.OrderItemDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项Mapper接口
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemDO> {
}
