# EcoMarket 微服务电商平台 API 文档

**版本**: v1.0  
**最后更新**: 2026-05-08  
**基础URL**: http://localhost:8080  

---

## 目录

1. [API基本信息](#api基本信息)
2. [认证与授权机制](#认证与授权机制)
3. [请求与响应格式](#请求与响应格式)
4. [错误码说明](#错误码说明)
5. [用户服务API](#用户服务api)
6. [商品服务API](#商品服务api)
7. [订单服务API](#订单服务api)
8. [支付服务API](#支付服务api)
9. [接口调用示例](#接口调用示例)
10. [限制条件与最佳实践](#限制条件与最佳实践)

---

## API基本信息

### 项目概述

EcoMarket 是一个基于 Spring Cloud Alibaba 的微服务电商平台，采用前后端分离架构，提供用户管理、商品管理、订单管理、支付管理等核心功能。

### 服务架构

| 服务名称 | 端口 | 功能说明 |
|---------|------|---------|
| user-service | 8081 | 用户注册、登录、信息查询 |
| product-service | 8082 | 商品管理、分类管理、库存管理 |
| order-service | 8083 | 订单创建、查询、购物车管理 |
| payment-service | 8084 | 支付记录创建、支付操作 |

### 技术栈

- **后端**: Spring Boot 2.7.x, Spring Cloud Alibaba
- **数据库**: MySQL 8.0
- **缓存**: Redis 6.x
- **服务注册**: Nacos
- **数据传输**: JSON

---

## 认证与授权机制

### 当前认证方式

**注意**: 当前版本采用简化的认证机制，生产环境建议使用 JWT 或 OAuth2。

1. 用户登录后获得用户ID
2. 后续请求在请求参数或请求体中携带用户ID
3. Token字段目前为模拟实现

### 建议的认证流程（生产环境）

```
1. 用户登录 → 获取 JWT Token
2. 后续请求在 Header 中携带 Token
   Authorization: Bearer {token}
3. 服务端验证 Token 有效性
```

---

## 请求与响应格式

### 请求格式

- **Content-Type**: application/json
- **字符编码**: UTF-8

### 通用响应格式

所有API接口统一使用以下响应格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    // 具体业务数据
  }
}
```

#### 响应字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| code | Integer | 是 | 状态码，200表示成功 |
| message | String | 是 | 响应消息 |
| data | Object/Array | 否 | 响应数据，根据接口返回 |

### HTTP状态码

| 状态码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 错误码说明

### 业务错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 错误响应示例

```json
{
  "code": 500,
  "message": "用户名已存在",
  "data": null
}
```

---

## 用户服务API

**服务地址**: http://localhost:8081  
**基础路径**: /api/user

### 1. 用户注册

#### 接口说明

创建新用户账号。

#### 接口信息

| 项目 | 内容 |
|------|------|
| **HTTP方法** | POST |
| **接口路径** | /api/user/register |
| **是否需要认证** | 否 |

#### 请求参数

**请求体 (application/json)**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |
| email | String | 否 | 邮箱 |
| phone | String | 否 | 手机号 |

#### 请求示例

```bash
curl -X POST http://localhost:8081/api/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com",
    "phone": "13800138000"
  }'
```

#### 响应示例

**成功响应 (200)**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "phone": "13800138000",
    "avatar": null,
    "status": 1,
    "createdAt": "2026-05-08T10:00:00.000+00:00",
    "updatedAt": "2026-05-08T10:00:00.000+00:00"
  }
}
```

**失败响应 (500)**

```json
{
  "code": 500,
  "message": "用户名已存在",
  "data": null
}
```

---

### 2. 用户登录

#### 接口说明

用户登录认证。

#### 接口信息

| 项目 | 内容 |
|------|------|
| **HTTP方法** | POST |
| **接口路径** | /api/user/login |
| **是否需要认证** | 否 |

#### 请求参数

**请求体 (application/json)**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

#### 请求示例

```bash
curl -X POST http://localhost:8081/api/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456"
  }'
```

#### 响应示例

**成功响应 (200)**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "token": "mock-token-xxx"
  }
}
```

**失败响应 (500)**

```json
{
  "code": 500,
  "message": "用户名或密码错误",
  "data": null
}
```

---

### 3. 根据ID查询用户

#### 接口说明

根据用户ID获取用户详细信息。

#### 接口信息

| 项目 | 内容 |
|------|------|
| **HTTP方法** | GET |
| **接口路径** | /api/user/{id} |
| **是否需要认证** | 否 |

#### 请求参数

**路径参数**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

#### 请求示例

```bash
curl -X GET http://localhost:8081/api/user/1
```

#### 响应示例

**成功响应 (200)**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "phone": "13800138000",
    "avatar": null,
    "status": 1,
    "createdAt": "2026-05-08T10:00:00.000+00:00",
    "updatedAt": "2026-05-08T10:00:00.000+00:00"
  }
}
```

---

## 商品服务API

**服务地址**: http://localhost:8082  
**基础路径**: /api/product

### 商品管理API

#### 1. 创建商品

**接口路径**: `POST /api/product`

**请求体**:
```json
{
  "name": "iPhone 15 Pro Max",
  "description": "全新钛金属设计，A17 Pro芯片",
  "price": 9999.00,
  "stock": 50,
  "categoryId": 1,
  "image": "http://example.com/image.jpg",
  "status": 1
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "iPhone 15 Pro Max",
    "description": "全新钛金属设计，A17 Pro芯片",
    "price": 9999.00,
    "stock": 50,
    "categoryId": 1,
    "image": "http://example.com/image.jpg",
    "status": 1,
    "createdAt": "2026-05-08T10:00:00.000+00:00",
    "updatedAt": "2026-05-08T10:00:00.000+00:00"
  }
}
```

---

#### 2. 更新商品

**接口路径**: `PUT /api/product`

**请求体**:
```json
{
  "id": 1,
  "name": "iPhone 15 Pro Max 256GB",
  "price": 9599.00,
  "stock": 45
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "iPhone 15 Pro Max 256GB",
    "description": "全新钛金属设计，A17 Pro芯片",
    "price": 9599.00,
    "stock": 45,
    "categoryId": 1,
    "image": "http://example.com/image.jpg",
    "status": 1,
    "createdAt": "2026-05-08T10:00:00.000+00:00",
    "updatedAt": "2026-05-08T11:00:00.000+00:00"
  }
}
```

---

#### 3. 根据ID查询商品

**接口路径**: `GET /api/product/{id}`

**路径参数**:
- `id`: 商品ID

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "iPhone 15 Pro Max 256GB",
    "description": "全新钛金属设计，A17 Pro芯片",
    "price": 9999.00,
    "stock": 50,
    "categoryId": 1,
    "image": "http://example.com/image.jpg",
    "status": 1,
    "createdAt": "2026-05-08T10:00:00.000+00:00",
    "updatedAt": "2026-05-08T10:00:00.000+00:00"
  }
}
```

---

#### 4. 查询所有商品

**接口路径**: `GET /api/product`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "iPhone 15 Pro Max 256GB",
      "description": "全新钛金属设计，A17 Pro芯片",
      "price": 9999.00,
      "stock": 50,
      "categoryId": 1,
      "image": "http://example.com/image.jpg",
      "status": 1,
      "createdAt": "2026-05-08T10:00:00.000+00:00",
      "updatedAt": "2026-05-08T10:00:00.000+00:00"
    }
  ]
}
```

---

#### 5. 根据分类ID查询商品

**接口路径**: `GET /api/product/by-category/{categoryId}`

**路径参数**:
- `categoryId`: 分类ID

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "iPhone 15 Pro Max 256GB",
      "description": "全新钛金属设计，A17 Pro芯片",
      "price": 9999.00,
      "stock": 50,
      "categoryId": 1,
      "image": "http://example.com/image.jpg",
      "status": 1,
      "createdAt": "2026-05-08T10:00:00.000+00:00",
      "updatedAt": "2026-05-08T10:00:00.000+00:00"
    }
  ]
}
```

---

#### 6. 删除商品

**接口路径**: `DELETE /api/product/{id}`

**路径参数**:
- `id`: 商品ID

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

#### 7. 扣减库存

**接口路径**: `POST /api/product/{id}/stock/deduct`

**路径参数**:
- `id`: 商品ID

**查询参数**:
- `quantity`: 扣减数量

**请求示例**:
```bash
curl -X POST "http://localhost:8082/api/product/1/stock/deduct?quantity=2"
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": true
}
```

---

#### 8. 查询库存

**接口路径**: `GET /api/product/{id}/stock`

**路径参数**:
- `id`: 商品ID

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": 50
}
```

