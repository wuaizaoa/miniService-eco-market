package com.sg.ecomarket.payment.app.service;

import com.sg.ecomarket.common.exception.BizException;
import com.sg.ecomarket.payment.app.cmd.CreatePaymentCmd;
import com.sg.ecomarket.payment.app.dto.PaymentDTO;
import com.sg.ecomarket.payment.client.OrderServiceClient;
import com.sg.ecomarket.payment.domain.entity.Payment;
import com.sg.ecomarket.payment.domain.repository.PaymentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * 支付服务
 */
@Service
public class PaymentService {

    @Resource
    private PaymentRepository paymentRepository;

    @Resource
    private OrderServiceClient orderServiceClient;

    /**
     * 创建支付记录
     */
    public PaymentDTO createPayment(CreatePaymentCmd cmd) {
        // 检查是否已存在支付记录
        Payment existingPayment = paymentRepository.findByOrderId(cmd.getOrderId());
        if (existingPayment != null) {
            throw new BizException("该订单已存在支付记录");
        }

        // 创建支付记录
        Payment payment = new Payment();
        BeanUtils.copyProperties(cmd, payment);
        payment.setStatus(0); // 待支付
        payment.setCreatedAt(new Date());
        payment.setUpdatedAt(new Date());

        Payment savedPayment = paymentRepository.save(payment);
        return toDTO(savedPayment);
    }

    /**
     * Mock 支付（直接成功）
     */
    @Transactional(rollbackFor = Exception.class)
    public PaymentDTO mockPay(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        if (payment == null) {
            throw new BizException("支付记录不存在");
        }

        if (payment.getStatus() == 1) {
            throw new BizException("该订单已支付");
        }

        // Mock支付成功
        payment.setStatus(1);
        payment.setThirdPartyNo("MOCK" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setUpdatedAt(new Date());

        Payment updatedPayment = paymentRepository.save(payment);

        // 调用订单服务更新订单状态
        try {
            OrderServiceClient.UpdateOrderStatusCmd updateCmd = new OrderServiceClient.UpdateOrderStatusCmd();
            updateCmd.setOrderId(payment.getOrderId());
            updateCmd.setStatus(1);
            orderServiceClient.updateOrderStatus(updateCmd);
        } catch (Exception e) {
            throw new BizException("支付成功，但更新订单状态失败：" + e.getMessage());
        }

        return toDTO(updatedPayment);
    }

    /**
     * 根据ID查询支付记录
     */
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id);
        if (payment == null) {
            throw new BizException("支付记录不存在");
        }
        return toDTO(payment);
    }

    /**
     * 根据订单号查询支付记录
     */
    public PaymentDTO getPaymentByOrderNo(String orderNo) {
        Payment payment = paymentRepository.findByOrderNo(orderNo);
        if (payment == null) {
            throw new BizException("支付记录不存在");
        }
        return toDTO(payment);
    }

    /**
     * 转换为DTO
     */
    private PaymentDTO toDTO(Payment payment) {
        if (payment == null) {
            return null;
        }
        PaymentDTO dto = new PaymentDTO();
        BeanUtils.copyProperties(payment, dto);
        return dto;
    }
}
