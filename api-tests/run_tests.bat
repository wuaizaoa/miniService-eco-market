@echo off
echo ====================================
echo 电商微服务 API 测试 - 执行脚本
echo ====================================
echo.

REM 检查是否安装了Python
python --version &gt;nul 2&gt;&amp;1
if %errorlevel% neq 0 (
    echo [错误] 未检测到Python，请先安装Python 3.7+
    pause
    exit /b 1
)

REM 检查是否在虚拟环境中，或者激活虚拟环境
if not exist "venv" (
    echo [信息] 创建虚拟环境...
    python -m venv venv
)

echo [信息] 激活虚拟环境...
call venv\Scripts\activate.bat

echo [信息] 安装依赖...
pip install -r requirements.txt

echo.
echo ====================================
echo 请选择测试类型:
echo ====================================
echo 1. 运行所有API测试
echo 2. 运行用户服务测试
echo 3. 运行商品服务测试
echo 4. 运行订单服务测试
echo 5. 运行支付服务测试
echo 6. 运行端到端测试
echo 7. 运行性能测试
echo 8. 启动Locust性能测试界面
echo.
set /p choice="请输入选项 (1-8): "

if "%choice%"=="1" goto run_all
if "%choice%"=="2" goto run_user
if "%choice%"=="3" goto run_product
if "%choice%"=="4" goto run_order
if "%choice%"=="5" goto run_payment
if "%choice%"=="6" goto run_e2e
if "%choice%"=="7" goto run_performance
if "%choice%"=="8" goto run_locust

echo [错误] 无效的选项
goto end

:run_all
echo [信息] 运行所有API测试...
pytest test_user_service.py test_product_service.py test_order_service.py test_payment_service.py test_e2e_flow.py -v --html=reports/test_report.html
goto end

:run_user
echo [信息] 运行用户服务测试...
pytest test_user_service.py -v --html=reports/user_service_report.html
goto end

:run_product
echo [信息] 运行商品服务测试...
pytest test_product_service.py -v --html=reports/product_service_report.html
goto end

:run_order
echo [信息] 运行订单服务测试...
pytest test_order_service.py -v --html=reports/order_service_report.html
goto end

:run_payment
echo [信息] 运行支付服务测试...
pytest test_payment_service.py -v --html=reports/payment_service_report.html
goto end

:run_e2e
echo [信息] 运行端到端测试...
pytest test_e2e_flow.py -v --html=reports/e2e_report.html
goto end

:run_performance
echo [信息] 运行性能测试...
python performance_test.py
goto end

:run_locust
echo [信息] 启动Locust性能测试界面...
echo 请在浏览器中访问 http://localhost:8089
locust -f locustfile.py --host=http://localhost:8081
goto end

:end
echo.
echo 测试执行完毕！
pause
