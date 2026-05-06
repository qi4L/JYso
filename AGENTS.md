# JYso 项目指南

> 本文档面向 AI 编程助手。JYso 是一个将 ysoserial 与 JNDIExploit 功能整合为一体的 Java 安全研究工具，支持多种 JNDI 高版本 Bypass、WAF 绕过与 RASP 绕过。

---

## 项目概览

- **名称**: JYso
- **版本**: 1.3.8
- **组织**: `org.example`
- **主类**: `com.qi4l.JYso.Starter`
- **JDK 要求**: Java 8（`sourceCompatibility = JavaVersion.VERSION_1_8`）
- **构建工具**: Gradle 7.6.6（使用 Shadow 插件打包 Fat JAR）
- **默认构建产物**: `build/libs/JYso-1.3.8.jar`（通过 `shadowJar` 任务生成）

### 运行模式

本项目通过命令行参数区分三种运行模式：

1. **Web GUI 模式（默认）**
   - 启动参数：无参数，或 `-w`，或 `--web`
   - 行为：启动嵌入式 Jetty Web 服务器，提供基于 React 的前端图形化界面
   - 默认监听随机高位端口（10000~65535），可通过 `-wP <port>` 或 `--webPort <port>` 指定
   - 默认账号：`qi`，密码为程序随机生成并自动复制到剪贴板
   - 可同时附加 JNDI 服务：加上 `--with-jndi`

2. **JNDI 服务器模式**
   - 启动参数：`-j`
   - 行为：启动 LDAP、HTTP、RMI 服务，若配置了 TLS 则同时启动 LDAPS
   - 用于接收目标反连并下发 Payload

3. **ysoserial 命令行模式**
   - 启动参数：`-y`
   - 行为：像传统 ysoserial 一样生成指定 Gadget 的序列化 Payload 并输出到标准输出或文件

---

## 技术栈

### 后端

- **语言**: Java 8
- **构建**: Gradle 7.6.6 + Shadow Plugin 7.1.2
- **Web 容器**: Jetty 9.4.51（嵌入式，用于 Web GUI）
- **序列化/反序列化工具库**:
  - Apache Commons Collections 3.2.1 / 4.0
  - Spring Framework 5.3.31（AOP、Beans、Core、Tx）
  - Hibernate Core 4.3.11.Final
  - Jackson Databind 2.11.3
  - Fastjson 1.2.83
  - XStream 1.4.9
  - ROME / ROME Tools
  - SnakeYaml（间接使用）
- **JNDI/LDAP/RMI 相关**:
  - UnboundID LDAP SDK 4.0.9
  - JBoss Remoting / Remoting JMX
  - Jenkins Remoting 2.55
- **脚本/动态执行**:
  - Groovy 2.5.23
  - Jython Standalone 2.5.2
  - BeanShell 2.0b5
  - Mozilla Rhino JS 1.7R2
  - Clojure 1.8.0
- **其他关键依赖**:
  - Javassist 3.29.2-GA（动态字节码生成）
  - ASM 8.0.1
  - AspectJ Weaver 1.9.7
  - Hutool-all 5.7.7
  - JCommander 1.78（命令行参数解析）
  - Apache Tomcat Catalina / WebSocket 9.0.83
  - Undertow Core / Servlet 2.2.2.Final
  - C3P0 0.9.5.5
  - Vaadin Server 7.7.14
  - Apache Wicket Util 6.23.0
  - MyFaces Impl 2.2.9
  - Resin 4.0.65
  - Click NoDeps 2.3.0
  - Teradata JDBC 20.00.00.06
  - Log4j2 2.20.0（排除 logback）

### 前端

- **框架**: React 18 + Vite 5
- **路由**: React Router DOM v6
- **HTTP 客户端**: Axios
- **构建输出目录**: `src/main/resources/static`（Jetty 直接托管该目录）
- **开发代理**: Vite dev server 端口 3000，`/api` 代理到 `http://localhost:8080`

---

## 目录结构

```
src/
├── main/
│   ├── frontend/                # React + Vite 前端项目
│   │   ├── index.html
│   │   ├── package.json
│   │   ├── vite.config.js
│   │   └── src/
│   │       ├── App.jsx
│   │       ├── main.jsx
│   │       └── index.css
│   ├── java/com/qi4l/JYso/
│   │   ├── Starter.java          # 程序入口，模式分发
│   │   ├── HTTPServer.java       # HTTP 下载服务器（用于提供 class/bytecode）
│   │   ├── LdapServer.java       # LDAP 服务
│   │   ├── LdapsServer.java      # LDAPS 服务（TLS 转发）
│   │   ├── RMIServer.java        # RMI 服务
│   │   ├── controllers/          # JNDI/LDAP 路由控制器、XStream、SnakeYaml、JDBC、RMI 等
│   │   ├── gadgets/              # 反序列化 Gadget 链（~90 个实现）
│   │   │   ├── ObjectPayload.java
│   │   │   ├── Config/           # 全局配置（Config.java、ysoserial.java、MemShellPayloads.java）
│   │   │   ├── utils/            # 序列化、反射、字节码、混淆、脏数据填充等工具
│   │   │   └── annotation/       # @Authors、@Dependencies
│   │   ├── exploit/              # 独立漏洞利用模块（JRMP、JBoss、Jenkins、JSF 等）
│   │   ├── template/             # 内存马、反弹 Shell、Meterpreter、ClassLoader 等模板
│   │   ├── web/                  # Jetty Web GUI 启动与 Servlet/Filter
│   │   ├── enumtypes/            # GadgetType、PayloadType、WebsphereActionType
│   │   └── exceptions/           # 自定义异常
│   └── resources/
│       ├── application.properties  # Spring 风格配置（实际未使用 Spring Boot）
│       ├── log4j2.xml              # Log4j2 配置（仅 ERROR 级别输出到控制台）
│       └── static/                 # 前端构建产物（由 Vite 生成，Git 忽略）
└── test/java/
    ├── Main.java
    └── com/example/demo/demos/web/
        ├── Test.java
        └── secCig.java
```

