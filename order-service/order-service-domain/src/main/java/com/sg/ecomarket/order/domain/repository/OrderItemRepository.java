package com.sg.ecomarket.order.domain.repository;

import com.sg.ecomarket.order.domain.entity.OrderItem;

import java.util.List;

/**
 * 订单项仓储接口
 */
public interface OrderItemRepository {

    /**
     * 保存订单项
     */
    OrderItem save(OrderItem orderItem);

    /**
     * 批量保存订单项
     */
    List<OrderItem> saveBatch(List<OrderItem> orderItems);

    /**
     * 根据订单ID查询订单项列表
     */
    List<OrderItem> findByOrderId(Long orderId);
}
