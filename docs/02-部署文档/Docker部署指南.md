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
