package com.sg.ecomarket.order.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sg.ecomarket.order.domain.entity.Order;
import com.sg.ecomarket.order.domain.repository.OrderRepository;
import com.sg.ecomarket.order.infrastructure.dataobject.OrderDO;
import com.sg.ecomarket.order.infrastructure.mapper.OrderMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单仓储实现
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public Order save(Order order) {
        OrderDO orderDO = toDO(order);
        if (orderDO.getId() == null) {
            orderMapper.insert(orderDO);
        } else {
            orderMapper.updateById(orderDO);
        }
        return toEntity(orderDO);
    }

    @Override
    public Order findById(Long id) {
        OrderDO orderDO = orderMapper.selectById(id);
        return toEntity(orderDO);
    }

    @Override
    public Order findByOrderNo(String orderNo) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getOrderNo, orderNo);
        OrderDO orderDO = orderMapper.selectOne(wrapper);
        return toEntity(orderDO);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getUserId, userId);
        wrapper.orderByDesc(OrderDO::getCreatedAt);
        List<OrderDO> orderDOList = orderMapper.selectList(wrapper);
        return orderDOList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        LambdaUpdateWrapper<OrderDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(OrderDO::getId, id);
        wrapper.set(OrderDO::getStatus, status);
        return orderMapper.update(null, wrapper) > 0;
    }

    private OrderDO toDO(Order order) {
        if (order == null) {
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(order, orderDO);
        return orderDO;
    }

    private Order toEntity(OrderDO orderDO) {
        if (orderDO == null) {
            return null;
        }
        Order order = new Order();
        BeanUtils.copyProperties(orderDO, order);
        return order;
    }
}
