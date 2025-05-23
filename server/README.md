# 服务端项目

本目录包含 Logistics Cloud 的所有后端微服务项目。

## 项目结构

```
server/
├── common/                    # 通用组件
│   ├── common-core/           # 核心工具类
│   │   - 统一响应对象
│   │   - 全局异常处理
│   │   - 通用工具类
│   ├── common-security/       # 安全组件
│   │   - JWT工具类
│   │   - 安全配置
│   │   - 权限注解
│   └── common-data/           # 数据访问组件
│       - 多租户支持
│       - 数据权限
│       - 审计功能
│
├── framework/                 # 框架组件
│   ├── framework-web/         # Web框架
│   │   - 统一跨域配置
│   │   - 请求日志记录
│   │   - 参数验证
│   └── framework-swagger/     # API文档
│       - Swagger配置
│       - 接口文档规范
│
├── gateway/                   # API网关
│   - 路由配置
│   - 认证过滤
│   - 限流配置
│
├── auth/                      # 认证中心
│   - RBAC权限模型
│   - 租户管理
│   - 系统管理
│
└── services/                  # 业务服务
    ├── wms/                   # 仓库管理系统
    │   ├── wms-api/           # 接口定义
    │   ├── wms-inbound/       # 入库服务
    │   ├── wms-outbound/      # 出库服务
    │   ├── wms-internal/      # 库内服务
    │   └── wms-report/        # 报表服务
    │
    └── tms/                   # 运输管理系统
        ├── tms-api/           # 接口定义
        ├── tms-route/         # 路线规划
        ├── tms-schedule/      # 调度服务
        └── tms-tracking/      # 跟踪服务

```

## 技术栈

- 核心框架：Spring Boot 3.2.x, Spring Cloud 2023+
- 数据存储：PostgreSQL, Redis
- 消息队列：RabbitMQ
- 服务注册：Eureka
- 配置中心：Spring Cloud Config
- 网关服务：Spring Cloud Gateway
- 安全框架：Spring Security, JWT
- API文档：Swagger/OpenAPI 3.0
- 构建工具：Gradle 8.7

## 开发规范

1. 代码规范
   - 遵循 Alibaba Java 编码规范
   - 使用 CheckStyle 进行代码检查
   - 使用 SpotBugs 进行bug检查

2. 提交规范
   - feat: 新功能
   - fix: 修复bug
   - docs: 文档更新
   - style: 代码格式化
   - refactor: 代码重构
   - test: 测试代码
   - chore: 其他修改

3. 分支管理
   - master: 主分支
   - develop: 开发分支
   - feature/*: 功能分支
   - hotfix/*: 紧急修复分支

## 日志配置约定

- 所有后端微服务（包括后续新增的服务）都必须在各自的 `src/main/resources` 目录下放置一份 `logback-spring.xml` 日志配置文件。
- 推荐使用 `server/common/common-core/src/main/resources/logback-spring.xml` 作为统一模板，复制到各服务后可根据实际需要微调。
- 这样可以保证日志格式、输出路径、日志级别等在所有服务中保持一致，便于后续运维和日志收集。

## 开发指南

1. 环境准备
   - JDK 17+
   - PostgreSQL 15+
   - Redis 7+
   - RabbitMQ 3.12+

2. 本地开发
   ```bash
   # 编译项目
   ./gradlew clean build

   # 运行服务
   ./gradlew bootRun
   ```

3. Docker部署
   ```bash
   # 构建镜像
   ./gradlew bootBuildImage

   # 运行容器
   docker-compose up -d
   ```

## Nacos 注册中心与配置中心使用说明

### 1. 依赖引入

已在根 build.gradle 中为所有子项目引入如下依赖：
- com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery
- com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config

### 2. application.yml 基础配置示例

```yaml
spring:
  application:
    name: your-service-name
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848   # Nacos服务地址
      config:
        server-addr: 127.0.0.1:8848   # Nacos服务地址
        file-extension: yaml
```

### 3. 启动Nacos（本地开发）

推荐使用官方Docker镜像：
```bash
docker run -d --name nacos-standalone -e MODE=standalone -p 8848:8848 nacos/nacos-server:2.3.0
```

访问 http://localhost:8848/nacos 默认账号密码：nacos/nacos

### 4. 常见问题
- 各服务的 spring.application.name 必须唯一。
- Nacos配置中心支持多环境（dev/test/prod）和动态刷新。
- 生产环境建议使用Nacos集群+MySQL持久化。

### 5. 参考文档
- [Nacos官方文档](https://nacos.io/zh-cn/docs/) 