---

### 分类管理API

**基础路径**: /api/product/category

#### 1. 创建分类

**接口路径**: `POST /api/product/category`

**请求体**:
```json
{
  "name": "手机",
  "parentId": 0,
  "sort": 1
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "手机",
    "parentId": 0,
    "sort": 1,
    "createdAt": "2026-05-08T10:00:00.000+00:00",
    "updatedAt": "2026-05-08T10:00:00.000+00:00"
  }
}
```

---

#### 2. 根据ID查询分类

**接口路径**: `GET /api/product/category/{id}`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "手机",
    "parentId": 0,
    "sort": 1,
    "createdAt": "2026-05-08T10:00:00.000+00:00",
    "updatedAt": "2026-05-08T10:00:00.000+00:00"
  }
}
```

---

#### 3. 查询所有分类

**接口路径**: `GET /api/product/category`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "手机",
      "parentId": 0,
      "sort": 1,
      "createdAt": "2026-05-08T10:00:00.000+00:00",
      "updatedAt": "2026-05-08T10:00:00.000+00:00"
    }
  ]
}
```

---

#### 4. 根据父分类ID查询子分类

**接口路径**: `GET /api/product/category/parent/{parentId}`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 2,
      "name": "智能手机",
      "parentId": 1,
      "sort": 1,
      "createdAt": "2026-05-08T10:00:00.000+00:00",
      "updatedAt": "2026-05-08T10:00:00.000+00:00"
    }
  ]
}
```

---

#### 5. 删除分类

**接口路径**: `DELETE /api/product/category/{id}`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

## 订单服务API

**服务地址**: http://localhost:8083

### 购物车管理API

**基础路径**: /api/cart

#### 1. 添加商品到购物车

**接口路径**: `POST /api/cart`

**请求体**:
```json
{
  "userId": 1,
  "productId": 1,
  "quantity": 2
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

#### 2. 更新购物车商品数量

**接口路径**: `PUT /api/cart`

**请求体**:
```json
{
  "userId": 1,
  "productId": 1,
  "quantity": 3
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

#### 3. 从购物车删除商品

**接口路径**: `DELETE /api/cart/{userId}/{productId}`

**路径参数**:
- `userId`: 用户ID
- `productId`: 商品ID

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

#### 4. 获取用户购物车

**接口路径**: `GET /api/cart/{userId}`

**路径参数**:
- `userId`: 用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "productId": 1,
      "productName": "iPhone 15 Pro Max",
      "productPrice": 9999.00,
      "quantity": 2
    }
  ]
}
```

---

#### 5. 清空购物车

**接口路径**: `DELETE /api/cart/{userId}`

**路径参数**:
- `userId`: 用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

### 订单管理API

**基础路径**: /api/order

#### 1. 创建订单

**接口路径**: `POST /api/order`

**请求体**:
```json
{
  "userId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "orderNo": "ORD202605080001",
    "userId": 1,
    "totalAmount": 19998.00,
    "status": 0,
    "payTime": null,
    "createdAt": "2026-05-08T10:00:00.000+00:00",
    "updatedAt": "2026-05-08T10:00:00.000+00:00",
    "orderItems": [
      {
        "id": 1,
        "orderId": 1,
        "productId": 1,
        "productName": "iPhone 15 Pro Max",
        "productPrice": 9999.00,
        "quantity": 2,
        "totalPrice": 19998.00
      }
    ]
  }
}
```

**订单状态说明**:
- 0: 待支付
- 1: 已支付
- 2: 已发货
- 3: 已收货
- 4: 已取消

---

#### 2. 根据ID查询订单

**接口路径**: `GET /api/order/{id}`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "orderNo": "ORD202605080001",
    "userId": 1,
    "totalAmount": 19998.00,
    "status": 0,
    "payTime": null,
    "createdAt": "2026-05-08T10:00:00.000+00:00",
    "updatedAt": "2026-05-08T10:00:00.000+00:00",
    "orderItems": [
      {
        "id": 1,
        "orderId": 1,
        "productId": 1,
        "productName": "iPhone 15 Pro Max",
        "productPrice": 9999.00,
        "quantity": 2,
        "totalPrice": 19998.00
      }
    ]
  }
}
```

---

#### 3. 根据用户ID查询订单列表

**接口路径**: `GET /api/order/user/{userId}`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "orderNo": "ORD202605080001",
      "userId": 1,
      "totalAmount": 19998.00,
      "status": 0,
      "payTime": null,
      "createdAt": "2026-05-08T10:00:00.000+00:00",
      "updatedAt": "2026-05-08T10:00:00.000+00:00",
      "orderItems": [
        {
          "id": 1,
          "orderId": 1,
          "productId": 1,
          "productName": "iPhone 15 Pro Max",
          "productPrice": 9999.00,
          "quantity": 2,
          "totalPrice": 19998.00
        }
      ]
    }
  ]
}
```

