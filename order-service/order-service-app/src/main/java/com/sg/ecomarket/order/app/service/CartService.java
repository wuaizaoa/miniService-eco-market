package com.sg.ecomarket.order.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.ecomarket.order.app.dto.CartItemDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 购物车服务
 */
@Service
public class CartService {

    private static final String CART_KEY_PREFIX = "cart:user:";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 添加商品到购物车
     */
    public void addToCart(Long userId, Long productId, String productName, BigDecimal productPrice, Integer quantity) {
        String key = CART_KEY_PREFIX + userId;
        CartItemDTO existingItem = (CartItemDTO) redisTemplate.opsForHash().get(key, String.valueOf(productId));

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            existingItem = new CartItemDTO(productId, productName, productPrice, quantity);
        }

        redisTemplate.opsForHash().put(key, String.valueOf(productId), existingItem);
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
    }

    /**
     * 更新购物车商品数量
     */
    public void updateCartItem(Long userId, Long productId, Integer quantity) {
        String key = CART_KEY_PREFIX + userId;
        CartItemDTO item = (CartItemDTO) redisTemplate.opsForHash().get(key, String.valueOf(productId));

        if (item != null) {
            if (quantity <= 0) {
                removeFromCart(userId, productId);
            } else {
                item.setQuantity(quantity);
                redisTemplate.opsForHash().put(key, String.valueOf(productId), item);
            }
        }
    }

    /**
     * 从购物车删除商品
     */
    public void removeFromCart(Long userId, Long productId) {
        String key = CART_KEY_PREFIX + userId;
        redisTemplate.opsForHash().delete(key, String.valueOf(productId));
    }

    /**
     * 获取用户购物车
     */
    public List<CartItemDTO> getCart(Long userId) {
        String key = CART_KEY_PREFIX + userId;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        List<CartItemDTO> cartItems = new ArrayList<>();
        for (Object value : entries.values()) {
            cartItems.add(objectMapper.convertValue(value, CartItemDTO.class));
        }
        return cartItems;
    }

    /**
     * 清空购物车
     */
    public void clearCart(Long userId) {
        String key = CART_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }
}
