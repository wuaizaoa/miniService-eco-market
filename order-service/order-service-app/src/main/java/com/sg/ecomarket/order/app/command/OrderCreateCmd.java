package com.sg.ecomarket.order.app.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 订单创建命令
 */
@Data
public class OrderCreateCmd {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotEmpty(message = "购物车商品不能为空")
    private List<OrderItemCmd> items;

    @Data
    public static class OrderItemCmd {
        @NotNull(message = "商品ID不能为空")
        private Long productId;
        @NotNull(message = "数量不能为空")
        private Integer quantity;
    }
}
