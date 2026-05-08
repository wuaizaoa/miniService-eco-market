# Eco-Market Docker 部署实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 Eco-Market 微服务系统完整容器化，实现 Docker Compose 一键部署

**Architecture:** 单主机 Docker Compose 部署，包含 Nacos、Redis 和4个微服务，外部 MySQL 保持不变

**Tech Stack:** Docker, Docker Compose, Spring Boot 2.7, OpenJDK 8

---

## 任务概览

1. 创建目录结构和环境变量文件
2. 编写4个微服务的 Dockerfile
3. 编写 docker-compose.yml 编排文件
4. 为各服务添加 Docker 环境配置文件
5. 编写部署指南文档
6. 验证和测试部署

---

### Task 1: 创建目录结构和 .env 文件

**Files:**
- Create: `d:\coding_study\lesson\miniService\eco-market\.env`
- Create: `d:\coding_study\lesson\miniService\eco-market\docker\` (directory)
- Create: `d:\coding_study\lesson\miniService\eco-market\docker\user-service\`
- Create: `d:\coding_study\lesson\miniService\eco-market\docker\product-service\`
- Create: `d:\coding_study\lesson\miniService\eco-market\docker\order-service\`
- Create: `d:\coding_study\lesson\miniService\eco-market\docker\payment-service\`

- [ ] **Step 1: 创建 .env 环境变量文件**

```env
# Nacos 配置
NACOS_VERSION=2.2.0
NACOS_PORT=8848
NACOS_USERNAME=nacos
NACOS_PASSWORD=nacos

# Redis 配置
REDIS_VERSION=6.2-alpine
REDIS_PORT=6379

# 服务端口
USER_SERVICE_PORT=8081
PRODUCT_SERVICE_PORT=8082
ORDER_SERVICE_PORT=8083
PAYMENT_SERVICE_PORT=8084

# 网络配置
NETWORK_NAME=eco-market-network
```

- [ ] **Step 2: 创建 Docker 目录结构**

在 `eco-market` 目录下创建 `docker` 文件夹，以及各服务的子文件夹。

---

### Task 2: 编写 user-service Dockerfile

**Files:**
- Create: `d:\coding_study\lesson\miniService\eco-market\docker\user-service\Dockerfile`

- [ ] **Step 1: 编写 Dockerfile**

```dockerfile
# 基于 OpenJDK 8 运行时环境
FROM openjdk:8-jre-alpine

# 设置工作目录
WORKDIR /app

# 复制编译好的 jar 包
COPY user-service/user-service-start/target/user-service-start.jar app.jar

# 暴露服务端口
EXPOSE 8081

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

### Task 3: 编写 product-service Dockerfile

**Files:**
- Create: `d:\coding_study\lesson\miniService\eco-market\docker\product-service\Dockerfile`

- [ ] **Step 1: 编写 Dockerfile**

```dockerfile
# 基于 OpenJDK 8 运行时环境
FROM openjdk:8-jre-alpine

# 设置工作目录
WORKDIR /app

# 复制编译好的 jar 包
COPY product-service/product-service-start/target/product-service-start.jar app.jar

# 暴露服务端口
EXPOSE 8082

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

### Task 4: 编写 order-service Dockerfile

**Files:**
- Create: `d:\coding_study\lesson\miniService\eco-market\docker\order-service\Dockerfile`

- [ ] **Step 1: 编写 Dockerfile**

```dockerfile
# 基于 OpenJDK 8 运行时环境
FROM openjdk:8-jre-alpine

# 设置工作目录
WORKDIR /app

# 复制编译好的 jar 包
COPY order-service/order-service-start/target/order-service-start.jar app.jar

# 暴露服务端口
EXPOSE 8083

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

### Task 5: 编写 payment-service Dockerfile

**Files:**
- Create: `d:\coding_study\lesson\miniService\eco-market\docker\payment-service\Dockerfile`

- [ ] **Step 1: 编写 Dockerfile**

```dockerfile
# 基于 OpenJDK 8 运行时环境
FROM openjdk:8-jre-alpine

# 设置工作目录
WORKDIR /app

# 复制编译好的 jar 包
COPY payment-service/payment-service-start/target/payment-service-start.jar app.jar

# 暴露服务端口
EXPOSE 8084

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

### Task 6: 编写 docker-compose.yml

**Files:**
- Create: `d:\coding_study\lesson\miniService\eco-market\docker-compose.yml`

- [ ] **Step 1: 编写完整的 docker-compose.yml**

```yaml
version: '3.8'

# 自定义网络
networks:
  eco-market-network:
    driver: bridge

# 数据卷
volumes:
  nacos-data:
  redis-data:

