import pytest
from faker import Faker
from api_client import APIClient
from config import Config

fake = Faker('zh_CN')

@pytest.fixture(scope="session")
def api_client():
    return APIClient()

@pytest.fixture(scope="session")
def test_user(api_client):
    username = f"test_user_{fake.uuid4()[:8]}"
    password = "Test123456"
    response = api_client.register_user(username, password, email=fake.email(), phone=fake.phone_number())
    assert response.status_code == 200
    user_data = response.json()["data"]
    return user_data

@pytest.fixture(scope="session")
def test_category(api_client):
    response = api_client.create_category(f"测试分类_{fake.uuid4()[:8]}", sort=1)
    assert response.status_code == 200
    category_data = response.json()["data"]
    return category_data

@pytest.fixture(scope="session")
def test_product(api_client, test_category):
    response = api_client.create_product(
        name=f"测试商品_{fake.uuid4()[:8]}",
        price=99.99,
        stock=100,
        category_id=test_category["id"],
        description="这是一个测试商品"
    )
    assert response.status_code == 200
    product_data = response.json()["data"]
    return product_data
