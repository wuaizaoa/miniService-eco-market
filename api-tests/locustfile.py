from locust import HttpUser, task, between, events
import random
from faker import Faker

fake = Faker('zh_CN')

class ECommerceUser(HttpUser):
    wait_time = between(1, 3)
    test_user_id = None
    test_category_id = None
    test_product_id = None
    test_order_id = None
    
    def on_start(self):
        self.prepare_test_data()
    
    def prepare_test_data(self):
        username = f"load_test_{fake.uuid4()[:8]}"
        password = "Test123456"
        
        # 注册用户
        register_response = self.client.post(
            "/api/user/register",
            json={"username": username, "password": password},
            headers={"Content-Type": "application/json"}
        )
        if register_response.status_code == 200:
            self.test_user_id = register_response.json()["data"]["id"]
        
        # 创建分类
        category_response = self.client.post(
            "/api/product/category",
            json={"name": f"LoadTestCategory_{fake.uuid4()[:8]}", "sort": 1},
            headers={"Content-Type": "application/json"}
        )
        if category_response.status_code == 200:
            self.test_category_id = category_response.json()["data"]["id"]
        
        # 创建商品
        if self.test_category_id:
            product_response = self.client.post(
                "/api/product",
                json={
                    "name": f"LoadTestProduct_{fake.uuid4()[:8]}",
                    "price": 99.99,
                    "stock": 1000,
                    "categoryId": self.test_category_id
                },
                headers={"Content-Type": "application/json"}
            )
            if product_response.status_code == 200:
                self.test_product_id = product_response.json()["data"]["id"]
    
    @task(3)
    def test_get_product_list(self):
        self.client.get("/api/product")
    
    @task(2)
    def test_get_product_detail(self):
        if self.test_product_id:
            self.client.get(f"/api/product/{self.test_product_id}")
    
    @task(2)
    def test_add_to_cart(self):
        if self.test_user_id and self.test_product_id:
            self.client.post(
                "/api/cart",
                json={"userId": self.test_user_id, "productId": self.test_product_id, "quantity": random.randint(1, 3)},
                headers={"Content-Type": "application/json"}
            )
    
    @task(1)
    def test_get_cart(self):
        if self.test_user_id:
            self.client.get(f"/api/cart/{self.test_user_id}")
    
    @task(1)
    def test_create_order(self):
        if self.test_user_id and self.test_product_id:
            order_response = self.client.post(
                "/api/order",
                json={"userId": self.test_user_id, "items": [{"productId": self.test_product_id, "quantity": 1}]},
                headers={"Content-Type": "application/json"}
            )
            if order_response.status_code == 200:
                self.test_order_id = order_response.json()["data"]["id"]
    
    @task(1)
    def test_get_orders(self):
        if self.test_user_id:
            self.client.get(f"/api/order/user/{self.test_user_id}")
    
    @task(2)
    def test_get_category_list(self):
        self.client.get("/api/product/category")
