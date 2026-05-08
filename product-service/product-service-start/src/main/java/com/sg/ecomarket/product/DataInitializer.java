package com.sg.ecomarket.product;

import com.sg.ecomarket.product.domain.entity.Product;
import com.sg.ecomarket.product.domain.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 数据初始化类
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Resource
    private ProductRepository productRepository;

    @Override
    public void run(String... args) {
        // 检查是否已经有数据
        if (productRepository.findAll().isEmpty()) {
            initProducts();
        }
    }

    private void initProducts() {
        // 手机类商品
        createProduct(
                "iPhone 15 Pro Max 256GB",
                "全新钛金属设计，A17 Pro芯片，4800万像素主摄，支持USB 3.0速度传输",
                new BigDecimal("9999.00"),
                50,
                1L,
                "https://images.unsplash.com/photo-1695048133142-1a2046465184?w=400&h=300&fit=crop",
                1
        );

        createProduct(
                "Samsung Galaxy S24 Ultra 512GB",
                "骁龙8 Gen3处理器，2亿像素主摄，1-120Hz自适应刷新率，S Pen手写笔",
                new BigDecimal("8999.00"),
                45,
                1L,
                "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400&h=300&fit=crop",
                1
        );

        createProduct(
                "小米14 Ultra 256GB",
                "徕卡专业光学镜头，骁龙8 Gen3，120W快充，IP68级防水防尘",
                new BigDecimal("6499.00"),
                60,
                1L,
                "https://images.unsplash.com/photo-1592899677977-9c10ca588bbd?w=400&h=300&fit=crop",
                1
        );

        // 笔记本电脑
        createProduct(
                "MacBook Pro 14寸 M3 Pro",
                "M3 Pro芯片，18GB统一内存，512GB SSD，Liquid Retina XDR显示屏",
                new BigDecimal("16999.00"),
                30,
                2L,
                "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400&h=300&fit=crop",
                1
        );

        createProduct(
                "联想拯救者Y9000P 2024",
                "i9-14900HX，RTX4070显卡，16英寸2.5K 240Hz电竞屏",
                new BigDecimal("11999.00"),
                35,
                2L,
                "https://images.unsplash.com/photo-1525547719571-a2d4ac8945e2?w=400&h=300&fit=crop",
                1
        );

        createProduct(
                "华为MateBook X Pro",
                "i7-1360P，16GB，512GB，3.2K触控全面屏，轻薄金属机身",
                new BigDecimal("8999.00"),
                40,
                2L,
                "https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d?w=400&h=300&fit=crop",
                1
        );

        // 耳机音响
        createProduct(
                "AirPods Pro 2代",
                "主动降噪，自适应通透模式，个性化空间音频，USB-C充电盒",
                new BigDecimal("1899.00"),
                100,
                3L,
                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=300&fit=crop",
                1
        );

        createProduct(
                "索尼WH-1000XM5",
                "行业领先的降噪技术，30小时续航，蓝牙5.2，舒适轻盈",
                new BigDecimal("2499.00"),
                70,
                3L,
                "https://images.unsplash.com/photo-1572569511254-d8f925fe2cbb?w=400&h=300&fit=crop",
                1
        );

        createProduct(
                "Bose QuietComfort Ultra",
                "空间音频，沉浸式体验，世界顶级主动降噪，无线蓝牙耳机",
                new BigDecimal("3299.00"),
                55,
                3L,
                "https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=400&h=300&fit=crop",
                1
        );

        // 平板
        createProduct(
                "iPad Pro 12.9寸 M2",
                "M2芯片，Liquid Retina XDR显示屏，Apple Pencil悬停，5G支持",
                new BigDecimal("8999.00"),
                40,
                4L,
                "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400&h=300&fit=crop",
                1
        );

        createProduct(
                "华为MatePad Pro 13.2",
                "鸿蒙4.0系统，13.2寸OLED屏，12GB+512GB，磁吸键盘",
                new BigDecimal("5999.00"),
                50,
                4L,
                "https://images.unsplash.com/photo-1585790050230-5dd28ade0438?w=400&h=300&fit=crop",
                1
        );

        // 智能手表
        createProduct(
                "Apple Watch Series 9",
                "S9芯片，双指互点手势，体温感应，全天候视网膜显示屏",
                new BigDecimal("2999.00"),
                80,
                5L,
                "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=400&h=300&fit=crop",
                1
        );

        createProduct(
                "小米手表S3",
                "蓝牙通话，1.43英寸AMOLED高清大屏，12天超长续航",
                new BigDecimal("799.00"),
                120,
                5L,
                "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&h=300&fit=crop",
                1
        );

        // 相机
        createProduct(
                "Sony A7 IV 全画幅微单",
                "3300万像素全画幅传感器，4K60P视频录制，5轴机身防抖",
                new BigDecimal("16999.00"),
                25,
                6L,
                "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=400&h=300&fit=crop",
                1
        );

        createProduct(
                "Canon EOS R6 Mark II",
                "2420万像素全画幅，30fps连拍，8级防抖，4K60P无裁切",
                new BigDecimal("15999.00"),
                20,
                6L,
                "https://images.unsplash.com/photo-1495745966610-2a67f2297e5e?w=400&h=300&fit=crop",
                1
        );

        // 游戏机
        createProduct(
                "PlayStation 5 (PS5) 光驱版",
                "索尼次世代主机，4K 120fps游戏，DualSense触觉反馈手柄",
                new BigDecimal("3899.00"),
                35,
                7L,
                "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=400&h=300&fit=crop",
                1
        );

        createProduct(
                "任天堂Switch OLED版",
                "7英寸OLED屏幕，64GB存储，可连电视可便携，支持双人游戏",
                new BigDecimal("2299.00"),
                65,
                7L,
                "https://images.unsplash.com/photo-1578303512597-81e6cc155b3e?w=400&h=300&fit=crop",
                1
        );
    }

    private void createProduct(String name, String description, BigDecimal price,
                               Integer stock, Long categoryId, String image, Integer status) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategoryId(categoryId);
        product.setImage(image);
        product.setStatus(status);
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());
        productRepository.save(product);
    }
}
