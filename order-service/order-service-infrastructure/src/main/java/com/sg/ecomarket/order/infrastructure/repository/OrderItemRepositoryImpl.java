package com.sg.ecomarket.order.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sg.ecomarket.order.domain.entity.OrderItem;
import com.sg.ecomarket.order.domain.repository.OrderItemRepository;
import com.sg.ecomarket.order.infrastructure.dataobject.OrderItemDO;
import com.sg.ecomarket.order.infrastructure.mapper.OrderItemMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单项仓储实现
 */
@Repository
public class OrderItemRepositoryImpl implements OrderItemRepository {

    @Resource
    private OrderItemMapper orderItemMapper;

    @Override
    public OrderItem save(OrderItem orderItem) {
        OrderItemDO orderItemDO = toDO(orderItem);
        if (orderItemDO.getId() == null) {
            orderItemMapper.insert(orderItemDO);
        } else {
            orderItemMapper.updateById(orderItemDO);
        }
        return toEntity(orderItemDO);
    }

    @Override
    public List<OrderItem> saveBatch(List<OrderItem> orderItems) {
        List<OrderItemDO> orderItemDOList = orderItems.stream()
                .map(this::toDO)
                .collect(Collectors.toList());
        orderItemDOList.forEach(orderItemDO -> {
            if (orderItemDO.getId() == null) {
                orderItemMapper.insert(orderItemDO);
            } else {
                orderItemMapper.updateById(orderItemDO);
            }
        });
        return orderItemDOList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItemDO::getOrderId, orderId);
        List<OrderItemDO> orderItemDOList = orderItemMapper.selectList(wrapper);
        return orderItemDOList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    private OrderItemDO toDO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        OrderItemDO orderItemDO = new OrderItemDO();
        BeanUtils.copyProperties(orderItem, orderItemDO);
        return orderItemDO;
    }

    private OrderItem toEntity(OrderItemDO orderItemDO) {
        if (orderItemDO == null) {
            return null;
        }
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyProperties(orderItemDO, orderItem);
        return orderItem;
    }
}
