package com.sg.ecomarket.payment.infra.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus配置类
 */
@Configuration
@MapperScan("com.sg.ecomarket.payment.infra.mapper")
public class MybatisPlusConfig {
}
