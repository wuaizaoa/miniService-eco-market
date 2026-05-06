import requests
import time
from typing import Dict, Any, Optional
from config import Config

class APIClient:
    def __init__(self):
        self.session = requests.Session()
        self.session.headers.update({"Content-Type": "application/json"})
    
    def _request(self, method: str, url: str, **kwargs) -> requests.Response:
        start_time = time.time()
        try:
            response = self.session.request(method, url, **kwargs)
            end_time = time.time()
            response.elapsed_time = end_time - start_time
            return response
        except requests.exceptions.RequestException as e:
            raise Exception(f"Request failed: {e}")
    
    # User Service
    def register_user(self, username: str, password: str, email: str = None, phone: str = None) -> requests.Response:
        data = {"username": username, "password": password}
        if email:
            data["email"] = email
        if phone:
            data["phone"] = phone
        return self._request("POST", f"{Config.USER_API}/register", json=data)
    
    def login_user(self, username: str, password: str) -> requests.Response:
        data = {"username": username, "password": password}
        return self._request("POST", f"{Config.USER_API}/login", json=data)
    
    def get_user(self, user_id: int) -> requests.Response:
        return self._request("GET", f"{Config.USER_API}/{user_id}")
    
    # Product Service - Category
    def create_category(self, name: str, parent_id: int = None, sort: int = 0) -> requests.Response:
        data = {"name": name, "sort": sort}
        if parent_id:
            data["parentId"] = parent_id
        return self._request("POST", f"{Config.PRODUCT_API}/category", json=data)
    
    def get_category(self, category_id: int) -> requests.Response:
        return self._request("GET", f"{Config.PRODUCT_API}/category/{category_id}")
    
    def get_all_categories(self) -> requests.Response:
        return self._request("GET", f"{Config.PRODUCT_API}/category")
    
    def delete_category(self, category_id: int) -> requests.Response:
        return self._request("DELETE", f"{Config.PRODUCT_API}/category/{category_id}")
    
    # Product Service - Product
    def create_product(self, name: str, price: float, stock: int, category_id: int, 
                       description: str = None, image: str = None, status: int = 1) -> requests.Response:
        data = {
            "name": name,
            "price": price,
            "stock": stock,
            "categoryId": category_id,
            "status": status
        }
        if description:
            data["description"] = description
        if image:
            data["image"] = image
        return self._request("POST", f"{Config.PRODUCT_API}", json=data)
    
    def update_product(self, product_id: int, **kwargs) -> requests.Response:
        data = {"id": product_id}
        data.update(kwargs)
        return self._request("PUT", f"{Config.PRODUCT_API}", json=data)
    
    def get_product(self, product_id: int) -> requests.Response:
        return self._request("GET", f"{Config.PRODUCT_API}/{product_id}")
    
    def get_all_products(self) -> requests.Response:
        return self._request("GET", f"{Config.PRODUCT_API}")
    
    def get_products_by_category(self, category_id: int) -> requests.Response:
        return self._request("GET", f"{Config.PRODUCT_API}/category/{category_id}")
    
    def delete_product(self, product_id: int) -> requests.Response:
        return self._request("DELETE", f"{Config.PRODUCT_API}/{product_id}")
    
    def deduct_stock(self, product_id: int, quantity: int) -> requests.Response:
        return self._request("POST", f"{Config.PRODUCT_API}/{product_id}/stock/deduct", params={"quantity": quantity})
    
    def get_stock(self, product_id: int) -> requests.Response:
        return self._request("GET", f"{Config.PRODUCT_API}/{product_id}/stock")
    
    # Order Service
    def create_order(self, user_id: int, items: list) -> requests.Response:
        data = {"userId": user_id, "items": items}
        return self._request("POST", f"{Config.ORDER_API}", json=data)
    
    def get_order(self, order_id: int) -> requests.Response:
        return self._request("GET", f"{Config.ORDER_API}/{order_id}")
    
    def get_user_orders(self, user_id: int) -> requests.Response:
        return self._request("GET", f"{Config.ORDER_API}/user/{user_id}")
    
    def update_order_status(self, order_id: int, status: int) -> requests.Response:
        data = {"orderId": order_id, "status": status}
        return self._request("PUT", f"{Config.ORDER_API}/status", json=data)
    
    # Cart Service
    def add_to_cart(self, user_id: int, product_id: int, quantity: int) -> requests.Response:
        data = {"userId": user_id, "productId": product_id, "quantity": quantity}
        return self._request("POST", f"{Config.CART_API}", json=data)
    
    def update_cart_item(self, user_id: int, product_id: int, quantity: int) -> requests.Response:
        data = {"userId": user_id, "productId": product_id, "quantity": quantity}
        return self._request("PUT", f"{Config.CART_API}", json=data)
    
    def remove_from_cart(self, user_id: int, product_id: int) -> requests.Response:
        return self._request("DELETE", f"{Config.CART_API}/{user_id}/{product_id}")
    
    def get_cart(self, user_id: int) -> requests.Response:
        return self._request("GET", f"{Config.CART_API}/{user_id}")
    
    def clear_cart(self, user_id: int) -> requests.Response:
        return self._request("DELETE", f"{Config.CART_API}/{user_id}")
    
    # Payment Service
    def create_payment(self, order_id: int, order_no: str, user_id: int, amount: float, pay_method: str = "alipay") -> requests.Response:
        data = {
            "orderId": order_id,
            "orderNo": order_no,
            "userId": user_id,
            "amount": amount,
            "payMethod": pay_method
        }
        return self._request("POST", f"{Config.PAYMENT_API}", json=data)
    
    def mock_pay(self, payment_id: int) -> requests.Response:
        return self._request("POST", f"{Config.PAYMENT_API}/{payment_id}/pay")
    
    def get_payment(self, payment_id: int) -> requests.Response:
        return self._request("GET", f"{Config.PAYMENT_API}/{payment_id}")
    
    def get_payment_by_order_no(self, order_no: str) -> requests.Response:
        return self._request("GET", f"{Config.PAYMENT_API}/order/{order_no}")