---

#### 4. 更新订单状态

**接口路径**: `PUT /api/order/status`

**请求体**:
```json
{
  "orderId": 1,
  "status": 1
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": true
}
```

---

## 支付服务API

**服务地址**: http://localhost:8084  
**基础路径**: /api/payment

### 1. 创建支付记录

**接口路径**: `POST /api/payment`

**请求体**:
```json
{
  "orderId": 1,
  "orderNo": "ORD202605080001",
  "userId": 1,
  "amount": 19998.00,
  "payMethod": "alipay"
}
```

**支付方式说明**:
- `alipay`: 支付宝
- `wechat`: 微信支付

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "orderId": 1,
    "orderNo": "ORD202605080001",
    "userId": 1,
    "amount": 19998.00,
    "payMethod": "alipay",
    "status": 0,
    "thirdPartyNo": null,
    "createdAt": "2026-05-08T10:00:00.000+00:00",
    "updatedAt": "2026-05-08T10:00:00.000+00:00"
  }
}
```

**支付状态说明**:
- 0: 待支付
- 1: 已支付

---

### 2. Mock支付（直接成功）

**接口路径**: `POST /api/payment/{id}/pay`

**路径参数**:
- `id`: 支付记录ID

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "orderId": 1,
    "orderNo": "ORD202605080001",
    "userId": 1,
    "amount": 19998.00,
    "payMethod": "alipay",
    "status": 1,
    "thirdPartyNo": "MOCK202605080001",
    "createdAt": "2026-05-08T10:00:00.000+00:00",
    "updatedAt": "2026-05-08T10:05:00.000+00:00"
  }
}
```

