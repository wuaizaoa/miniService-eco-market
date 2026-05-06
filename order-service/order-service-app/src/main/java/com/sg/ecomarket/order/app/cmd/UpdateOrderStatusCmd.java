package com.sg.ecomarket.order.app.cmd;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新订单状态命令
 */
@Data
public class UpdateOrderStatusCmd {

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    /**
     * 订单状态：0-待支付，1-已支付，2-已发货，3-已收货，4-已取消
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
}
