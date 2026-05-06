import pytest
import allure
from schemas import validate_response_schema, ORDER_SCHEMA, CART_ITEM_SCHEMA

@allure.feature("订单服务")
@allure.story("购物车管理")
class TestCartService:
    
    @allure.title("测试添加商品到购物车")
    def test_add_to_cart(self, api_client, test_user, test_product):
        response = api_client.add_to_cart(
            user_id=test_user["id"],
            product_id=test_product["id"],
            quantity=2
        )
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
    
    @allure.title("测试获取购物车")
    def test_get_cart(self, api_client, test_user, test_product):
        api_client.add_to_cart(test_user["id"], test_product["id"], 2)
        response = api_client.get_cart(test_user["id"])
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert isinstance(data["data"], list)
        validate_response_schema(data, CART_ITEM_SCHEMA)
    
    @allure.title("测试更新购物车商品数量")
    def test_update_cart_item(self, api_client, test_user, test_product):
        api_client.add_to_cart(test_user["id"], test_product["id"], 1)
        response = api_client.update_cart_item(test_user["id"], test_product["id"], 5)
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        
        cart_response = api_client.get_cart(test_user["id"])
        cart_items = cart_response.json()["data"]
        for item in cart_items:
            if item["productId"] == test_product["id"]:
                assert item["quantity"] == 5
    
    @allure.title("测试从购物车删除商品")
    def test_remove_from_cart(self, api_client, test_user, test_product):
        api_client.add_to_cart(test_user["id"], test_product["id"], 1)
        response = api_client.remove_from_cart(test_user["id"], test_product["id"])
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
    
    @allure.title("测试清空购物车")
    def test_clear_cart(self, api_client, test_user, test_product):
        api_client.add_to_cart(test_user["id"], test_product["id"], 1)
        response = api_client.clear_cart(test_user["id"])
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        
        cart_response = api_client.get_cart(test_user["id"])
        assert len(cart_response.json()["data"]) == 0


@allure.feature("订单服务")
@allure.story("订单管理")
class TestOrderService:
    
    @allure.title("测试创建订单")
    def test_create_order(self, api_client, test_user, test_product):
        items = [{"productId": test_product["id"], "quantity": 1}]
        response = api_client.create_order(user_id=test_user["id"], items=items)
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert "orderNo" in data["data"]
        validate_response_schema(data, ORDER_SCHEMA)
        return data["data"]
    
    @allure.title("测试获取订单")
    def test_get_order(self, api_client, test_user, test_product):
        order = self.test_create_order(api_client, test_user, test_product)
        response = api_client.get_order(order["id"])
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert data["data"]["id"] == order["id"]
        validate_response_schema(data, ORDER_SCHEMA)
    
    @allure.title("测试获取用户订单列表")
    def test_get_user_orders(self, api_client, test_user, test_product):
        self.test_create_order(api_client, test_user, test_product)
        response = api_client.get_user_orders(test_user["id"])
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert isinstance(data["data"], list)
        validate_response_schema(data, ORDER_SCHEMA)
    
    @allure.title("测试更新订单状态")
    def test_update_order_status(self, api_client, test_user, test_product):
        order = self.test_create_order(api_client, test_user, test_product)
        new_status = 1  # 已支付
        response = api_client.update_order_status(order["id"], new_status)
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        
        order_response = api_client.get_order(order["id"])
        assert order_response.json()["data"]["status"] == new_status
