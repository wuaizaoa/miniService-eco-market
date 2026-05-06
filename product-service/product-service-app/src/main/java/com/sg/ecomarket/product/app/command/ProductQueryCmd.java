package com.sg.ecomarket.product.app.command;

import lombok.Data;

/**
 * 商品查询命令
 */
@Data
public class ProductQueryCmd {

    private Long categoryId;

    private String keyword;

    private Integer status;
}
