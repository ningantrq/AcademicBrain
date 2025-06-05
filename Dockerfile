# 使用官方OpenJDK 8基础镜像
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制Maven构建文件
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# 复制源代码
COPY src src

# 构建应用
RUN ./mvnw clean package -DskipTests

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "target/academic-brain-1.0.0.jar"] 