from jsonschema import validate
from typing import Dict, Any

# Response Schema Definitions
RESULT_SCHEMA = {
    "type": "object",
    "properties": {
        "code": {"type": "integer"},
        "message": {"type": "string"},
        "data": {}
    },
    "required": ["code", "message", "data"]
}

USER_SCHEMA = {
    "type": "object",
    "properties": {
        "id": {"type": "integer"},
        "username": {"type": "string"},
        "email": {"type": ["string", "null"]},
        "phone": {"type": ["string", "null"]},
        "avatar": {"type": ["string", "null"]},
        "status": {"type": "integer"},
        "createdAt": {"type": "string"},
        "updatedAt": {"type": "string"}
    },
    "required": ["id", "username", "status"]
}

LOGIN_SCHEMA = {
    "type": "object",
    "properties": {
        "id": {"type": "integer"},
        "username": {"type": "string"},
        "token": {"type": "string"}
    },
    "required": ["id", "username"]
}

CATEGORY_SCHEMA = {
    "type": "object",
    "properties": {
        "id": {"type": "integer"},
        "name": {"type": "string"},
        "parentId": {"type": ["integer", "null"]},
        "sort": {"type": "integer"},
        "createdAt": {"type": "string"},
        "updatedAt": {"type": "string"}
    },
    "required": ["id", "name"]
}

PRODUCT_SCHEMA = {
    "type": "object",
    "properties": {
        "id": {"type": "integer"},
        "name": {"type": "string"},
        "description": {"type": ["string", "null"]},
        "price": {"type": ["number", "string"]},
        "stock": {"type": "integer"},
        "categoryId": {"type": ["integer", "null"]},
        "image": {"type": ["string", "null"]},
        "status": {"type": "integer"},
        "createdAt": {"type": "string"},
        "updatedAt": {"type": "string"}
    },
    "required": ["id", "name", "price", "stock"]
}

ORDER_SCHEMA = {
    "type": "object",
    "properties": {
        "id": {"type": "integer"},
        "orderNo": {"type": "string"},
        "userId": {"type": "integer"},
        "totalAmount": {"type": ["number", "string"]},
        "status": {"type": "integer"},
        "payTime": {"type": ["string", "null"]},
        "createdAt": {"type": "string"},
        "updatedAt": {"type": "string"},
        "items": {
            "type": "array",
            "items": {
                "type": "object",
                "properties": {
                    "id": {"type": "integer"},
                    "productId": {"type": "integer"},
                    "productName": {"type": "string"},
                    "productPrice": {"type": ["number", "string"]},
                    "quantity": {"type": "integer"},
                    "createdAt": {"type": "string"}
                },
                "required": ["id", "productId", "productName", "productPrice", "quantity"]
            }
        }
    },
    "required": ["id", "orderNo", "userId", "totalAmount", "status"]
}

CART_ITEM_SCHEMA = {
    "type": "object",
    "properties": {
        "productId": {"type": "integer"},
        "productName": {"type": "string"},
        "productPrice": {"type": ["number", "string"]},
        "quantity": {"type": "integer"}
    },
    "required": ["productId", "productName", "productPrice", "quantity"]
}

PAYMENT_SCHEMA = {
    "type": "object",
    "properties": {
        "id": {"type": "integer"},
        "orderId": {"type": "integer"},
        "orderNo": {"type": "string"},
        "userId": {"type": "integer"},
        "amount": {"type": ["number", "string"]},
        "payMethod": {"type": ["string", "null"]},
        "status": {"type": "integer"},
        "thirdPartyNo": {"type": ["string", "null"]},
        "createdAt": {"type": "string"},
        "updatedAt": {"type": "string"}
    },
    "required": ["id", "orderId", "orderNo", "userId", "amount", "status"]
}

def validate_response_schema(response_data: Dict[str, Any], data_schema: Dict = None):
    validate(instance=response_data, schema=RESULT_SCHEMA)
    if data_schema and response_data.get("data"):
        data = response_data["data"]
        if isinstance(data, list):
            for item in data:
                validate(instance=item, schema=data_schema)
        else:
            validate(instance=data, schema=data_schema)
