# 订单服务 API

**服务地址**: http://localhost:8083  
**版本**: v1.0  
**最后更新**: 2026-05-08  

## 目录

1. [购物车管理API](#购物车管理api)
   - [添加商品到购物车](#添加商品到购物车)
   - [更新购物车商品数量](#更新购物车商品数量)
   - [从购物车删除商品](#从购物车删除商品)
   - [获取用户购物车](#获取用户购物车)
   - [清空购物车](#清空购物车)
2. [订单管理API](#订单管理api)
   - [创建订单](#创建订单)
   - [根据ID查询订单](#根据id查询订单)
   - [根据用户ID查询订单列表](#根据用户id查询订单列表)
   - [更新订单状态](#更新订单状态)

---

## 购物车管理API

### 添加商品到购物车

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

### 更新购物车商品数量

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

### 从购物车删除商品

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

### 获取用户购物车

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

### 清空购物车

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

## 订单管理API

### 创建订单

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

### 根据ID查询订单

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

### 根据用户ID查询订单列表

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

### 更新订单状态

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

## 相关文档

- [README.md](./README.md) - API文档总览
- [通用规范.md](./通用规范.md) - 通用API规范
- [用户服务API.md](./用户服务API.md)
- [商品服务API.md](./商品服务API.md)
- [支付服务API.md](./支付服务API.md)
- [接口调用示例.md](./接口调用示例.md)

---

**文档结束**
