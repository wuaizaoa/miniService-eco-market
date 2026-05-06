package com.sg.ecomarket.product.domain.repository;

import com.sg.ecomarket.product.domain.entity.Category;

import java.util.List;

/**
 * 分类仓库接口
 */
public interface CategoryRepository {

    /**
     * 保存分类
     */
    Category save(Category category);

    /**
     * 根据ID查询分类
     */
    Category findById(Long id);

    /**
     * 查询所有分类
     */
    List<Category> findAll();

    /**
     * 根据父分类ID查询子分类
     */
    List<Category> findByParentId(Long parentId);

    /**
     * 删除分类
     */
    void deleteById(Long id);

    /**
     * 检查分类是否存在
     */
    boolean existsById(Long id);
}
