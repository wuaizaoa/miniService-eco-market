package com.sg.ecomarket.product.app.command;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商品更新命令
 */
@Data
public class ProductUpdateCmd {

    @NotNull(message = "商品ID不能为空")
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private Long categoryId;

    private String image;

    private Integer status;
}
