package com.sg.ecomarket.payment.infra.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sg.ecomarket.payment.domain.entity.Payment;
import com.sg.ecomarket.payment.domain.repository.PaymentRepository;
import com.sg.ecomarket.payment.infra.dataobject.PaymentDO;
import com.sg.ecomarket.payment.infra.mapper.PaymentMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 支付仓库实现
 */
@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    @Resource
    private PaymentMapper paymentMapper;

    @Override
    public Payment save(Payment payment) {
        PaymentDO paymentDO = toDO(payment);
        if (paymentDO.getId() == null) {
            paymentMapper.insert(paymentDO);
        } else {
            paymentMapper.updateById(paymentDO);
        }
        return toEntity(paymentDO);
    }

    @Override
    public Payment findById(Long id) {
        PaymentDO paymentDO = paymentMapper.selectById(id);
        return toEntity(paymentDO);
    }

    @Override
    public Payment findByOrderId(Long orderId) {
        LambdaQueryWrapper<PaymentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentDO::getOrderId, orderId);
        PaymentDO paymentDO = paymentMapper.selectOne(wrapper);
        return toEntity(paymentDO);
    }

    @Override
    public Payment findByOrderNo(String orderNo) {
        LambdaQueryWrapper<PaymentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentDO::getOrderNo, orderNo);
        PaymentDO paymentDO = paymentMapper.selectOne(wrapper);
        return toEntity(paymentDO);
    }

    private PaymentDO toDO(Payment payment) {
        if (payment == null) {
            return null;
        }
        PaymentDO paymentDO = new PaymentDO();
        BeanUtils.copyProperties(payment, paymentDO);
        return paymentDO;
    }

    private Payment toEntity(PaymentDO paymentDO) {
        if (paymentDO == null) {
            return null;
        }
        Payment payment = new Payment();
        BeanUtils.copyProperties(paymentDO, payment);
        return payment;
    }
}
