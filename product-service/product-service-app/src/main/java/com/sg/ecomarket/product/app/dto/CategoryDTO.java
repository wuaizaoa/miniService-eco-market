package com.sg.ecomarket.product.app.dto;

import lombok.Data;

import java.util.Date;

/**
 * 分类DTO
 */
@Data
public class CategoryDTO {

    private Long id;

    private String name;

    private Long parentId;

    private Integer sort;

    private Date createdAt;

    private Date updatedAt;
}
