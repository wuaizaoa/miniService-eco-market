package com.sg.ecomarket.order.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sg.ecomarket.order.domain.entity.Order;
import com.sg.ecomarket.order.domain.repository.OrderItemRepository;
import com.sg.ecomarket.order.domain.repository.OrderRepository;
import com.sg.ecomarket.order.infrastructure.dataobject.OrderDO;
import com.sg.ecomarket.order.infrastructure.mapper.OrderMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Resource
    private OrderItemRepository orderItemRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order save(Order order) {
        OrderDO orderDO = toDO(order);
        if (orderDO.getId() == null) {
            orderMapper.insert(orderDO);
        } else {
            orderMapper.updateById(orderDO);
        }
        
        Order savedOrder = toEntity(orderDO);
        
        // 保存订单项
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            for (com.sg.ecomarket.order.domain.entity.OrderItem orderItem : order.getOrderItems()) {
                orderItem.setOrderId(savedOrder.getId());
            }
            orderItemRepository.saveBatch(order.getOrderItems());
            savedOrder.setOrderItems(order.getOrderItems());
        }
        
        return savedOrder;
    }

    @Override
    public Order findById(Long id) {
        OrderDO orderDO = orderMapper.selectById(id);
        Order order = toEntity(orderDO);
        if (order != null) {
            List<com.sg.ecomarket.order.domain.entity.OrderItem> orderItems = orderItemRepository.findByOrderId(id);
            order.setOrderItems(orderItems);
        }
        return order;
    }

    @Override
    public Order findByOrderNo(String orderNo) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getOrderNo, orderNo);
        OrderDO orderDO = orderMapper.selectOne(wrapper);
        Order order = toEntity(orderDO);
        if (order != null) {
            List<com.sg.ecomarket.order.domain.entity.OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            order.setOrderItems(orderItems);
        }
        return order;
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getUserId, userId);
        wrapper.orderByDesc(OrderDO::getCreatedAt);
        List<OrderDO> orderDOList = orderMapper.selectList(wrapper);
        List<Order> orders = orderDOList.stream().map(this::toEntity).collect(Collectors.toList());
        
        // 加载每个订单的订单项
        for (Order order : orders) {
            List<com.sg.ecomarket.order.domain.entity.OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            order.setOrderItems(orderItems);
        }
        
        return orders;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        LambdaUpdateWrapper<OrderDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(OrderDO::getId, id);
        wrapper.set(OrderDO::getStatus, status);
        return orderMapper.update(null, wrapper) > 0;
    }

    @Override
    public List<Order> findAll() {
        List<OrderDO> orderDOList = orderMapper.selectList(null);
        List<Order> orders = orderDOList.stream().map(this::toEntity).collect(Collectors.toList());
        
        // 加载每个订单的订单项
        for (Order order : orders) {
            List<com.sg.ecomarket.order.domain.entity.OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            order.setOrderItems(orderItems);
        }
        
        return orders;
    }

    @Override
    public Order findWithItemsById(Long id) {
        return findById(id);
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
