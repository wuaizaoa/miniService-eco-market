package com.sg.ecomarket.order.app.cmd;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 购物车操作命令
 */
@Data
public class CartItemCmd {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    private Integer quantity;
}
