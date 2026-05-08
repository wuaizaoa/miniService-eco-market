package com.sg.ecomarket.order.adapter.controller;

import com.sg.ecomarket.common.result.Result;
import com.sg.ecomarket.order.client.ProductServiceClient;
import com.sg.ecomarket.order.app.cmd.CreateOrderCmd;
import com.sg.ecomarket.order.app.cmd.UpdateOrderStatusCmd;
import com.sg.ecomarket.order.app.dto.OrderDTO;
import com.sg.ecomarket.order.app.service.OrderService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private ProductServiceClient productServiceClient;

    /**
     * 创建订单
     */
    @PostMapping
    public Result<OrderDTO> createOrder(@Validated @RequestBody CreateOrderCmd cmd) {
        OrderDTO orderDTO = orderService.createOrder(cmd);
        return Result.success(orderDTO);
    }

    /**
     * 根据ID查询订单
     */
    @GetMapping("/{id}")
    public Result<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        return Result.success(orderDTO);
    }

    /**
     * 根据用户ID查询订单列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<OrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderDTO> orderDTOList = orderService.getOrdersByUserId(userId);
        return Result.success(orderDTOList);
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/status")
    public Result<Boolean> updateOrderStatus(@Validated @RequestBody UpdateOrderStatusCmd cmd) {
        boolean result = orderService.updateOrderStatus(cmd.getOrderId(), cmd.getStatus());
        return Result.success(result);
    }

    /**
     * 管理员查询所有订单
     */
    @GetMapping("/admin/list")
    public Result<List<OrderDTO>> adminListAllOrders() {
        List<OrderDTO> orderList = orderService.adminListAllOrders();
        return Result.success(orderList);
    }

    /**
     * 管理员查询订单详情
     */
    @GetMapping("/admin/{id}")
    public Result<OrderDTO> adminGetOrderDetail(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.adminGetOrderDetail(id);
        return Result.success(orderDTO);
    }

    /**
     * 管理员更新订单状态
     */
    @PutMapping("/admin/{id}/status")
    public Result<OrderDTO> adminUpdateOrderStatus(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Integer status = request.get("status");
        OrderDTO orderDTO = orderService.adminUpdateOrderStatus(id, status);
        return Result.success(orderDTO);
    }
}
