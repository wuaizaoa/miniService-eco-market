package com.sg.ecomarket.product.app.command;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 分类创建命令
 */
@Data
public class CategoryCreateCmd {

    @NotBlank(message = "分类名称不能为空")
    private String name;

    private Long parentId;

    private Integer sort;
}
