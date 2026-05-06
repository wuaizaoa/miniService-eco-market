import os
from dotenv import load_dotenv

load_dotenv()

class Config:
    # 服务地址
    USER_SERVICE_URL = os.getenv("USER_SERVICE_URL", "http://localhost:8081")
    PRODUCT_SERVICE_URL = os.getenv("PRODUCT_SERVICE_URL", "http://localhost:8082")
    ORDER_SERVICE_URL = os.getenv("ORDER_SERVICE_URL", "http://localhost:8083")
    PAYMENT_SERVICE_URL = os.getenv("PAYMENT_SERVICE_URL", "http://localhost:8084")
    
    # 性能测试配置
    CONCURRENT_USERS = int(os.getenv("CONCURRENT_USERS", "50"))
    SPAWN_RATE = int(os.getenv("SPAWN_RATE", "10"))
    RUN_DURATION = int(os.getenv("RUN_DURATION", "60"))
    
    # API 路径
    USER_API = f"{USER_SERVICE_URL}/api/user"
    PRODUCT_API = f"{PRODUCT_SERVICE_URL}/api/product"
    ORDER_API = f"{ORDER_SERVICE_URL}/api/order"
    CART_API = f"{ORDER_SERVICE_URL}/api/cart"
    PAYMENT_API = f"{PAYMENT_SERVICE_URL}/api/payment"
