package com.sg.ecomarket.order.adapter.controller;

import com.sg.ecomarket.common.exception.BizException;
import com.sg.ecomarket.common.result.Result;
import com.sg.ecomarket.order.client.ProductServiceClient;
import com.sg.ecomarket.order.app.cmd.CartItemCmd;
import com.sg.ecomarket.order.app.dto.CartItemDTO;
import com.sg.ecomarket.order.app.service.CartService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @Resource
    private ProductServiceClient productServiceClient;

    /**
     * 添加商品到购物车
     */
    @PostMapping
    public Result<Void> addToCart(@Validated @RequestBody CartItemCmd cmd) {
        // 验证商品
        Result<ProductServiceClient.ProductDTO> productResult = productServiceClient.getById(cmd.getProductId());
        if (productResult == null || productResult.getData() == null) {
            throw new BizException("商品不存在");
        }

        ProductServiceClient.ProductDTO product = productResult.getData();
        cartService.addToCart(cmd.getUserId(), cmd.getProductId(), product.getName(), product.getPrice(), cmd.getQuantity());
        return Result.success();
    }

    /**
     * 更新购物车商品数量
     */
    @PutMapping
    public Result<Void> updateCartItem(@Validated @RequestBody CartItemCmd cmd) {
        cartService.updateCartItem(cmd.getUserId(), cmd.getProductId(), cmd.getQuantity());
        return Result.success();
    }

    /**
     * 从购物车删除商品
     */
    @DeleteMapping("/{userId}/{productId}")
    public Result<Void> removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        cartService.removeFromCart(userId, productId);
        return Result.success();
    }

    /**
     * 获取用户购物车
     */
    @GetMapping("/{userId}")
    public Result<List<CartItemDTO>> getCart(@PathVariable Long userId) {
        List<CartItemDTO> cartItems = cartService.getCart(userId);
        return Result.success(cartItems);
    }

    /**
     * 清空购物车
     */
    @DeleteMapping("/{userId}")
    public Result<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return Result.success();
    }
}
