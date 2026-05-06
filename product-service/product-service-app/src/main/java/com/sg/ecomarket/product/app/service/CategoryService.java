package com.sg.ecomarket.product.app.service;

import com.sg.ecomarket.common.enums.ErrorCode;
import com.sg.ecomarket.common.exception.BizException;
import com.sg.ecomarket.product.app.command.CategoryCreateCmd;
import com.sg.ecomarket.product.app.dto.CategoryDTO;
import com.sg.ecomarket.product.domain.entity.Category;
import com.sg.ecomarket.product.domain.repository.CategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类服务
 */
@Service
public class CategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    /**
     * 创建分类
     */
    public CategoryDTO create(CategoryCreateCmd cmd) {
        Category category = new Category();
        category.setName(cmd.getName());
        category.setParentId(cmd.getParentId());
        category.setSort(cmd.getSort() != null ? cmd.getSort() : 0);
        category.setCreatedAt(new Date());
        category.setUpdatedAt(new Date());

        Category savedCategory = categoryRepository.save(category);
        return toDTO(savedCategory);
    }

    /**
     * 根据ID查询分类
     */
    public CategoryDTO getById(Long id) {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "分类不存在");
        }
        return toDTO(category);
    }

    /**
     * 查询所有分类
     */
    public List<CategoryDTO> getAll() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * 根据父分类ID查询子分类
     */
    public List<CategoryDTO> getByParentId(Long parentId) {
        List<Category> categoryList = categoryRepository.findByParentId(parentId);
        return categoryList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * 删除分类
     */
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new BizException(ErrorCode.NOT_FOUND, "分类不存在");
        }
        categoryRepository.deleteById(id);
    }

    private CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, categoryDTO);
        return categoryDTO;
    }
}