---

### 3. 根据ID查询支付记录

**接口路径**: `GET /api/payment/{id}`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "orderId": 1,
    "orderNo": "ORD202605080001",
    "userId": 1,
    "amount": 19998.00,
    "payMethod": "alipay",
    "status": 1,
    "thirdPartyNo": "MOCK202605080001",
    "createdAt": "2026-05-08T10:00:00.000+00:00",
    "updatedAt": "2026-05-08T10:05:00.000+00:00"
  }
}
```

---

### 4. 根据订单号查询支付记录

**接口路径**: `GET /api/payment/order/{orderNo}`

**路径参数**:
- `orderNo`: 订单号

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "orderId": 1,
    "orderNo": "ORD202605080001",
    "userId": 1,
    "amount": 19998.00,
    "payMethod": "alipay",
    "status": 1,
    "thirdPartyNo": "MOCK202605080001",
    "createdAt": "2026-05-08T10:00:00.000+00:00",
    "updatedAt": "2026-05-08T10:05:00.000+00:00"
  }
}
```

---

## 接口调用示例

### JavaScript (Axios) 示例

```javascript
import axios from 'axios';

// 基础配置
const api = axios.create({
  timeout: 10000
});

// 用户登录
async function login(username, password) {
  const response = await api.post('http://localhost:8081/api/user/login', {
    username,
    password
  });
  return response.data;
}

// 获取商品列表
async function getProducts() {
  const response = await api.get('http://localhost:8082/api/product');
  return response.data;
}

// 创建订单
async function createOrder(userId, items) {
  const response = await api.post('http://localhost:8083/api/order', {
    userId,
    items
  });
  return response.data;
}

// 完整购物流程示例
async function completeShoppingFlow() {
  try {
    // 1. 登录
    const loginResult = await login('testuser', '123456');
    const userId = loginResult.data.id;
    
    // 2. 获取商品
    const productsResult = await getProducts();
    const product = productsResult.data[0];
    
    // 3. 添加到购物车
    await api.post('http://localhost:8083/api/cart', {
      userId,
      productId: product.id,
      quantity: 1
    });
    
    // 4. 创建订单
    const orderResult = await createOrder(userId, [
      { productId: product.id, quantity: 1 }
    ]);
    const order = orderResult.data;
    
    // 5. 创建支付
    const paymentResult = await api.post('http://localhost:8084/api/payment', {
      orderId: order.id,
      orderNo: order.orderNo,
      userId,
      amount: order.totalAmount,
      payMethod: 'alipay'
    });
    
    // 6. 完成支付
    await api.post(`http://localhost:8084/api/payment/${paymentResult.data.id}/pay`);
    
    console.log('购物流程完成！');
  } catch (error) {
    console.error('购物流程失败:', error);
  }
}
```

---

### Python (Requests) 示例

```python
import requests

BASE_URLS = {
    'user': 'http://localhost:8081',
    'product': 'http://localhost:8082',
    'order': 'http://localhost:8083',
    'payment': 'http://localhost:8084'
}

