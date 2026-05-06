package com.sg.ecomarket.order.infra.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单项数据对象
 */
@Data
@TableName("order_order_item")
public class OrderItemDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long productId;

    private String productName;

    private BigDecimal productPrice;

    private Integer quantity;
}
