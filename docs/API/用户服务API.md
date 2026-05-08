# 用户服务 API

**服务地址**: http://localhost:8081  
**基础路径**: /api/user  
**版本**: v1.0  
**最后更新**: 2026-05-08  

## 目录

1. [用户注册](#用户注册)
2. [用户登录](#用户登录)
3. [根据ID查询用户](#根据id查询用户)

---

## 用户注册

### 接口说明

创建新用户账号。

### 接口信息

| 项目 | 内容 |
|------|------|
| **HTTP方法** | POST |
| **接口路径** | /api/user/register |
| **是否需要认证** | 否 |

### 请求参数

**请求体 (application/json)**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |
| email | String | 否 | 邮箱 |
| phone | String | 否 | 手机号 |

### 请求示例

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

### 响应示例

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

## 用户登录

### 接口说明

用户登录认证。

### 接口信息

| 项目 | 内容 |
|------|------|
| **HTTP方法** | POST |
| **接口路径** | /api/user/login |
| **是否需要认证** | 否 |

### 请求参数

**请求体 (application/json)**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

### 请求示例

```bash
curl -X POST http://localhost:8081/api/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456"
  }'
```

### 响应示例

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

## 根据ID查询用户

### 接口说明

根据用户ID获取用户详细信息。

### 接口信息

| 项目 | 内容 |
|------|------|
| **HTTP方法** | GET |
| **接口路径** | /api/user/{id} |
| **是否需要认证** | 否 |

### 请求参数

**路径参数**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

### 请求示例

```bash
curl -X GET http://localhost:8081/api/user/1
```

### 响应示例

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

## 相关文档

- [README.md](./README.md) - API文档总览
- [通用规范.md](./通用规范.md) - 通用API规范
- [商品服务API.md](./商品服务API.md)
- [订单服务API.md](./订单服务API.md)
- [支付服务API.md](./支付服务API.md)
- [接口调用示例.md](./接口调用示例.md)

---

**文档结束**
