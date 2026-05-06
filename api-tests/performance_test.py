import time
import statistics
from api_client import APIClient
from faker import Faker

fake = Faker('zh_CN')

class PerformanceTester:
    def __init__(self):
        self.client = APIClient()
        self.results = []
    
    def run_concurrent_test(self, concurrent_users: int, iterations: int):
        import concurrent.futures
        
        print(f"Starting performance test with {concurrent_users} concurrent users...")
        
        with concurrent.futures.ThreadPoolExecutor(max_workers=concurrent_users) as executor:
            futures = [
                executor.submit(self._test_iteration, i) 
                for i in range(concurrent_users * iterations)
            ]
            
            for future in concurrent.futures.as_completed(futures):
                try:
                    result = future.result()
                    self.results.append(result)
                except Exception as e:
                    print(f"Test error: {e}")
        
        return self._analyze_results()
    
    def _test_iteration(self, iteration_id: int):
        start_time = time.time()
        
        # 简单的读操作性能测试
        self.client.get_all_products()
        self.client.get_all_categories()
        
        end_time = time.time()
        elapsed = (end_time - start_time) * 1000
        
        return {
            "iteration_id": iteration_id,
            "success": True,
            "elapsed_ms": elapsed
        }
    
    def _analyze_results(self):
        if not self.results:
            return {}
        
        success_count = sum(1 for r in self.results if r["success"])
        times = [r["elapsed_ms"] for r in self.results if r["success"]]
        
        return {
            "total_tests": len(self.results),
            "success_tests": success_count,
            "fail_tests": len(self.results) - success_count,
            "success_rate": (success_count / len(self.results)) * 100,
            "avg_response_ms": statistics.mean(times) if times else 0,
            "min_response_ms": min(times) if times else 0,
            "max_response_ms": max(times) if times else 0,
            "p50_response_ms": statistics.median(times) if times else 0,
            "p95_response_ms": sorted(times)[int(len(times)*0.95)] if times else 0,
            "p99_response_ms": sorted(times)[int(len(times)*0.99)] if times else 0
        }


if __name__ == "__main__":
    tester = PerformanceTester()
    results = tester.run_concurrent_test(concurrent_users=10, iterations=5)
    
    print("\n" + "="*50)
    print("性能测试结果")
    print("="*50)
    for key, value in results.items():
        print(f"{key}: {value}")
    print("="*50 + "\n")
