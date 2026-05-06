package com.sg.ecomarket.order.domain.repository;

import com.sg.ecomarket.order.domain.entity.Order;

import java.util.List;

/**
 * 订单仓库接口
 */
public interface OrderRepository {

    /**
     * 保存订单
     */
    Order save(Order order);

    /**
     * 根据ID查询订单
     */
    Order findById(Long id);

    /**
     * 根据订单编号查询订单
     */
    Order findByOrderNo(String orderNo);

    /**
     * 根据用户ID查询订单列表
     */
    List<Order> findByUserId(Long userId);

    /**
     * 更新订单状态
     */
    boolean updateStatus(Long id, Integer status);
}
