package com.sg.ecomarket.payment.adapter.controller;

import com.sg.ecomarket.common.result.Result;
import com.sg.ecomarket.payment.app.cmd.CreatePaymentCmd;
import com.sg.ecomarket.payment.app.dto.PaymentDTO;
import com.sg.ecomarket.payment.app.service.PaymentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 支付控制器
 */
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    /**
     * 创建支付记录
     */
    @PostMapping
    public Result<PaymentDTO> createPayment(@Validated @RequestBody CreatePaymentCmd cmd) {
        PaymentDTO paymentDTO = paymentService.createPayment(cmd);
        return Result.success(paymentDTO);
    }

    /**
     * Mock支付（直接成功）
     */
    @PostMapping("/{id}/pay")
    public Result<PaymentDTO> mockPay(@PathVariable("id") Long id) {
        PaymentDTO paymentDTO = paymentService.mockPay(id);
        return Result.success(paymentDTO);
    }

    /**
     * 根据ID查询支付记录
     */
    @GetMapping("/{id}")
    public Result<PaymentDTO> getPaymentById(@PathVariable("id") Long id) {
        PaymentDTO paymentDTO = paymentService.getPaymentById(id);
        return Result.success(paymentDTO);
    }

    /**
     * 根据订单号查询支付记录
     */
    @GetMapping("/order/{orderNo}")
    public Result<PaymentDTO> getPaymentByOrderNo(@PathVariable("orderNo") String orderNo) {
        PaymentDTO paymentDTO = paymentService.getPaymentByOrderNo(orderNo);
        return Result.success(paymentDTO);
    }
}
