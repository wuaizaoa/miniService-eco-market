package com.sg.ecomarket.product.app.service;

import com.sg.ecomarket.common.enums.ErrorCode;
import com.sg.ecomarket.common.exception.BizException;
import com.sg.ecomarket.product.app.command.ProductCreateCmd;
import com.sg.ecomarket.product.app.command.ProductQueryCmd;
import com.sg.ecomarket.product.app.command.ProductUpdateCmd;
import com.sg.ecomarket.product.app.dto.ProductDTO;
import com.sg.ecomarket.product.domain.entity.Product;
import com.sg.ecomarket.product.domain.repository.CategoryRepository;
import com.sg.ecomarket.product.domain.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务
 */
@Service
public class ProductService {

    @Resource
    private ProductRepository productRepository;

    @Resource
    private CategoryRepository categoryRepository;

    /**
     * 创建商品
     */
    public ProductDTO create(ProductCreateCmd cmd) {
        // 检查分类是否存在
        if (!categoryRepository.existsById(cmd.getCategoryId())) {
            throw new BizException(ErrorCode.NOT_FOUND, "分类不存在");
        }

        Product product = new Product();
        product.setName(cmd.getName());
        product.setDescription(cmd.getDescription());
        product.setPrice(cmd.getPrice());
        product.setStock(cmd.getStock());
        product.setCategoryId(cmd.getCategoryId());
        product.setImage(cmd.getImage());
        product.setStatus(cmd.getStatus() != null ? cmd.getStatus() : 1);
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());

        Product savedProduct = productRepository.save(product);
        return toDTO(savedProduct);
    }

    /**
     * 更新商品
     */
    public ProductDTO update(ProductUpdateCmd cmd) {
        Product product = productRepository.findById(cmd.getId());
        if (product == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "商品不存在");
        }

        if (StringUtils.hasText(cmd.getName())) {
            product.setName(cmd.getName());
        }
        if (StringUtils.hasText(cmd.getDescription())) {
            product.setDescription(cmd.getDescription());
        }
        if (cmd.getPrice() != null) {
            product.setPrice(cmd.getPrice());
        }
        if (cmd.getStock() != null) {
            product.setStock(cmd.getStock());
        }
        if (cmd.getCategoryId() != null) {
            if (!categoryRepository.existsById(cmd.getCategoryId())) {
                throw new BizException(ErrorCode.NOT_FOUND, "分类不存在");
            }
            product.setCategoryId(cmd.getCategoryId());
        }
        if (StringUtils.hasText(cmd.getImage())) {
            product.setImage(cmd.getImage());
        }
        if (cmd.getStatus() != null) {
            product.setStatus(cmd.getStatus());
        }
        product.setUpdatedAt(new Date());

        Product savedProduct = productRepository.save(product);
        return toDTO(savedProduct);
    }

    /**
     * 根据ID查询商品
     */
    public ProductDTO getById(Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "商品不存在");
        }
        return toDTO(product);
    }

    /**
     * 查询所有商品
     */
    public List<ProductDTO> getAll() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * 根据分类ID查询商品
     */
    public List<ProductDTO> getByCategoryId(Long categoryId) {
        List<Product> productList = productRepository.findByCategoryId(categoryId);
        return productList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * 删除商品
     */
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BizException(ErrorCode.NOT_FOUND, "商品不存在");
        }
        productRepository.deleteById(id);
    }

    /**
     * 扣减库存
     */
    public boolean deductStock(Long id, Integer quantity) {
        if (!productRepository.existsById(id)) {
            throw new BizException(ErrorCode.NOT_FOUND, "商品不存在");
        }
        if (quantity <= 0) {
            throw new BizException(ErrorCode.PARAM_ERROR, "扣减数量必须大于0");
        }
        return productRepository.deductStock(id, quantity);
    }

    /**
     * 查询库存
     */
    public Integer getStock(Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "商品不存在");
        }
        return product.getStock();
    }

    /**
     * 管理员查询所有商品
     */
    public List<ProductDTO> adminListAllProducts() {
        return getAll();
    }

    /**
     * 管理员创建商品
     */
    public ProductDTO adminCreateProduct(ProductCreateCmd cmd) {
        return create(cmd);
    }

    /**
     * 管理员更新商品
     */
    public ProductDTO adminUpdateProduct(Long productId, ProductUpdateCmd cmd) {
        cmd.setId(productId);
        return update(cmd);
    }

    /**
     * 管理员删除商品
     */
    public void adminDeleteProduct(Long productId) {
        deleteById(productId);
    }

    private ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }
}
