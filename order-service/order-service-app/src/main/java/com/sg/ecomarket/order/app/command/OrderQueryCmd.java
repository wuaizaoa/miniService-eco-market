package com.sg.ecomarket.order.app.command;

import lombok.Data;

/**
 * 订单查询命令
 */
@Data
public class OrderQueryCmd {

    private Long userId;

    private Integer status;
}
