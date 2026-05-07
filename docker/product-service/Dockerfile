# 基于 OpenJDK 8 运行时环境
FROM openjdk:8-jre-alpine

# 设置工作目录
WORKDIR /app

# 复制编译好的 jar 包
COPY product-service/product-service-start/target/product-service-start.jar app.jar

# 暴露服务端口
EXPOSE 8082

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
