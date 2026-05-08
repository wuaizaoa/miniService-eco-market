# 后端管理后台 API 设计文档

## 一、概述

本文档描述了为电商平台添加管理后台 API 的设计方案。采用最小改动原则，在现有代码基础上扩展。

## 二、设计决策

### 2.1 技术选型
- **管理员实现方式**：在现有用户表中添加 role 字段（user/admin）
- **权限验证方式**：在 Service 层手动验证用户角色，不使用拦截器
- **API 路径组织**：登录用独立 `/api/admin/login`，其他管理功能复用现有服务
- **初始化方式**：提供 SQL 脚本手动初始化管理员账号

### 2.2 架构原则
- 保持现有分层架构（Controller → Service → Repository）
- 复用现有代码模式
- 最小化改动范围

## 三、数据库设计

### 3.1 表结构变更

#### 表 `user_user` 变更

新增字段：

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| role | VARCHAR(20) | 用户角色：user/admin | 默认 'user' |

### 3.2 SQL 脚本

```sql
-- 添加角色字段
ALTER TABLE `user_user` ADD COLUMN `role` VARCHAR(20) DEFAULT 'user' COMMENT '用户角色：user/admin' AFTER `status`;

-- 插入默认管理员账号（用户名 admin，密码 admin123）
INSERT INTO `user_user` (`username`, `password`, `email`, `phone`, `status`, `role`, `created_at`, `updated_at`)
VALUES ('admin', '21232f297a57a5a743894a0e4a801fc3', 'admin@example.com', '13800138000', 1, 'admin', NOW(), NOW());
```

密码说明：`admin123` + salt `ecomarket` 的 MD5 值为 `21232f297a57a5a743894a0e4a801fc3`

## 四、API 设计

### 4.1 管理员登录 API

**路径**：`POST /api/admin/login`

**请求体**：
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "admin",
    "token": "jwt-token-here"
  }
}
```

### 4.2 用户管理 API

#### 获取所有用户
**路径**：`GET /api/user/admin/list`

**请求头**：`Authorization: Bearer {token}`

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "username": "user1",
      "email": "user1@example.com",
      "phone": "13800138001",
      "status": 1,
      "role": "user",
      "createdAt": "2026-05-08T10:00:00"
    }
  ]
}
```

#### 创建用户
**路径**：`POST /api/user/admin`

**请求体**：
```json
{
  "username": "newuser",
  "password": "123456",
  "email": "newuser@example.com",
  "phone": "13800138002"
}
```

#### 更新用户
**路径**：`PUT /api/user/admin/{id}`

**请求体**：
```json
{
  "username": "newuser",
  "email": "newuser@example.com",
  "phone": "13800138002",
  "status": 1
}
```

#### 删除用户
**路径**：`DELETE /api/user/admin/{id}`

### 4.3 商品管理 API

#### 获取所有商品
**路径**：`GET /api/product/admin/list`

#### 创建商品
**路径**：`POST /api/product/admin`

**请求体**：
```json
{
  "name": "iPhone 15",
  "description": "全新手机",
  "price": 9999.00,
  "stock": 50,
  "categoryId": 1,
  "image": "http://example.com/image.jpg",
  "status": 1
}
```

#### 更新商品
**路径**：`PUT /api/product/admin/{id}`

#### 删除商品
**路径**：`DELETE /api/product/admin/{id}`

### 4.4 订单管理 API

#### 获取所有订单
**路径**：`GET /api/order/admin/list`

#### 获取订单详情
**路径**：`GET /api/order/admin/{id}`

**响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "orderNo": "ORD202605080001",
    "userId": 1,
    "totalAmount": 9999.00,
    "status": 1,
    "createdAt": "2026-05-08T10:00:00",
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "iPhone 15",
        "productPrice": 9999.00,
        "quantity": 1
      }
    ]
  }
}
```

#### 更新订单状态
**路径**：`PUT /api/order/admin/{id}/status`

**请求体**：
```json
{
  "status": 2
}
```

## 五、权限验证设计

### 5.1 验证流程

1. 管理员调用 `/api/admin/login` 登录，获取 JWT Token
2. 调用管理接口时在请求头携带 Token
3. 在 Service 层进行权限验证：
   - 从 Token 中解析 userId
   - 根据 userId 查询用户信息
   - 检查 `user.getRole().equals("admin")`
   - 不是管理员则抛出异常

### 5.2 权限验证工具方法

在各 Service 中添加统一的权限验证方法：

```java
private void checkAdminPermission(Long userId) {
    User user = userRepository.findById(userId);
    if (user == null || !"admin".equals(user.getRole())) {
        throw new BizException(ErrorCode.ADMIN_REQUIRED);
    }
}
```

## 六、错误码

### 6.1 新增错误码

在 `ErrorCode` 枚举中添加：

| 错误码 | 说明 |
|--------|------|
| ADMIN_REQUIRED | 需要管理员权限 |

## 七、文件变更清单

### 7.1 用户服务
- `User.java` - 添加 role 字段
- `UserDO.java` - 添加 role 字段
- `UserRepository.java` - 添加查询方法
- `UserService.java` - 添加管理方法和权限验证
- `UserController.java` - 添加管理接口
- `AdminController.java` - 新增，管理员登录
- 新增 Cmd/DTO 类

### 7.2 商品服务
- `ProductService.java` - 添加管理方法
- `ProductController.java` - 添加管理接口

### 7.3 订单服务
- `OrderService.java` - 添加管理方法
- `OrderController.java` - 添加管理接口

### 7.4 公共模块
- `ErrorCode.java` - 添加 ADMIN_REQUIRED

## 八、实施顺序

1. 更新数据库（添加 role 字段，插入管理员账号）
2. 更新用户实体和数据对象
3. 实现管理员登录功能
4. 实现用户管理 API
5. 实现商品管理 API
6. 实现订单管理 API
7. 测试所有接口

## 九、注意事项

1. **密码加密**：保持与现有一致（MD5 + salt）
2. **Token 生成**：复用现有的 JwtUtil
3. **角色验证**：所有管理接口都必须在 Service 层验证管理员角色
4. **数据安全**：删除操作需要谨慎，建议添加确认机制
5. **API 文档**：更新现有的 API 文档，添加管理接口说明