services:
  # Nacos 服务注册中心
  nacos:
    image: nacos/nacos-server:${NACOS_VERSION:-2.2.0}
    container_name: eco-market-nacos
    environment:
      - MODE=standalone
      - SPRING_DATASOURCE_PLATFORM=mysql
      - MYSQL_SERVICE_HOST=202.168.169.116
      - MYSQL_SERVICE_PORT=35626
      - MYSQL_SERVICE_DB_NAME=lesson
      - MYSQL_SERVICE_USER=sg_user
      - MYSQL_SERVICE_PASSWORD=Bjx250828@!123
      - NACOS_AUTH_ENABLE=true
      - NACOS_AUTH_TOKEN=SecretKey012345678901234567890123456789012345678901234567890123456789
    volumes:
      - nacos-data:/home/nacos/data
    ports:
      - "${NACOS_PORT:-8848}:8848"
    networks:
      - eco-market-network
    healthcheck:
      test: ["CMD", "wget", "--quiet", "--tries=1", "--spider", "http://localhost:8848/nacos/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
    restart: unless-stopped

  # Redis 缓存服务
  redis:
    image: redis:${REDIS_VERSION:-6.2-alpine}
    container_name: eco-market-redis
    volumes:
      - redis-data:/data
    ports:
      - "${REDIS_PORT:-6379}:6379"
    networks:
      - eco-market-network
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 30s
      timeout: 10s
      retries: 3
    restart: unless-stopped

  # 用户服务
  user-service:
    build:
      context: .
      dockerfile: docker/user-service/Dockerfile
    container_name: eco-market-user-service
    ports:
      - "${USER_SERVICE_PORT:-8081}:8081"
    networks:
      - eco-market-network
    depends_on:
      nacos:
        condition: service_healthy
      redis:
        condition: service_healthy
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    restart: unless-stopped

  # 商品服务
  product-service:
    build:
      context: .
      dockerfile: docker/product-service/Dockerfile
    container_name: eco-market-product-service
    ports:
      - "${PRODUCT_SERVICE_PORT:-8082}:8082"
    networks:
      - eco-market-network
    depends_on:
      nacos:
        condition: service_healthy
      redis:
        condition: service_healthy
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    restart: unless-stopped

  # 订单服务
  order-service:
    build:
      context: .
      dockerfile: docker/order-service/Dockerfile
    container_name: eco-market-order-service
    ports:
      - "${ORDER_SERVICE_PORT:-8083}:8083"
    networks:
      - eco-market-network
    depends_on:
      nacos:
        condition: service_healthy
      redis:
        condition: service_healthy
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    restart: unless-stopped

  # 支付服务
  payment-service:
    build:
      context: .
      dockerfile: docker/payment-service/Dockerfile
    container_name: eco-market-payment-service
    ports:
      - "${PAYMENT_SERVICE_PORT:-8084}:8084"
    networks:
      - eco-market-network
    depends_on:
      nacos:
        condition: service_healthy
      redis:
        condition: service_healthy
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    restart: unless-stopped
```

---

### Task 7: 为 user-service 添加 Docker 环境配置

**Files:**
- Create: `d:\coding_study\lesson\miniService\eco-market\user-service\user-service-start\src\main\resources\application-docker.yml`

- [ ] **Step 1: 创建 application-docker.yml**

```yaml
server:
  port: 8081

spring:
  application:
    name: user-service
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://202.168.169.116:35626/lesson?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: sg_user
    password: Bjx250828@!123
  cloud:
    nacos:
      discovery:
        server-addr: nacos:8848
        namespace: public
        group: DEFAULT_GROUP
        username: nacos
        password: nacos

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: classpath*:/mapper/**/*.xml

jwt:
  secret: ecomarket-secret-key-2024-for-jwt-token-generation
  expiration: 86400000
```

---

### Task 8: 为 product-service 添加 Docker 环境配置

**Files:**
- Create: `d:\coding_study\lesson\miniService\eco-market\product-service\product-service-start\src\main\resources\application-docker.yml`

- [ ] **Step 1: 创建 application-docker.yml**

```yaml
server:
  port: 8082

spring:
  application:
    name: product-service
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://202.168.169.116:35626/lesson?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: sg_user
    password: Bjx250828@!123
  cloud:
    nacos:
      discovery:
        server-addr: nacos:8848
        namespace: public
        group: DEFAULT_GROUP
        username: nacos
        password: nacos

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: classpath*:/mapper/**/*.xml
```

---

### Task 9: 为 order-service 添加 Docker 环境配置

**Files:**
- Create: `d:\coding_study\lesson\miniService\eco-market\order-service\order-service-start\src\main\resources\application-docker.yml`

- [ ] **Step 1: 创建 application-docker.yml**

```yaml
server:
  port: 8083

spring:
  application:
    name: order-service
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://202.168.169.116:35626/lesson?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: sg_user
    password: Bjx250828@!123
  cloud:
    nacos:
      discovery:
        server-addr: nacos:8848
        namespace: public
        group: DEFAULT_GROUP
        username: nacos
        password: nacos
  redis:
    host: redis
    port: 6379
    database: 0
    timeout: 10000ms

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: classpath*:/mapper/**/*.xml
```

---

### Task 10: 为 payment-service 添加 Docker 环境配置

**Files:**
- Create: `d:\coding_study\lesson\miniService\eco-market\payment-service\payment-service-start\src\main\resources\application-docker.yml`

- [ ] **Step 1: 创建 application-docker.yml**

```yaml
server:
  port: 8084

spring:
  application:
    name: payment-service
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://202.168.169.116:35626/lesson?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: sg_user
    password: Bjx250828@!123
  cloud:
    nacos:
      discovery:
        server-addr: nacos:8848
        namespace: public
        group: DEFAULT_GROUP
        username: nacos
        password: nacos

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: classpath*:/mapper/**/*.xml
```

---

### Task 11: 编写部署指南文档

**Files:**
- Create: `d:\coding_study\lesson\miniService\eco-market\docs\Docker部署指南.md`

- [ ] **Step 1: 编写部署指南**

```markdown
# Eco-Market Docker 部署指南

## 前置要求

1. Docker 20.x 或更高版本
2. Docker Compose 1.29 或更高版本
3. Maven 3.8.x
4. JDK 8

## 部署步骤

### 1. 编译打包项目

在项目根目录执行：

```bash
cd d:\coding_study\lesson\miniService\eco-market
mvn clean install -DskipTests
```

### 2. 启动所有服务

```bash
docker-compose up -d
```

### 3. 查看服务状态

```bash
docker-compose ps
```

期望输出：所有服务状态为 `Up`

### 4. 查看服务日志

```bash
# 查看所有服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f user-service
```

### 5. 验证部署

#### 5.1 访问 Nacos 控制台

打开浏览器访问：http://localhost:8848/nacos

- 用户名：`nacos`
- 密码：`nacos`

在"服务管理" -> "服务列表"中应该能看到4个微服务。

#### 5.2 测试 API（可选）

使用 Postman 或 curl 测试各个服务的健康端点。

## 常用命令

### 停止服务

```bash
docker-compose down
```

### 重启服务

```bash
docker-compose restart
```

### 重启单个服务

```bash
docker-compose restart user-service
```

### 重新构建并启动

```bash
docker-compose up -d --build
```

### 进入容器

```bash
docker exec -it eco-market-user-service sh
```

## 故障排查

### 问题：服务无法连接到 Nacos

检查：
1. Nacos 容器是否正常运行：`docker ps`
2. 查看 Nacos 日志：`docker-compose logs nacos`
3. 确认网络配置正确

### 问题：端口被占用

解决：
1. 修改 `.env` 文件中的端口配置
2. 或者停止占用端口的进程

### 问题：容器启动失败

查看日志：
```bash
docker-compose logs [服务名]
```

## 清理

### 停止并删除所有容器

```bash
docker-compose down
```

### 停止并删除容器和数据卷

```bash
docker-compose down -v
```

### 删除镜像

```bash
docker rmi eco-market-user-service
docker rmi eco-market-product-service
docker rmi eco-market-order-service
docker rmi eco-market-payment-service
```
```

---

### Task 12: 验证和测试部署

**Files:**
- (No new files, verification only)

- [ ] **Step 1: 编译项目**

```bash
cd d:\coding_study\lesson\miniService\eco-market
mvn clean install -DskipTests
```

- [ ] **Step 2: 启动服务**

```bash
docker-compose up -d
```

- [ ] **Step 3: 等待服务启动**

等待 2-3 分钟让所有服务完全启动。

- [ ] **Step 4: 检查容器状态**

```bash
docker-compose ps
```

Expected: All services show `Up` status.

- [ ] **Step 5: 验证 Nacos 服务注册**

访问 http://localhost:8848/nacos，登录后检查服务列表，确认4个微服务都已注册。

- [ ] **Step 6: 验证功能（可选）**

通过前端或 API 测试工具验证：
- 用户注册/登录
- 商品浏览
- 购物车功能
- 下单支付流程

---

## 实现完成后的文件结构

```
eco-market/
├── .env
├── docker-compose.yml
├── docker/
│   ├── user-service/
│   │   └── Dockerfile
│   ├── product-service/
│   │   └── Dockerfile
│   ├── order-service/
│   │   └── Dockerfile
│   └── payment-service/
│       └── Dockerfile
├── user-service/
│   └── user-service-start/
│       └── src/main/resources/
│           └── application-docker.yml
├── product-service/
│   └── product-service-start/
│       └── src/main/resources/
│           └── application-docker.yml
├── order-service/
│   └── order-service-start/
│       └── src/main/resources/
│           └── application-docker.yml
├── payment-service/
│   └── payment-service-start/
│       └── src/main/resources/
│           └── application-docker.yml
└── docs/
    ├── Docker部署设计.md
    └── Docker部署指南.md
```
