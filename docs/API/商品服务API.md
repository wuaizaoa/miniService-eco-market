# 商品服务 API

**服务地址**: http://localhost:8082  
**基础路径**: /api/product  
**版本**: v1.0  
**最后更新**: 2026-05-08  

## 目录

1. [商品管理API](#商品管理api)
   - [创建商品](#创建商品)
   - [更新商品](#更新商品)
   - [根据ID查询商品](#根据id查询商品)
   - [查询所有商品](#查询所有商品)
   - [根据分类ID查询商品](#根据分类id查询商品)
   - [删除商品](#删除商品)
   - [扣减库存](#扣减库存)
   - [查询库存](#查询库存)
2. [分类管理API](#分类管理api)
   - [创建分类](#创建分类)
   - [根据ID查询分类](#根据id查询分类)
   - [查询所有分类](#查询所有分类)
   - [根据父分类ID查询子分类](#根据父分类id查询子分类)
   - [删除分类](#删除分类)

---

## 商品管理API

### 创建商品

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

### 更新商品

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

### 根据ID查询商品

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

### 查询所有商品

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

### 根据分类ID查询商品

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

### 删除商品

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

### 扣减库存

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

### 查询库存

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

## 分类管理API

### 创建分类

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

### 根据ID查询分类

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

### 查询所有分类

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

### 根据父分类ID查询子分类

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

### 删除分类

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

## 相关文档

- [README.md](./README.md) - API文档总览
- [通用规范.md](./通用规范.md) - 通用API规范
- [用户服务API.md](./用户服务API.md)
- [订单服务API.md](./订单服务API.md)
- [支付服务API.md](./支付服务API.md)
- [接口调用示例.md](./接口调用示例.md)

---

**文档结束**
