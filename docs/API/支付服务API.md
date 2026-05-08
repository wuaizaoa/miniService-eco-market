# 支付服务 API

**服务地址**: http://localhost:8084  
**基础路径**: /api/payment  
**版本**: v1.0  
**最后更新**: 2026-05-08  

## 目录

1. [创建支付记录](#创建支付记录)
2. [Mock支付（直接成功）](#mock支付直接成功)
3. [根据ID查询支付记录](#根据id查询支付记录)
4. [根据订单号查询支付记录](#根据订单号查询支付记录)

---

## 创建支付记录

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

## Mock支付（直接成功）

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

## 根据ID查询支付记录

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

## 根据订单号查询支付记录

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

## 相关文档

- [README.md](./README.md) - API文档总览
- [通用规范.md](./通用规范.md) - 通用API规范
- [用户服务API.md](./用户服务API.md)
- [商品服务API.md](./商品服务API.md)
- [订单服务API.md](./订单服务API.md)
- [接口调用示例.md](./接口调用示例.md)

---

**文档结束**