def login(username, password):
    url = f"{BASE_URLS['user']}/api/user/login"
    data = {'username': username, 'password': password}
    response = requests.post(url, json=data)
    return response.json()

def get_products():
    url = f"{BASE_URLS['product']}/api/product"
    response = requests.get(url)
    return response.json()

def create_order(user_id, items):
    url = f"{BASE_URLS['order']}/api/order"
    data = {'userId': user_id, 'items': items}
    response = requests.post(url, json=data)
    return response.json()

def complete_shopping_flow():
    try:
        # 1. 登录
        login_result = login('testuser', '123456')
        user_id = login_result['data']['id']
        
        # 2. 获取商品
        products_result = get_products()
        product = products_result['data'][0]
        
        # 3. 创建订单
        order_result = create_order(user_id, [
            {'productId': product['id'], 'quantity': 1}
        ])
        order = order_result['data']
        
        # 4. 创建支付
        payment_url = f"{BASE_URLS['payment']}/api/payment"
        payment_data = {
            'orderId': order['id'],
            'orderNo': order['orderNo'],
            'userId': user_id,
            'amount': float(order['totalAmount']),
            'payMethod': 'alipay'
        }
        payment_result = requests.post(payment_url, json=payment_data).json()
        
        # 5. 完成支付
        pay_url = f"{BASE_URLS['payment']}/api/payment/{payment_result['data']['id']}/pay"
        requests.post(pay_url)
        
        print('购物流程完成！')
    except Exception as e:
        print(f'购物流程失败: {e}')

if __name__ == '__main__':
    complete_shopping_flow()
```

---

### Java (RestTemplate) 示例

```java
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

public class ApiClient {
    private static final String USER_SERVICE_URL = "http://localhost:8081";
    private static final String PRODUCT_SERVICE_URL = "http://localhost:8082";
    private static final String ORDER_SERVICE_URL = "http://localhost:8083";
    private static final String PAYMENT_SERVICE_URL = "http://localhost:8084";
    
    private RestTemplate restTemplate = new RestTemplate();
    
    public Map<String, Object> login(String username, String password) {
        String url = USER_SERVICE_URL + "/api/user/login";
        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        return response.getBody();
    }
    
    public List<Map<String, Object>> getProducts() {
        String url = PRODUCT_SERVICE_URL + "/api/product";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> body = response.getBody();
        return (List<Map<String, Object>>) body.get("data");
    }
    
    public Map<String, Object> createOrder(Long userId, List<Map<String, Object>> items) {
        String url = ORDER_SERVICE_URL + "/api/order";
        Map<String, Object> request = new HashMap<>();
        request.put("userId", userId);
        request.put("items", items);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        return response.getBody();
    }
}
```

---

## 限制条件与最佳实践

### 限制条件

1. **请求频率限制**
   - 建议单个IP每秒请求不超过100次
   - 超出限制可能触发限流保护

2. **数据大小限制**
   - 请求体大小不超过10MB
   - 单个商品描述不超过5000字符

3. **库存限制**
   - 订单创建时会自动扣减库存
   - 库存不足时订单创建失败

### 最佳实践

1. **错误处理**
   - 始终检查响应的 `code` 字段
   - 对网络错误进行重试（幂等接口）
   - 提供友好的错误提示给用户

2. **性能优化**
   - 合理使用缓存，避免频繁请求相同数据
   - 商品列表等数据建议客户端缓存
   - 使用分页查询（如需要）

3. **安全建议**
   - 生产环境使用HTTPS
   - 敏感信息（密码）不要记录日志
   - 实现完整的JWT认证机制
   - 对用户输入进行严格验证

4. **接口调用建议**
   - 先登录获取用户ID
   - 创建订单前检查商品库存
   - 支付成功后及时更新订单状态
   - 实现订单超时自动取消机制

5. **数据一致性**
   - 订单创建使用分布式事务（如需要）
   - 支付回调实现幂等性
   - 库存操作使用乐观锁或分布式锁

### 测试建议

1. **单元测试**
   - 测试各服务的业务逻辑
   - 验证参数校验逻辑

2. **集成测试**
   - 测试服务间调用
   - 验证完整的购物流程

3. **性能测试**
   - 测试高并发场景下的性能
   - 验证数据库连接池配置

---

## 更新日志

| 版本 | 日期 | 说明 |
|------|------|------|
| v1.0 | 2026-05-08 | 初始版本，完成所有基础API文档 |

---

## 联系方式

如有问题或建议，请联系开发团队。

---

**文档结束**
