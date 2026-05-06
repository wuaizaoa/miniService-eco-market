package com.sg.ecomarket.payment.domain.repository;

import com.sg.ecomarket.payment.domain.entity.Payment;

/**
 * 支付仓库接口
 */
public interface PaymentRepository {

    /**
     * 保存支付记录
     */
    Payment save(Payment payment);

    /**
     * 根据ID查询支付记录
     */
    Payment findById(Long id);

    /**
     * 根据订单ID查询支付记录
     */
    Payment findByOrderId(Long orderId);

    /**
     * 根据订单号查询支付记录
     */
    Payment findByOrderNo(String orderNo);
}
