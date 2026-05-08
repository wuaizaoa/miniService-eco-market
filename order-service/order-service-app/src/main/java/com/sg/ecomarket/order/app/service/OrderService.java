package com.sg.ecomarket.order.app.service;

import com.sg.ecomarket.common.enums.ErrorCode;
import com.sg.ecomarket.common.exception.BizException;
import com.sg.ecomarket.common.result.Result;
import com.sg.ecomarket.order.client.ProductServiceClient;
import com.sg.ecomarket.order.app.cmd.CreateOrderCmd;
import com.sg.ecomarket.order.app.dto.OrderDTO;
import com.sg.ecomarket.order.app.dto.OrderItemDTO;
import com.sg.ecomarket.order.app.util.JwtUtil;
import com.sg.ecomarket.order.domain.entity.Order;
import com.sg.ecomarket.order.domain.entity.OrderItem;
import com.sg.ecomarket.order.domain.repository.OrderRepository;
import com.sg.ecomarket.order.infra.dataobject.UserDO;
import com.sg.ecomarket.order.infra.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 订单服务
 */
@Service
public class OrderService {

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private ProductServiceClient productServiceClient;

    @Resource
    private UserMapper userMapper;

    /**
     * 创建订单
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderDTO createOrder(CreateOrderCmd cmd) {
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 验证商品并计算总价
        for (CreateOrderCmd.OrderItemCmd itemCmd : cmd.getItems()) {
            Result<ProductServiceClient.ProductDTO> productResult = productServiceClient.getById(itemCmd.getProductId());
            if (productResult == null || productResult.getData() == null) {
                throw new BizException("商品不存在，商品ID: " + itemCmd.getProductId());
            }

            ProductServiceClient.ProductDTO product = productResult.getData();
            if (product.getStock() < itemCmd.getQuantity()) {
                throw new BizException("商品库存不足，商品ID: " + itemCmd.getProductId());
            }

            // 扣减库存
            Result<Boolean> deductResult = productServiceClient.deductStock(itemCmd.getProductId(), itemCmd.getQuantity());
            if (deductResult == null || !Boolean.TRUE.equals(deductResult.getData())) {
                throw new BizException("扣减库存失败，商品ID: " + itemCmd.getProductId());
            }

            // 构建订单项
            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductPrice(product.getPrice());
            item.setQuantity(itemCmd.getQuantity());
            orderItems.add(item);

            // 计算总价
            BigDecimal itemAmount = product.getPrice().multiply(BigDecimal.valueOf(itemCmd.getQuantity()));
            totalAmount = totalAmount.add(itemAmount);
        }

        // 构建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(cmd.getUserId());
        order.setTotalAmount(totalAmount);
        order.setStatus(0); // 待支付
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        order.setOrderItems(orderItems);

        // 保存订单
        Order savedOrder = orderRepository.save(order);
        return toDTO(savedOrder);
    }

    /**
     * 根据ID查询订单
     */
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        return toDTO(order);
    }

    /**
     * 根据用户ID查询订单列表
     */
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * 更新订单状态
     */
    public boolean updateOrderStatus(Long orderId, Integer status) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }

        boolean success = orderRepository.updateStatus(orderId, status);
        if (success && status == 1) { // 已支付
            // 如果是更新为已支付状态，设置支付时间
            order.setStatus(status);
            order.setPayTime(new Date());
            orderRepository.save(order);
        }
        return success;
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 管理员查询所有订单
     */
    public List<OrderDTO> adminListAllOrders(Long adminUserId) {
        checkAdminPermission(adminUserId);
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * 管理员查询订单详情
     */
    public OrderDTO adminGetOrderDetail(Long adminUserId, Long orderId) {
        checkAdminPermission(adminUserId);
        Order order = orderRepository.findWithItemsById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        return toDTO(order);
    }

    /**
     * 管理员更新订单状态
     */
    public OrderDTO adminUpdateOrderStatus(Long adminUserId, Long orderId, Integer status) {
        checkAdminPermission(adminUserId);
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        updateOrderStatus(orderId, status);
        return getOrderById(orderId);
    }

    /**
     * 验证管理员权限
     */
    private void checkAdminPermission(Long userId) {
        UserDO userDO = userMapper.selectById(userId);
        if (userDO == null || !"admin".equals(userDO.getRole())) {
            throw new BizException(ErrorCode.ADMIN_REQUIRED);
        }
    }

    /**
     * 转换为DTO
     */
    private OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }
        OrderDTO dto = new OrderDTO();
        BeanUtils.copyProperties(order, dto);

        if (order.getOrderItems() != null) {
            List<OrderItemDTO> itemDTOs = order.getOrderItems().stream().map(item -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                BeanUtils.copyProperties(item, itemDTO);
                return itemDTO;
            }).collect(Collectors.toList());
            dto.setOrderItems(itemDTOs);
        }

        return dto;
    }
}
