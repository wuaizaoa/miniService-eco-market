# Eco-Market Docker 部署设计文档

## 一、项目概述

### 1.1 设计目标
将 Eco-Market 微服务电商系统完整地容器化部署，实现一键启动所有服务，满足实训课程答辩演示要求。

### 1.2 架构选择
采用**方案一：单主机 Docker Compose 全栈部署**

## 二、整体架构设计

### 2.1 系统拓扑图

```
┌─────────────────────────────────────────────────────────────────┐
│                     Docker Host (本地机器)                       │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │           Docker Network: eco-market-network               │ │
│  │                                                           │ │
│  │  ┌──────────────┐  ┌──────────────┐  ┌─────────────────┐ │ │
│  │  │   Nacos      │  │    Redis     │  │  user-service   │ │ │
│  │  │   (8848)     │  │    (6379)    │  │    (8081)       │ │ │
│  │  └──────────────┘  └──────────────┘  └─────────────────┘ │ │
│  │                                                           │ │
│  │  ┌─────────────────┐  ┌─────────────────┐                  │ │
│  │  │ product-service │  │  order-service  │                  │ │
│  │  │    (8082)       │  │    (8083)       │                  │ │
│  │  └─────────────────┘  └─────────────────┘                  │ │
│  │                                                           │ │
│  │  ┌─────────────────┐                                       │ │
│  │  │payment-service  │                                       │ │
│  │  │    (8084)       │                                       │ │
│  │  └─────────────────┘                                       │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
                    ┌─────────────────┐
                    │  外部 MySQL     │
                    │  (202.168...)   │
                    └─────────────────┘
```

### 2.2 服务清单

| 服务名称 | 端口 | 镜像来源 | 说明 |
|---------|------|---------|------|
| Nacos | 8848 | nacos/nacos-server:2.2.0 | 服务注册中心 |
| Redis | 6379 | redis:6.2-alpine | 缓存服务 |
| user-service | 8081 | 自定义构建 | 用户服务 |
| product-service | 8082 | 自定义构建 | 商品服务 |
| order-service | 8083 | 自定义构建 | 订单服务 |
| payment-service | 8084 | 自定义构建 | 支付服务 |

### 2.3 外部依赖
- MySQL 数据库：`202.168.169.116:35626`（保持不变）

## 三、文件结构设计

```
eco-market/
├── docker-compose.yml          # 主编排文件
├── .env                        # 环境变量配置
├── docker/
│   ├── user-service/
│   │   └── Dockerfile
│   ├── product-service/
│   │   └── Dockerfile
│   ├── order-service/
│   │   └── Dockerfile
│   └── payment-service/
│       └── Dockerfile
└── docs/
    └── Docker部署指南.md       # 部署操作手册
```

## 四、Dockerfile 设计

### 4.1 通用 Dockerfile 模板

所有微服务使用相同结构的 Dockerfile：

```dockerfile
# 基于 OpenJDK 8 运行时环境
FROM openjdk:8-jre-alpine

# 设置工作目录
WORKDIR /app

# 复制编译好的 jar 包
COPY target/*-start.jar app.jar

# 暴露服务端口
EXPOSE 808x

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:808x/actuator/health || exit 1

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 4.2 各服务 Dockerfile
- user-service：EXPOSE 8081
- product-service：EXPOSE 8082
- order-service：EXPOSE 8083
- payment-service：EXPOSE 8084

## 五、Docker Compose 设计

### 5.1 核心配置要点

1. **自定义网络**：创建 `eco-market-network` 桥接网络
2. **服务依赖**：微服务依赖 Nacos 和 Redis 启动
3. **健康检查**：所有服务都配置健康检查
4. **端口映射**：将容器端口映射到主机
5. **重启策略**：配置 `unless-stopped` 自动重启

### 5.2 服务启动顺序

```
Nacos → Redis → user-service, product-service, order-service, payment-service
```

## 六、配置文件调整

### 6.1 需要修改的配置

将各服务 `application.yml` 中的服务地址改为 Docker 服务名：

**原配置：**
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
  redis:
    host: localhost
    port: 6379
```

**Docker 环境配置：**
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: nacos:8848
  redis:
    host: redis
    port: 6379
```

### 6.2 配置方案
使用 Spring Profile 实现多环境配置：
- `application.yml`：基础配置
- `application-docker.yml`：Docker 环境专用配置

## 七、部署流程

### 7.1 前置准备
1. 确保 Docker 和 Docker Compose 已安装
2. 确保外部 MySQL 可访问

### 7.2 部署步骤

```bash
# 1. 编译打包所有服务
mvn clean install -DskipTests

# 2. 启动所有服务
docker-compose up -d

# 3. 查看服务状态
docker-compose ps

# 4. 查看日志
docker-compose logs -f
```

### 7.3 验证清单

部署完成后验证以下项目：

- [ ] 所有容器状态为 `Up`
- [ ] Nacos 控制台可访问（http://localhost:8848/nacos）
- [ ] 4个微服务都已注册到 Nacos
- [ ] Redis 连接正常
- [ ] 前端可以正常调用 API
- [ ] 用户注册/登录功能正常
- [ ] 商品浏览功能正常
- [ ] 购物车功能正常
- [ ] 下单支付流程正常

## 八、运维说明

### 8.1 常用命令

```bash
# 启动服务
docker-compose up -d

# 停止服务
docker-compose down

# 重启服务
docker-compose restart

# 查看日志
docker-compose logs -f [服务名]

# 进入容器
docker exec -it [容器名] sh
```

### 8.2 数据持久化

- Nacos：使用 Docker 卷持久化配置
- Redis：使用 Docker 卷持久化数据

## 九、风险与应对

| 风险 | 影响 | 应对措施 |
|------|------|---------|
| 端口被占用 | 服务无法启动 | 修改端口映射或清理占用进程 |
| 内存不足 | 容器被 OOM kill | 调整 JVM 参数或增加主机内存 |
| 网络问题 | 服务间通信失败 | 检查 Docker 网络配置 |
