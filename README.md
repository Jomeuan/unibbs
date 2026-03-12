# unibbs

基于 **Java / Spring Boot & Spring Cloud** 的校内 BBS（论坛）平台项目，采用多模块/微服务拆分思路，面向“校内场景”的板块讨论、发帖回帖、身份权限（版主/会员/游客）等需求。

> 项目定位（来自文档与 POM 描述）：**基于 Java 的校内 BBS 平台**。  
> 目标要求：支持多个板块；区分版主/会员/游客权限；版主管理板块帖子，会员/版主可发帖跟帖，游客只读。

---

## 功能概览（规划中）

### 身份与权限
- **游客**：仅浏览
- **会员**：发帖、回帖、点赞等
- **版主**：除会员能力外，可对所辖板块内容进行管理（���删改查）

### 模块（按业务域）
- **security**
  - 用户注册
  - 用户登录
  - 修改密码
- **forum**
  - 板块 / 子版块管理（新增、修改、查询、搜索）
  - 版主变更
  - Thread（帖子合集/主题）管理：创建/删除/修改，向 Thread 添加/移除帖子
  - 帖子/评论：发布、查看列表、查看详情、修改、删除
  - 点赞/取消点赞
- **profile**
  - 历史发言
  - 历史点赞
  - 收藏
  - 修改个人信息

> 以上条目来自 `doc/TODO.md`，部分功能可能尚未完成或仍在迭代中。

---

## 技术栈

- **Java 21**
- **Spring Boot 3.2.x**
- **Spring Cloud 2023.0.0**
- **Nacos**（服务发现/配置）
- **Seata**（分布式事务）
- **MyBatis-Plus**（BOM 3.5.9）
- **Redis**
- **MySQL 5.7**
- **JWT**（jjwt 0.12.6）

---

## 仓库结构

仓库为多模块 Maven 工程（聚合 `pom`）：

- `unibbs/`：后端主工程目录（Maven 多模块）
  - `unibbs-security/`
  - `unibbs-gateway/`
  - `unibbs-forum/`
  - `unibbs-profile/`
  - `unibbs-common/`
  - `code-generator/`
- `docker/`：本地开发/部署相关（compose、脚本、Dockerfile）
- `doc/`：文档与 SQL
  - `unibbs.sql`：数据库初始化脚本
  - `cache.md`：缓存相关���明
  - `TODO.md`：需求/任务列表
- `lang-test/`：语言/实验目录（用途以实际内容为准）

---

## 快速开始（开发环境）

### 1) 环境要求
- JDK **21**
- Maven（或使用项目自带的 `mvnw`）
- Docker & Docker Compose（用于启动依赖：MySQL/Redis/Nacos/Seata）

### 2) 启动依赖（MySQL/Redis/Nacos/Seata）
仓库提供了 `docker/docker-compose.yml`：

```bash
cd docker
docker compose up -d
```

> Compose 中包含��
> - `mysql:5.7`（root 密码默认 `123456`）
> - `redis`
> - `nacos/nacos-server`
> - `seataio/seata-server`
> - `web`（会 `build: .`，具体镜像构建逻辑取决于 `docker/` 下 Dockerfile/上下文）

### 3) 初始化数据库
将 `doc/unibbs.sql` 导入 MySQL（示例命令仅供参考，按你的容器名/端口调整）：

```bash
# 进入 MySQL 容器（容器名以实际为准）
docker exec -it <mysql_container_name> mysql -uroot -p123456
# 然后执行 SQL（或用你习惯的 GUI 工具导入）
```

### 4) 构建与运行（Maven 多模块）
在后端工程目录执行构建：

```bash
cd unibbs
./mvnw -q -DskipTests package
```

运行方式取决于各模块的启动类（通常是 gateway/security/forum/profile 等服务分别启动）。
你可以在 IDE（IntelliJ IDEA）里按 Spring Boot 应用方式分别 Run，或使用 Maven 运行某个模块：

```bash
# 示例：运行某模块（以实际模块/启动方式为准）
./mvnw -pl unibbs-gateway spring-boot:run
```

---

## 配置说明

项目使用了 Nacos（discovery/config），通常需要：
- Nacos 服务地址
- 各服务的 `application.yml` / `bootstrap.yml` / Nacos 配置项
- 数据源、Redis、JWT 密钥等配置

建议先：
1. 启动 `docker compose` 中的 nacos
2. 查看各模块的配置文件（`unibbs/**/src/main/resources`）并按需补齐

---

## 常见问题（FAQ）

- **Dockerfile 里暴露端口是 9000，但 compose 映射的是 9010？**  
  以服务实际监听端口为准，建议检查各模块 `server.port` 配置，并统一 compose 与应用端口。

- **第一次跑不起来怎么办？**  
  建议按顺序排查：
  1) MySQL/Redis/Nacos/Seata 是否都健康启动  
  2) 数据库是否已导入 `doc/unibbs.sql`  
  3) Nacos 配置是否齐全（尤其是数据源、注册中心、网关路由等）

---

## 贡献

欢迎提交 Issue / PR：
- 修复 bug
- 完善 README / 文档
- 补齐 TODO 中的功能点
- 增加接口文档与部署说明

---

## License

本项目使用仓库根目录 `LICENSE` 所声明的许可证。
