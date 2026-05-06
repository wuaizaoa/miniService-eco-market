import pytest
import allure
from faker import Faker

fake = Faker('zh_CN')

@allure.feature("端到端测试")
@allure.story("完整购物流程")
class TestE2EFlow:
    
    @allure.title("测试完整购物流程: 注册-&gt;登录-&gt;创建商品-&gt;添加购物车-&gt;创建订单-&gt;Mock支付")
    def test_complete_shopping_flow(self, api_client):
        allure.step("1. 用户注册")
        username = f"e2e_user_{fake.uuid4()[:8]}"
        password = "Test123456"
        register_response = api_client.register_user(
            username=username,
            password=password,
            email=fake.email(),
            phone=fake.phone_number()
        )
        assert register_response.status_code == 200
        user_data = register_response.json()["data"]
        user_id = user_data["id"]
        allure.attach(str(user_data), "用户信息", allure.attachment_type.JSON)
        
        allure.step("2. 创建商品分类")
        category_response = api_client.create_category("E2E测试分类", sort=1)
        assert category_response.status_code == 200
        category_data = category_response.json()["data"]
        category_id = category_data["id"]
        allure.attach(str(category_data), "分类信息", allure.attachment_type.JSON)
        
        allure.step("3. 创建商品")
        product_response = api_client.create_product(
            name="E2E测试商品",
            price=199.00,
            stock=100,
            category_id=category_id,
            description="端到端测试商品"
        )
        assert product_response.status_code == 200
        product_data = product_response.json()["data"]
        product_id = product_data["id"]
        initial_stock = product_data["stock"]
        allure.attach(str(product_data), "商品信息", allure.attachment_type.JSON)
        
        allure.step("4. 添加商品到购物车")
        cart_add_response = api_client.add_to_cart(user_id, product_id, quantity=2)
        assert cart_add_response.status_code == 200
        allure.attach(str(cart_add_response.json()), "添加购物车结果", allure.attachment_type.JSON)
        
        allure.step("5. 查询购物车")
        cart_response = api_client.get_cart(user_id)
        assert cart_response.status_code == 200
        cart_items = cart_response.json()["data"]
        assert len(cart_items) &gt; 0
        allure.attach(str(cart_items), "购物车内容", allure.attachment_type.JSON)
        
        allure.step("6. 创建订单")
        items = [{"productId": product_id, "quantity": 2}]
        order_response = api_client.create_order(user_id=user_id, items=items)
        assert order_response.status_code == 200
        order_data = order_response.json()["data"]
        order_id = order_data["id"]
        order_no = order_data["orderNo"]
        assert order_data["status"] == 0  # 待支付
        allure.attach(str(order_data), "订单信息", allure.attachment_type.JSON)
        
        allure.step("7. 查询订单")
        get_order_response = api_client.get_order(order_id)
        assert get_order_response.status_code == 200
        assert get_order_response.json()["data"]["id"] == order_id
        
        allure.step("8. 创建支付记录")
        payment_response = api_client.create_payment(
            order_id=order_id,
            order_no=order_no,
            user_id=user_id,
            amount=order_data["totalAmount"],
            pay_method="alipay"
        )
        assert payment_response.status_code == 200
        payment_data = payment_response.json()["data"]
        payment_id = payment_data["id"]
        allure.attach(str(payment_data), "支付记录", allure.attachment_type.JSON)
        
        allure.step("9. Mock支付")
        pay_response = api_client.mock_pay(payment_id)
        assert pay_response.status_code == 200
        paid_payment_data = pay_response.json()["data"]
        assert paid_payment_data["status"] == 1  # 已支付
        allure.attach(str(paid_payment_data), "支付结果", allure.attachment_type.JSON)
        
        allure.step("10. 验证支付后订单状态")
        final_order_response = api_client.get_order(order_id)
        final_order_data = final_order_response.json()["data"]
        allure.attach(str(final_order_data), "最终订单状态", allure.attachment_type.JSON)
        
        allure.step("11. 验证库存扣减")
        stock_response = api_client.get_stock(product_id)
        final_stock = stock_response.json()["data"]
        assert final_stock == initial_stock - 2
        allure.attach(f"初始库存: {initial_stock}, 最终库存: {final_stock}", "库存变化", allure.attachment_type.TEXT)
        
        allure.step("12. 查询支付记录")
        get_payment_response = api_client.get_payment_by_order_no(order_no)
        assert get_payment_response.status_code == 200
        assert get_payment_response.json()["data"]["status"] == 1
        
        allure.step("流程执行完毕！")
