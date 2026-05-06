package com.sg.ecomarket.order.domain.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单项领域实体
 */
@Data
public class OrderItem {

    /**
     * 订单项ID
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品价格
     */
    private BigDecimal productPrice;

    /**
     * 商品数量
     */
    private Integer quantity;
}
