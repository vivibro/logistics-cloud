# common-security 模块说明

本模块为 Logistics Cloud 微服务架构下的通用安全组件，所有后端微服务均应依赖本模块，实现统一的认证与鉴权能力。

---

## 主要功能

1. **JWT 工具类（JwtUtil）**
   - 负责解析、校验 JWT token，提取用户ID、用户名、角色等信息。
   - 仅做解析和校验，不负责签发（签发由 auth 服务负责）。
   - 保证所有微服务对 token 的校验逻辑一致。

2. **Spring Security 配置（SecurityConfig）**
   - 集成 JWT 认证过滤器，实现无状态认证。
   - 支持注解式权限控制（如 `@PreAuthorize`）。
   - 关闭 CSRF，允许跨域，适配前后端分离架构。

3. **权限注解支持**
   - 支持 `@PreAuthorize`、`@Secured` 等注解，便于在 Controller 层精细化控制接口权限。
   - 示例：
     ```java
     @PreAuthorize("hasRole('ADMIN')")
     public String adminApi() { ... }
     ```

4. **全局安全异常处理器**
   - `RestAuthenticationEntryPoint`：未认证时返回 401，响应结构如下：
     ```json
     {
       "code": 401,
       "message": "未认证，请登录",
       "path": "/api/xxx",
       "timestamp": 1688888888888
     }
     ```
   - `RestAccessDeniedHandler`：权限不足时返回 403，响应结构如下：
     ```json
     {
       "code": 403,
       "message": "无权限访问",
       "path": "/api/xxx",
       "timestamp": 1688888888888
     }
     ```

---

## 集成与使用说明

1. **依赖本模块**
   - 在业务微服务的 build.gradle 中添加对 common-security 的依赖。

2. **接口权限控制**
   - 直接在 Controller 方法上使用 `@PreAuthorize` 等注解。
   - 角色、权限信息由 JWT token 中携带，微服务通过 JwtUtil 解析。

3. **异常处理**
   - 所有认证和权限异常均由全局处理器统一返回标准结构，前端可直接识别。

4. **注意事项**
   - JWT 密钥需与 auth 服务保持一致。
   - 仅做 token 校验和权限控制，不负责用户登录、token 签发。

---

## 扩展建议

- 可根据业务需要扩展自定义权限注解、用户上下文工具类等。
- 如需自定义异常响应结构，可修改 RestAuthenticationEntryPoint 和 RestAccessDeniedHandler。

---

如有疑问或建议，请联系架构组。 