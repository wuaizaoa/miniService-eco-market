package com.sg.ecomarket.payment.app.cmd;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新支付状态命令
 */
@Data
public class UpdatePaymentStatusCmd {

    /**
     * 支付ID
     */
    @NotNull(message = "支付ID不能为空")
    private Long paymentId;

    /**
     * 状态：0-待支付，1-已支付
     */
    @NotNull(message = "状态不能为空")
    private Integer status;

    /**
     * 第三方支付流水号（模拟）
     */
    private String thirdPartyNo;
}
