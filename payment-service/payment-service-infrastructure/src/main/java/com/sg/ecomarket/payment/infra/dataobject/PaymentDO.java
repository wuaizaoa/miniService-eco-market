package com.sg.ecomarket.payment.infra.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付数据对象
 */
@Data
@TableName("payment_payment")
public class PaymentDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private String orderNo;

    private Long userId;

    private BigDecimal amount;

    private String payMethod;

    private Integer status;

    private String thirdPartyNo;

    private Date createdAt;

    private Date updatedAt;
}
