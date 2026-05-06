package com.sg.ecomarket.product.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sg.ecomarket.product.domain.entity.Category;
import com.sg.ecomarket.product.domain.repository.CategoryRepository;
import com.sg.ecomarket.product.infrastructure.dataobject.CategoryDO;
import com.sg.ecomarket.product.infrastructure.mapper.CategoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类仓库实现
 */
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Category save(Category category) {
        CategoryDO categoryDO = toDO(category);
        if (categoryDO.getId() == null) {
            categoryMapper.insert(categoryDO);
        } else {
            categoryMapper.updateById(categoryDO);
        }
        return toEntity(categoryDO);
    }

    @Override
    public Category findById(Long id) {
        CategoryDO categoryDO = categoryMapper.selectById(id);
        return toEntity(categoryDO);
    }

    @Override
    public List<Category> findAll() {
        List<CategoryDO> categoryDOList = categoryMapper.selectList(null);
        return categoryDOList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Category> findByParentId(Long parentId) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getParentId, parentId);
        wrapper.orderByAsc(CategoryDO::getSort);
        List<CategoryDO> categoryDOList = categoryMapper.selectList(wrapper);
        return categoryDOList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return categoryMapper.selectById(id) != null;
    }

    private CategoryDO toDO(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDO categoryDO = new CategoryDO();
        BeanUtils.copyProperties(category, categoryDO);
        return categoryDO;
    }

    private Category toEntity(CategoryDO categoryDO) {
        if (categoryDO == null) {
            return null;
        }
        Category category = new Category();
        BeanUtils.copyProperties(categoryDO, category);
        return category;
    }
}
