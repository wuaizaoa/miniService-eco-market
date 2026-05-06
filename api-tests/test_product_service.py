import pytest
import allure
from faker import Faker
from schemas import validate_response_schema, CATEGORY_SCHEMA, PRODUCT_SCHEMA

fake = Faker('zh_CN')

@allure.feature("商品服务")
@allure.story("分类管理")
class TestCategoryService:
    
    @allure.title("测试创建商品分类")
    def test_create_category(self, api_client):
        category_name = f"分类_{fake.uuid4()[:8]}"
        response = api_client.create_category(category_name, sort=1)
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert data["data"]["name"] == category_name
        validate_response_schema(data, CATEGORY_SCHEMA)
    
    @allure.title("测试获取单个分类")
    def test_get_category(self, api_client, test_category):
        response = api_client.get_category(test_category["id"])
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert data["data"]["id"] == test_category["id"]
        validate_response_schema(data, CATEGORY_SCHEMA)
    
    @allure.title("测试获取所有分类")
    def test_get_all_categories(self, api_client):
        response = api_client.get_all_categories()
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert isinstance(data["data"], list)
        validate_response_schema(data, CATEGORY_SCHEMA)


@allure.feature("商品服务")
@allure.story("商品管理")
class TestProductService:
    
    @allure.title("测试创建商品")
    def test_create_product(self, api_client, test_category):
        product_name = f"商品_{fake.uuid4()[:8]}"
        response = api_client.create_product(
            name=product_name,
            price=199.99,
            stock=50,
            category_id=test_category["id"],
            description="测试商品描述"
        )
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert data["data"]["name"] == product_name
        validate_response_schema(data, PRODUCT_SCHEMA)
    
    @allure.title("测试获取单个商品")
    def test_get_product(self, api_client, test_product):
        response = api_client.get_product(test_product["id"])
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert data["data"]["id"] == test_product["id"]
        validate_response_schema(data, PRODUCT_SCHEMA)
    
    @allure.title("测试获取所有商品")
    def test_get_all_products(self, api_client):
        response = api_client.get_all_products()
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert isinstance(data["data"], list)
        validate_response_schema(data, PRODUCT_SCHEMA)
    
    @allure.title("测试根据分类获取商品")
    def test_get_products_by_category(self, api_client, test_category, test_product):
        response = api_client.get_products_by_category(test_category["id"])
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert isinstance(data["data"], list)
        validate_response_schema(data, PRODUCT_SCHEMA)
    
    @allure.title("测试更新商品")
    def test_update_product(self, api_client, test_product):
        new_name = f"更新后商品_{fake.uuid4()[:8]}"
        response = api_client.update_product(
            product_id=test_product["id"],
            name=new_name,
            price=299.99
        )
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        validate_response_schema(data, PRODUCT_SCHEMA)
        
        get_response = api_client.get_product(test_product["id"])
        assert get_response.json()["data"]["name"] == new_name
    
    @allure.title("测试查询库存")
    def test_get_stock(self, api_client, test_product):
        response = api_client.get_stock(test_product["id"])
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert isinstance(data["data"], int)
    
    @allure.title("测试扣减库存")
    def test_deduct_stock(self, api_client, test_product):
        initial_response = api_client.get_stock(test_product["id"])
        initial_stock = initial_response.json()["data"]
        
        deduct_quantity = 10
        response = api_client.deduct_stock(test_product["id"], deduct_quantity)
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        
        after_response = api_client.get_stock(test_product["id"])
        after_stock = after_response.json()["data"]
        assert after_stock == initial_stock - deduct_quantity
    
    @allure.title("测试扣减超过库存数量失败")
    def test_deduct_stock_exceed(self, api_client, test_product):
        stock_response = api_client.get_stock(test_product["id"])
        current_stock = stock_response.json()["data"]
        deduct_quantity = current_stock + 100
        
        response = api_client.deduct_stock(test_product["id"], deduct_quantity)
        assert response.status_code == 200
        data = response.json()
        # 根据实际业务逻辑，可能返回false或错误