---

## 构建与运行

### 构建 Fat JAR

```bash
./gradlew shadowJar
```

生成的可执行 JAR 位于 `build/libs/JYso-1.3.8.jar`（不带 classifier），包含所有依赖。

> 注意：`build.gradle` 中硬编码了 `compileJava` 的 `javac` 路径为 `/Users/qi4l/env/amazon-corretto-8.jdk/Contents/Home/bin/javac`。若在其他环境构建，可能需要修改或删除该配置以使用系统默认 JDK。

### 前端独立构建（开发时使用）

```bash
cd src/main/frontend
bun install   # 或 npm install
bun run dev   # 开发服务器，端口 3000
bun run build # 构建到 ../resources/static
```

### 常用运行命令

```bash
# Web GUI 模式（默认）
java -jar JYso-1.3.8.jar

# Web GUI 并附带 JNDI 服务
java -jar JYso-1.3.8.jar -w --with-jndi

# JNDI 服务器模式
java -jar JYso-1.3.8.jar -j -i 0.0.0.0 -lP 1389 -rP 1099 -hP 3456

# ysoserial 模式（查看支持列表）
java -jar JYso-1.3.8.jar -y -ga

# ysoserial 模式（生成指定 Gadget）
java -jar JYso-1.3.8.jar -y [GadgetName] '[命令]'
```

---

## 代码风格与约定

- **包名**: 全小写，使用 `com.qi4l.JYso` 作为根包。注意 `JYso` 包含大写字母。
- **类名**: 采用驼峰命名，Gadget 类名通常直接映射 ysoserial 命名（如 `cc2`、`cc3`、`Spring2`、`Hibernate2` 等）。
- **注释**: 项目中主要使用中文注释，少量英文。
- **字符串拼接**: 多处使用 `+` 拼接 SQL/JDBC URL，修改时需特别注意转义与注入安全（本工具本身即为利用场景）。
- **反射与字节码**: 大量使用 `javassist.ClassPool`、`org.reflections` 与原生反射，修改时需关注 Java 8 兼容性。
- **日志**: 使用 Log4j2，但多数类直接通过 `System.out.println` 输出交互信息。

---

## 测试策略

- **测试框架**: 本项目几乎没有正式单元测试。
- `src/test/java/` 下仅有 3 个文件：`Main.java`、`Test.java`、`secCig.java`，内容多为临时验证代码或片段测试。
- **验证方式**: 修改后通常通过 `./gradlew shadowJar` 打包，然后直接运行 JAR 在目标场景或本地环境中验证。

---

## 安全与合规声明

- **本工具仅用于授权的安全测试、漏洞研究与教育目的。**
- 项目整合了大量公开已知的反序列化 Gadget 链、JNDI/LDAP/RMI 注入利用、内存马植入、JDBC 攻击等技术。
- 代码中包含生成恶意序列化数据、启动恶意协议服务器、动态生成混淆类名、脏数据填充、UTF-8 Overlong Encoding 绕过 WAF 等功能。
- **禁止在未经授权的系统上使用。使用者需自行承担法律与合规责任。**
- 部分依赖库包含已知历史漏洞（如 CommonsCollections 3.2.1、Fastjson 1.2.83 等），这是工具设计所需，而非项目自身漏洞。

---

## 修改建议

- 若需新增 Gadget，实现 `ObjectPayload<T>` 接口，放置在 `com.qi4l.JYso.gadgets` 包下，程序启动时会通过 `org.reflections` 自动扫描并注册。
- 若需新增 JNDI 路由或控制器，参考 `BaseLdapController` 与 `LdapMapping` 的映射方式。
- 若修改前端，直接编辑 `src/main/frontend/src/`，构建后产出到 `src/main/resources/static` 即可被 Jetty 托管。
- 若修改构建配置，注意 Shadow 插件的 `mergeServiceFiles()` 与 `zip64 = true` 设置，以避免大 JAR 中的 META-INF 服务文件冲突或 ZIP 条目数超限。
