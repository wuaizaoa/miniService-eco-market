package com.sg.ecomarket.payment.domain.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付领域实体
 */
@Data
public class Payment {

    /**
     * 支付ID
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 支付方式：alipay/wechat
     */
    private String payMethod;

    /**
     * 状态：0-待支付，1-已支付
     */
    private Integer status;

    /**
     * 第三方支付流水号（模拟）
     */
    private String thirdPartyNo;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;
}
