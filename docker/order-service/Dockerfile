# 基于 OpenJDK 8 运行时环境
FROM openjdk:8-jre-alpine

# 设置工作目录
WORKDIR /app

# 复制编译好的 jar 包
COPY order-service/order-service-start/target/order-service-start.jar app.jar

# 暴露服务端口
EXPOSE 8083

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
