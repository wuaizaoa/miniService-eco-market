package com.sg.ecomarket.product.domain.repository;

import com.sg.ecomarket.product.domain.entity.Product;

import java.util.List;

/**
 * 商品仓库接口
 */
public interface ProductRepository {

    /**
     * 保存商品
     */
    Product save(Product product);

    /**
     * 根据ID查询商品
     */
    Product findById(Long id);

    /**
     * 查询所有商品
     */
    List<Product> findAll();

    /**
     * 根据分类ID查询商品
     */
    List<Product> findByCategoryId(Long categoryId);

    /**
     * 删除商品
     */
    void deleteById(Long id);

    /**
     * 检查商品是否存在
     */
    boolean existsById(Long id);

    /**
     * 扣减库存
     */
    boolean deductStock(Long id, Integer quantity);

    /**
     * 增加库存
     */
    boolean addStock(Long id, Integer quantity);
}
