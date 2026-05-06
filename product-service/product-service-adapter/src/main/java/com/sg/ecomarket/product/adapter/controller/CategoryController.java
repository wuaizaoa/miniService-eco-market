package com.sg.ecomarket.product.adapter.controller;

import com.sg.ecomarket.common.result.Result;
import com.sg.ecomarket.product.app.command.CategoryCreateCmd;
import com.sg.ecomarket.product.app.dto.CategoryDTO;
import com.sg.ecomarket.product.app.service.CategoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类控制器
 */
@RestController
@RequestMapping("/api/product/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 创建分类
     */
    @PostMapping
    public Result<CategoryDTO> create(@Validated @RequestBody CategoryCreateCmd cmd) {
        CategoryDTO categoryDTO = categoryService.create(cmd);
        return Result.success(categoryDTO);
    }

    /**
     * 根据ID查询分类
     */
    @GetMapping("/{id}")
    public Result<CategoryDTO> getById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getById(id);
        return Result.success(categoryDTO);
    }

    /**
     * 查询所有分类
     */
    @GetMapping
    public Result<List<CategoryDTO>> getAll() {
        List<CategoryDTO> categoryDTOList = categoryService.getAll();
        return Result.success(categoryDTOList);
    }

    /**
     * 根据父分类ID查询子分类
     */
    @GetMapping("/parent/{parentId}")
    public Result<List<CategoryDTO>> getByParentId(@PathVariable Long parentId) {
        List<CategoryDTO> categoryDTOList = categoryService.getByParentId(parentId);
        return Result.success(categoryDTOList);
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return Result.success();
    }
}
