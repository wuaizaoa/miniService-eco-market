# API 测试说明

## 概述

本目录包含了电商微服务系统的完整API测试套件，包括：
- 单元测试（各服务独立API测试）
- 端到端业务流程测试
- 性能/并发测试
- 接口契约测试

## 项目结构

```
api-tests/
├── __init__.py
├── config.py                 # 配置文件
├── api_client.py             # API客户端封装
├── schemas.py                # 响应数据Schema定义
├── conftest.py               # Pytest配置和Fixtures
├── requirements.txt          # Python依赖
├── .env.example              # 环境变量示例
├── report_generator.py       # 测试报告生成器
├── performance_test.py       # 性能测试脚本
├── locustfile.py             # Locust负载测试配置
├── run_tests.bat             # Windows执行脚本
├── run_tests.sh              # Linux/Mac执行脚本
├── test_user_service.py      # 用户服务测试
├── test_product_service.py   # 商品服务测试
├── test_order_service.py     # 订单和购物车服务测试
├── test_payment_service.py   # 支付服务测试
├── test_e2e_flow.py          # 端到端业务流程测试
└── reports/                  # 测试报告输出目录
```

## 环境准备

### 前置条件

1. Python 3.7+
2. 微服务已启动（默认端口：8081-8084）
3. MySQL 数据库 lesson 已创建
4. Redis 已启动（用于购物车）

### 安装步骤

1. 进入测试目录：
```bash
cd api-tests
```

2. 创建虚拟环境：
```bash
python -m venv venv
```

3. 激活虚拟环境：
```bash
# Windows
venv\Scripts\activate.bat

# Linux/Mac
source venv/bin/activate
```

4. 安装依赖：
```bash
pip install -r requirements.txt
```

5. 复制并配置环境变量：
```bash
copy .env.example .env
# 编辑 .env 文件，根据实际情况修改配置
```

## 运行测试

### 快速开始

使用提供的执行脚本：

```bash
# Windows
run_tests.bat

# Linux/Mac
chmod +x run_tests.sh
./run_tests.sh
```

### 手动执行测试

#### 运行所有测试
```bash
pytest -v
```

#### 运行指定服务测试
```bash
# 仅用户服务
pytest test_user_service.py -v

# 仅商品服务
pytest test_product_service.py -v

# 仅订单服务
pytest test_order_service.py -v

# 仅支付服务
pytest test_payment_service.py -v

# 端到端测试
pytest test_e2e_flow.py -v
```

#### 生成HTML报告
```bash
pytest -v --html=reports/report.html --self-contained-html
```

#### 生成Allure报告
```bash
pytest --alluredir=allure-results
allure serve allure-results
```

## 性能测试

### 使用自定义性能测试脚本

```bash
python performance_test.py
```

### 使用Locust进行负载测试

1. 启动Locust Web界面：
```bash
locust -f locustfile.py --host=http://localhost:8081
```

2. 访问 http://localhost:8089
3. 设置并发用户数和生成速率
4. 开始测试

## 测试类型说明

### 1. 单元/契约测试
- 验证各API接口的功能正确性
- 验证返回数据格式符合Schema
- 测试正常和异常场景

### 2. 端到端测试
- 模拟完整购物流程：注册 → 登录 → 建商品 → 加购物车 → 创建订单 → 支付
- 验证服务间调用链的正确性

### 3. 性能/并发测试
- 使用Locust进行压力测试
- 测量响应时间、吞吐量等指标
- 验证系统在负载下的稳定性

## 配置修改

如需修改服务地址或数据库配置，编辑 `config.py` 或 `.env` 文件：

```python
USER_SERVICE_URL = "http://your-host:8081"
PRODUCT_SERVICE_URL = "http://your-host:8082"
ORDER_SERVICE_URL = "http://your-host:8083"
PAYMENT_SERVICE_URL = "http://your-host:8084"
```

## 测试报告

测试报告将生成在 `reports/` 目录下：
- `test_report.html` - HTML格式的测试报告
- 其他服务的独立报告

## 注意事项

1. 确保所有微服务正常运行
2. 确保数据库连接正常
3. 测试数据可能会被清理，请使用测试环境
4. 性能测试时注意观察服务器资源使用情况

## 故障排查

- 连接失败：检查服务是否启动，端口是否正确
- 测试失败：查看详细错误信息，检查数据状态
- 依赖问题：重新运行 `pip install -r requirements.txt`

## License

与主项目保持一致。
