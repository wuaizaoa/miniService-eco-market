import pytest
import allure
from faker import Faker
from schemas import validate_response_schema, USER_SCHEMA, LOGIN_SCHEMA

fake = Faker('zh_CN')

@allure.feature("用户服务")
@allure.story("用户注册")
class TestUserService:
    
    @allure.title("测试正常注册新用户")
    def test_register_user_success(self, api_client):
        username = f"user_{fake.uuid4()[:8]}"
        password = "Test123456"
        
        response = api_client.register_user(username, password, email=fake.email(), phone=fake.phone_number())
        
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert data["data"]["username"] == username
        validate_response_schema(data, USER_SCHEMA)
    
    @allure.title("测试使用已存在用户注册失败")
    def test_register_duplicate_user(self, api_client, test_user):
        response = api_client.register_user(test_user["username"], "Test123456")
        assert response.status_code == 200
        data = response.json()
        assert data["code"] != 200
    
    @allure.title("测试缺少必填字段注册失败")
    def test_register_missing_fields(self, api_client):
        response = api_client.register_user("", "")
        assert response.status_code in [400, 200]
        if response.status_code == 200:
            assert response.json()["code"] != 200
    
    @allure.title("测试用户登录")
    def test_login_user(self, api_client, test_user):
        response = api_client.register_user(test_user["username"], "Test123456")
        # 重新注册一个新用户
        username = f"login_test_{fake.uuid4()[:8]}"
        password = "Test123456"
        api_client.register_user(username, password)
        
        login_response = api_client.login_user(username, password)
        assert login_response.status_code == 200
        data = login_response.json()
        assert data["code"] == 200
        assert data["data"]["username"] == username
        validate_response_schema(data, LOGIN_SCHEMA)
    
    @allure.title("测试错误密码登录失败")
    def test_login_wrong_password(self, api_client):
        username = f"login_wrong_{fake.uuid4()[:8]}"
        api_client.register_user(username, "Test123456")
        response = api_client.login_user(username, "WrongPassword123")
        assert response.status_code == 200
        data = response.json()
        assert data["code"] != 200
    
    @allure.title("测试不存在用户查询")
    def test_get_user(self, api_client, test_user):
        response = api_client.get_user(test_user["id"])
        assert response.status_code == 200
        data = response.json()
        assert data["code"] == 200
        assert data["data"]["id"] == test_user["id"]
        validate_response_schema(data, USER_SCHEMA)
    
    @allure.title("测试查询不存在用户")
    def test_get_nonexistent_user(self, api_client):
        response = api_client.get_user(99999999)
        assert response.status_code == 200
        data = response.json()
        assert data["code"] != 200
