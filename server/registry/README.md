# 本地开发环境 Docker 配置说明

本项目推荐开发环境下使用 Docker 启动基础设施服务，业务微服务直接用IDE本地运行，提升开发效率。

---

## 1. Nacos 注册中心

```bash
docker run -d --name nacos-standalone -e MODE=standalone -p 8848:8848 nacos/nacos-server:2.3.0
```
- 控制台访问：http://localhost:8848/nacos
- 默认账号/密码：nacos / nacos

## 2. Redis（如需缓存）

```bash
docker run -d --name redis-dev -p 6379:6379 redis:7.2.4
```
- 连接地址：localhost:6379

## 3. PostgreSQL

> 本地无需安装，直接连接开发服务器数据库：
> 
> 地址：47.109.190.13:5432  数据库：lcdev  用户名：lcdev  密码：rXPFTpmcYJHLe555

application.yml 示例：
```yaml
spring:
  datasource:
    url: jdbc:postgresql://47.109.190.13:5432/lcdev
    username: lcdev
    password: rXPFTpmcYJHLe555
```

---

## 4. 业务微服务启动建议

- 推荐直接用IDEA等工具本地运行（无需打包镜像），配置好Nacos、Redis、PostgreSQL地址即可。
- 仅Nacos、Redis用Docker运行，数据库用远程服务器。

---

如需docker-compose一键启动脚本或其他环境支持，请联系架构组。 