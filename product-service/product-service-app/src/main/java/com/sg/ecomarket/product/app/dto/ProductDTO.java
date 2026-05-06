package com.sg.ecomarket.product.app.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品DTO
 */
@Data
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private Long categoryId;

    private String image;

    private Integer status;

    private Date createdAt;

    private Date updatedAt;
}
