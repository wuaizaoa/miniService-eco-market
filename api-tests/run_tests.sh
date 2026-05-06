#!/bin/bash

echo "===================================="
echo "电商微服务 API 测试 - 执行脚本"
echo "===================================="
echo ""

# 检查Python
if ! command -v python3 &amp;&gt; /dev/null; then
    echo "[错误] 未检测到Python，请先安装Python 3.7+"
    exit 1
fi

# 创建虚拟环境
if [ ! -d "venv" ]; then
    echo "[信息] 创建虚拟环境..."
    python3 -m venv venv
fi

echo "[信息] 激活虚拟环境..."
source venv/bin/activate

echo "[信息] 安装依赖..."
pip install -r requirements.txt

echo ""
echo "===================================="
echo "请选择测试类型:"
echo "===================================="
echo "1. 运行所有API测试"
echo "2. 运行用户服务测试"
echo "3. 运行商品服务测试"
echo "4. 运行订单服务测试"
echo "5. 运行支付服务测试"
echo "6. 运行端到端测试"
echo "7. 运行性能测试"
echo "8. 启动Locust性能测试界面"
echo ""
read -p "请输入选项 (1-8): " choice

case $choice in
    1)
        echo "[信息] 运行所有API测试..."
        pytest test_user_service.py test_product_service.py test_order_service.py test_payment_service.py test_e2e_flow.py -v --html=reports/test_report.html
        ;;
    2)
        echo "[信息] 运行用户服务测试..."
        pytest test_user_service.py -v --html=reports/user_service_report.html
        ;;
    3)
        echo "[信息] 运行商品服务测试..."
        pytest test_product_service.py -v --html=reports/product_service_report.html
        ;;
    4)
        echo "[信息] 运行订单服务测试..."
        pytest test_order_service.py -v --html=reports/order_service_report.html
        ;;
    5)
        echo "[信息] 运行支付服务测试..."
        pytest test_payment_service.py -v --html=reports/payment_service_report.html
        ;;
    6)
        echo "[信息] 运行端到端测试..."
        pytest test_e2e_flow.py -v --html=reports/e2e_report.html
        ;;
    7)
        echo "[信息] 运行性能测试..."
        python performance_test.py
        ;;
    8)
        echo "[信息] 启动Locust性能测试界面..."
        echo "请在浏览器中访问 http://localhost:8089"
        locust -f locustfile.py --host=http://localhost:8081
        ;;
    *)
        echo "[错误] 无效的选项"
        ;;
esac

echo ""
echo "测试执行完毕！"
