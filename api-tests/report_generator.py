import json
import os
from datetime import datetime
from pathlib import Path

class TestReportGenerator:
    def __init__(self, report_dir="reports"):
        self.report_dir = Path(report_dir)
        self.report_dir.mkdir(exist_ok=True)
    
    def generate_html_report(self, test_results: dict, report_name: str = None):
        if not report_name:
            report_name = f"test_report_{datetime.now().strftime('%Y%m%d_%H%M%S')}.html"
        
        report_path = self.report_dir / report_name
        
        html_content = self._build_html(test_results)
        
        with open(report_path, "w", encoding="utf-8") as f:
            f.write(html_content)
        
        print(f"Test report generated: {report_path}")
        return report_path
    
    def _build_html(self, test_results: dict):
        total_tests = test_results.get("total", 0)
        passed = test_results.get("passed", 0)
        failed = test_results.get("failed", 0)
        success_rate = (passed / total_tests * 100) if total_tests &gt; 0 else 0
        
        html = f"""
&lt;!DOCTYPE html&gt;
&lt;html lang="zh-CN"&gt;
&lt;head&gt;
    &lt;meta charset="UTF-8"&gt;
    &lt;meta name="viewport" content="width=device-width, initial-scale=1.0"&gt;
    &lt;title&gt;API测试报告&lt;/title&gt;
    &lt;style&gt;
        body {{
            font-family: 'Segoe UI', Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }}
        .container {{
            max-width: 1200px;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }}
        h1 {{
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }}
        .summary {{
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 20px;
            margin-bottom: 30px;
        }}
        .summary-card {{
            padding: 20px;
            border-radius: 8px;
            text-align: center;
            color: white;
        }}
        .total {{ background-color: #3498db; }}
        .passed {{ background-color: #27ae60; }}
        .failed {{ background-color: #e74c3c; }}
        .success-rate {{ background-color: #9b59b6; }}
        .summary-card h3 {{
            margin: 0 0 10px 0;
            font-size: 14px;
            opacity: 0.9;
        }}
        .summary-card .value {{
            font-size: 36px;
            font-weight: bold;
        }}
        .test-cases {{
            margin-top: 30px;
        }}
        .test-cases h2 {{
            color: #333;
            border-bottom: 2px solid #3498db;
            padding-bottom: 10px;
        }}
        table {{
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }}
        th, td {{
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }}
        th {{
            background-color: #f8f9fa;
            font-weight: bold;
            color: #333;
        }}
        .status-pass {{ color: #27ae60; font-weight: bold; }}
        .status-fail {{ color: #e74c3c; font-weight: bold; }}
        .timestamp {{
            text-align: center;
            color: #666;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }}
    &lt;/style&gt;
&lt;/head&gt;
&lt;body&gt;
    &lt;div class="container"&gt;
        &lt;h1&gt;🛒 电商微服务 API 测试报告&lt;/h1&gt;
        
        &lt;div class="summary"&gt;
            &lt;div class="summary-card total"&gt;
                &lt;h3&gt;总测试数&lt;/h3&gt;
                &lt;div class="value"&gt;{total_tests}&lt;/div&gt;
            &lt;/div&gt;
            &lt;div class="summary-card passed"&gt;
                &lt;h3&gt;通过&lt;/h3&gt;
                &lt;div class="value"&gt;{passed}&lt;/div&gt;
            &lt;/div&gt;
            &lt;div class="summary-card failed"&gt;
                &lt;h3&gt;失败&lt;/h3&gt;
                &lt;div class="value"&gt;{failed}&lt;/div&gt;
            &lt;/div&gt;
            &lt;div class="summary-card success-rate"&gt;
                &lt;h3&gt;通过率&lt;/h3&gt;
                &lt;div class="value"&gt;{success_rate:.1f}%&lt;/div&gt;
            &lt;/div&gt;
        &lt;/div&gt;
        
        &lt;div class="test-cases"&gt;
            &lt;h2&gt;测试用例详情&lt;/h2&gt;
            &lt;table&gt;
                &lt;thead&gt;
                    &lt;tr&gt;
                        &lt;th&gt;测试套件&lt;/th&gt;
                        &lt;th&gt;测试用例&lt;/th&gt;
                        &lt;th&gt;状态&lt;/th&gt;
                        &lt;th&gt;耗时&lt;/th&gt;
                        &lt;th&gt;备注&lt;/th&gt;
                    &lt;/tr&gt;
                &lt;/thead&gt;
                &lt;tbody&gt;
        """
        
        for test_case in test_results.get("test_cases", []):
            status_class = "status-pass" if test_case.get("status") == "PASS" else "status-fail"
            html += f"""
                    &lt;tr&gt;
                        &lt;td&gt;{test_case.get("suite", "")}&lt;/td&gt;
                        &lt;td&gt;{test_case.get("name", "")}&lt;/td&gt;
                        &lt;td class="{status_class}"&gt;{test_case.get("status", "")}&lt;/td&gt;
                        &lt;td&gt;{test_case.get("duration", "")}s&lt;/td&gt;
                        &lt;td&gt;{test_case.get("message", "")}&lt;/td&gt;
                    &lt;/tr&gt;
            """
        
        html += f"""
                &lt;/tbody&gt;
            &lt;/table&gt;
        &lt;/div&gt;
        
        &lt;div class="timestamp"&gt;
            报告生成时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}
        &lt;/div&gt;
    &lt;/div&gt;
&lt;/body&gt;
&lt;/html&gt;
        """
        return html


if __name__ == "__main__":
    sample_results = {
        "total": 30,
        "passed": 25,
        "failed": 5,
        "test_cases": [
            {"suite": "用户服务", "name": "test_register_user", "status": "PASS", "duration": "0.5", "message": "OK"},
            {"suite": "用户服务", "name": "test_login_user", "status": "PASS", "duration": "0.3", "message": "OK"},
            {"suite": "商品服务", "name": "test_create_product", "status": "FAIL", "duration": "0.8", "message": "Connection error"}
        ]
    }
    
    generator = TestReportGenerator()
    generator.generate_html_report(sample_results)
