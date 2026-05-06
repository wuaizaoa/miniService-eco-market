package com.sg.ecomarket.product.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sg.ecomarket.product.domain.entity.Product;
import com.sg.ecomarket.product.domain.repository.ProductRepository;
import com.sg.ecomarket.product.infrastructure.dataobject.ProductDO;
import com.sg.ecomarket.product.infrastructure.mapper.ProductMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品仓库实现
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Resource
    private ProductMapper productMapper;

    @Override
    public Product save(Product product) {
        ProductDO productDO = toDO(product);
        if (productDO.getId() == null) {
            productMapper.insert(productDO);
        } else {
            productMapper.updateById(productDO);
        }
        return toEntity(productDO);
    }

    @Override
    public Product findById(Long id) {
        ProductDO productDO = productMapper.selectById(id);
        return toEntity(productDO);
    }

    @Override
    public List<Product> findAll() {
        List<ProductDO> productDOList = productMapper.selectList(null);
        return productDOList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        LambdaQueryWrapper<ProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductDO::getCategoryId, categoryId);
        List<ProductDO> productDOList = productMapper.selectList(wrapper);
        return productDOList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        productMapper.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return productMapper.selectById(id) != null;
    }

    @Override
    public boolean deductStock(Long id, Integer quantity) {
        return productMapper.deductStock(id, quantity) > 0;
    }

    @Override
    public boolean addStock(Long id, Integer quantity) {
        return productMapper.addStock(id, quantity) > 0;
    }

    private ProductDO toDO(Product product) {
        if (product == null) {
            return null;
        }
        ProductDO productDO = new ProductDO();
        BeanUtils.copyProperties(product, productDO);
        return productDO;
    }

    private Product toEntity(ProductDO productDO) {
        if (productDO == null) {
            return null;
        }
        Product product = new Product();
        BeanUtils.copyProperties(productDO, product);
        return product;
    }
}
