import pytest
import allure
from faker import Faker
from schemas import validate_response_schema, PAYMENT_SCHEMA

fake = Faker('zh_CN')

@allure.feature("支付服务")
@allure.story("支付管理")
class TestPaymentService:
    
    def create_test_order(self, api_client, test_user, test_product):
        items = [{"productId": test_product["id"], "quantity": 1}]
        order_response = api_client.create_order(user_id=test_user["id"], items=items)
        assert order_response.status_code == 200
        return order_response.json()["data"]
    
    @allure.title("测试创建支付记录")
    def test_create_payment(self, api_client, test_user, test_product):
        order = self.create_test_order(api_client, test_user, test_product)
        response = api_client.create_payment(
            order_id=order["id"],
            order_no=order["orderNo"],
            user_id=test_user["id"],
            amount=order["totalAmount"],
            pay_method="alipay"
        )
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert data["data"]["orderId"] == order["id"]
        assert data["data"]["status"] == 0  # 待支付
        validate_response_schema(data, PAYMENT_SCHEMA)
        return data["data"]
    
    @allure.title("测试Mock支付")
    def test_mock_pay(self, api_client, test_user, test_product):
        payment = self.test_create_payment(api_client, test_user, test_product)
        response = api_client.mock_pay(payment["id"])
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert data["data"]["status"] == 1  # 已支付
        validate_response_schema(data, PAYMENT_SCHEMA)
    
    @allure.title("测试查询支付记录")
    def test_get_payment(self, api_client, test_user, test_product):
        payment = self.test_create_payment(api_client, test_user, test_product)
        response = api_client.get_payment(payment["id"])
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert data["data"]["id"] == payment["id"]
        validate_response_schema(data, PAYMENT_SCHEMA)
    
    @allure.title("测试根据订单号查询支付记录")
    def test_get_payment_by_order_no(self, api_client, test_user, test_product):
        payment = self.test_create_payment(api_client, test_user, test_product)
        response = api_client.get_payment_by_order_no(payment["orderNo"])
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert data["data"]["orderNo"] == payment["orderNo"]
        validate_response_schema(data, PAYMENT_SCHEMA)
