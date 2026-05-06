package com.sg.ecomarket.product.app.command;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商品创建命令
 */
@Data
public class ProductCreateCmd {

    @NotBlank(message = "商品名称不能为空")
    private String name;

    private String description;

    @NotNull(message = "商品价格不能为空")
    private BigDecimal price;

    @NotNull(message = "商品库存不能为空")
    private Integer stock;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    private String image;

    private Integer status;
}
