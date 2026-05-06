package com.sg.ecomarket.order.client;

import com.sg.ecomarket.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    @GetMapping("/api/product/{id}")
    Result<ProductDTO> getById(@PathVariable("id") Long id);

    @PostMapping("/api/product/{id}/stock/deduct")
    Result<Boolean> deductStock(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity);

    @lombok.Data
    class ProductDTO {
        private Long id;
        private String name;
        private BigDecimal price;
        private Integer stock;
    }
}
