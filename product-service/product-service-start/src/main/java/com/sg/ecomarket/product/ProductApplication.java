package com.sg.ecomarket.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 商品服务启动类
 */
@SpringBootApplication(scanBasePackages = "com.sg.ecomarket")
@EnableDiscoveryClient
@MapperScan("com.sg.ecomarket.product.infrastructure.mapper")
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
