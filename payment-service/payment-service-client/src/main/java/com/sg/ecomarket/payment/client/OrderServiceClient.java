package com.sg.ecomarket.payment.client;

import com.sg.ecomarket.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service", url = "http://localhost:8083")
public interface OrderServiceClient {

    @PutMapping("/api/order/status")
    Result<Boolean> updateOrderStatus(@RequestBody UpdateOrderStatusCmd cmd);

    @lombok.Data
    class UpdateOrderStatusCmd {
        private Long orderId;
        private Integer status;
    }
}
