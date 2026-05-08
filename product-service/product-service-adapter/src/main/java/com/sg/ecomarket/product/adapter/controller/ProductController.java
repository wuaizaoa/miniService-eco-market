package com.sg.ecomarket.product.adapter.controller;

import com.sg.ecomarket.common.result.Result;
import com.sg.ecomarket.product.app.command.ProductCreateCmd;
import com.sg.ecomarket.product.app.command.ProductUpdateCmd;
import com.sg.ecomarket.product.app.dto.ProductDTO;
import com.sg.ecomarket.product.app.service.ProductService;
import com.sg.ecomarket.product.app.util.JwtUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 创建商品
     */
    @PostMapping
    public Result<ProductDTO> create(@Validated @RequestBody ProductCreateCmd cmd) {
        ProductDTO productDTO = productService.create(cmd);
        return Result.success(productDTO);
    }

    /**
     * 更新商品
     */
    @PutMapping
    public Result<ProductDTO> update(@Validated @RequestBody ProductUpdateCmd cmd) {
        ProductDTO productDTO = productService.update(cmd);
        return Result.success(productDTO);
    }

    /**
     * 根据ID查询商品
     */
    @GetMapping("/{id}")
    public Result<ProductDTO> getById(@PathVariable Long id) {
        ProductDTO productDTO = productService.getById(id);
        return Result.success(productDTO);
    }

    /**
     * 查询所有商品
     */
    @GetMapping
    public Result<List<ProductDTO>> getAll() {
        List<ProductDTO> productDTOList = productService.getAll();
        return Result.success(productDTOList);
    }

    /**
     * 根据分类ID查询商品
     */
    @GetMapping("/by-category/{categoryId}")
    public Result<List<ProductDTO>> getByCategoryId(@PathVariable Long categoryId) {
        List<ProductDTO> productDTOList = productService.getByCategoryId(categoryId);
        return Result.success(productDTOList);
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Long id) {
        productService.deleteById(id);
        return Result.success();
    }

    /**
     * 扣减库存
     */
    @PostMapping("/{id}/stock/deduct")
    public Result<Boolean> deductStock(@PathVariable Long id, @RequestParam Integer quantity) {
        boolean result = productService.deductStock(id, quantity);
        return Result.success(result);
    }

    /**
     * 查询库存
     */
    @GetMapping("/{id}/stock")
    public Result<Integer> getStock(@PathVariable Long id) {
        Integer stock = productService.getStock(id);
        return Result.success(stock);
    }

    /**
     * 从Authorization头中提取Token
     */
    private Long getAdminUserIdFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        throw new IllegalArgumentException("无效的授权头");
    }

    /**
     * 管理员查询所有商品
     */
    @GetMapping("/admin/list")
    public Result<List<ProductDTO>> adminListAllProducts(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long adminUserId = getAdminUserIdFromToken(authHeader);
        List<ProductDTO> productList = productService.adminListAllProducts(adminUserId);
        return Result.success(productList);
    }

    /**
     * 管理员创建商品
     */
    @PostMapping("/admin")
    public Result<ProductDTO> adminCreateProduct(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                 @Validated @RequestBody ProductCreateCmd cmd) {
        Long adminUserId = getAdminUserIdFromToken(authHeader);
        ProductDTO productDTO = productService.adminCreateProduct(adminUserId, cmd);
        return Result.success(productDTO);
    }

    /**
     * 管理员更新商品
     */
    @PutMapping("/admin/{id}")
    public Result<ProductDTO> adminUpdateProduct(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                 @PathVariable Long id,
                                                 @Validated @RequestBody ProductUpdateCmd cmd) {
        Long adminUserId = getAdminUserIdFromToken(authHeader);
        ProductDTO productDTO = productService.adminUpdateProduct(adminUserId, id, cmd);
        return Result.success(productDTO);
    }

    /**
     * 管理员删除商品
     */
    @DeleteMapping("/admin/{id}")
    public Result<Void> adminDeleteProduct(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                           @PathVariable Long id) {
        Long adminUserId = getAdminUserIdFromToken(authHeader);
        productService.adminDeleteProduct(adminUserId, id);
        return Result.success();
    }
}
