package com.sg.ecomarket.order.infra.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sg.ecomarket.order.domain.entity.Order;
import com.sg.ecomarket.order.domain.entity.OrderItem;
import com.sg.ecomarket.order.domain.repository.OrderRepository;
import com.sg.ecomarket.order.infra.dataobject.OrderDO;
import com.sg.ecomarket.order.infra.dataobject.OrderItemDO;
import com.sg.ecomarket.order.infra.mapper.OrderItemMapper;
import com.sg.ecomarket.order.infra.mapper.OrderMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单仓库实现
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order save(Order order) {
        OrderDO orderDO = toOrderDO(order);
        if (orderDO.getId() == null) {
            orderMapper.insert(orderDO);
        } else {
            orderMapper.updateById(orderDO);
            // 删除旧的订单项
            LambdaQueryWrapper<OrderItemDO> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(OrderItemDO::getOrderId, orderDO.getId());
            orderItemMapper.delete(deleteWrapper);
        }

        // 保存订单项
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            for (OrderItem item : order.getOrderItems()) {
                OrderItemDO itemDO = toOrderItemDO(item);
                itemDO.setOrderId(orderDO.getId());
                orderItemMapper.insert(itemDO);
            }
        }

        return toOrder(orderDO, order.getOrderItems());
    }

    @Override
    public Order findById(Long id) {
        OrderDO orderDO = orderMapper.selectById(id);
        if (orderDO == null) {
            return null;
        }
        List<OrderItemDO> itemDOList = getOrderItemsByOrderId(id);
        return toOrder(orderDO, toOrderItemList(itemDOList));
    }

    @Override
    public Order findByOrderNo(String orderNo) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getOrderNo, orderNo);
        OrderDO orderDO = orderMapper.selectOne(wrapper);
        if (orderDO == null) {
            return null;
        }
        List<OrderItemDO> itemDOList = getOrderItemsByOrderId(orderDO.getId());
        return toOrder(orderDO, toOrderItemList(itemDOList));
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getUserId, userId);
        wrapper.orderByDesc(OrderDO::getCreatedAt);
        List<OrderDO> orderDOList = orderMapper.selectList(wrapper);

        List<Order> orders = new ArrayList<>();
        for (OrderDO orderDO : orderDOList) {
            List<OrderItemDO> itemDOList = getOrderItemsByOrderId(orderDO.getId());
            orders.add(toOrder(orderDO, toOrderItemList(itemDOList)));
        }
        return orders;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        LambdaUpdateWrapper<OrderDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(OrderDO::getId, id);
        wrapper.set(OrderDO::getStatus, status);
        wrapper.set(OrderDO::getUpdatedAt, new java.util.Date());
        return orderMapper.update(null, wrapper) > 0;
    }

    private List<OrderItemDO> getOrderItemsByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItemDO::getOrderId, orderId);
        return orderItemMapper.selectList(wrapper);
    }

    private OrderDO toOrderDO(Order order) {
        if (order == null) {
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(order, orderDO);
        return orderDO;
    }

    private OrderItemDO toOrderItemDO(OrderItem item) {
        if (item == null) {
            return null;
        }
        OrderItemDO itemDO = new OrderItemDO();
        BeanUtils.copyProperties(item, itemDO);
        return itemDO;
    }

    private Order toOrder(OrderDO orderDO, List<OrderItem> orderItems) {
        if (orderDO == null) {
            return null;
        }
        Order order = new Order();
        BeanUtils.copyProperties(orderDO, order);
        order.setOrderItems(orderItems);
        return order;
    }

    private List<OrderItem> toOrderItemList(List<OrderItemDO> itemDOList) {
        if (itemDOList == null || itemDOList.isEmpty()) {
            return new ArrayList<>();
        }
        return itemDOList.stream().map(this::toOrderItem).collect(Collectors.toList());
    }

    private OrderItem toOrderItem(OrderItemDO itemDO) {
        if (itemDO == null) {
            return null;
        }
        OrderItem item = new OrderItem();
        BeanUtils.copyProperties(itemDO, item);
        return item;
    }
}
