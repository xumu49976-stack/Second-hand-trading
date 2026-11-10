# Maven 笔记 · 从"能用"到"知道为什么"

> 整理时间：2026-11-10
> 来源：第 11 周（11/09–11/15）Maven 环境打通，以及 11-10 晚的一次完整流程演示
> 用法：遇到"Maven 到底在干嘛""该点哪个按钮""为什么 jar 跑不起来"时，先查这里

---

## 0. 一句话版本

**Maven = 依赖管理 + 标准构建流程 + 目录约定。**

它不负责"运行程序"，负责**把程序准备好、并产出可交付的东西**。运行还是靠 IDEA 的 Run 或 `java` 命令。

---

## 1. 它到底"管理"什么

### 1.1 三件事

| 管什么 | 具体表现 | 解决什么问题 |
| --- | --- | --- |
| **依赖** | 在 `pom.xml` 写坐标，Maven 自动下载 jar 到本地仓库，并挂到 classpath | 不用手动下 jar、拖进 `lib`、配 Library、换电脑重来一遍 |
| **构建流程** | 一条标准流水线：编译 → 测试 → 打包 → 安装 → 发布 | 本地、IDEA、服务器/CI 用同一条命令，结果一致 |
| **目录约定** | `src/main/java`、`src/test/java`、`target/` | 拿到任何 Maven 项目都知道源码在哪，不用问 |

### 1.2 没有 Maven 的世界（以 commons-io 为例）

**手动方式：**

1. 浏览器搜 "commons-io jar"，找到官网下载 zip
2. 解压，把 jar 拖进项目建个 `lib` 文件夹
3. IDEA：`File → Project Structure → Libraries → +` 加进去
4. 这个 jar 自己还依赖别的库？再去下那几个
5. 换台电脑 → 全部重来
6. 别人 clone 你的代码 → 问你"要装哪些 jar？"

**Maven 方式：**

```xml
<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.22.0</version>
</dependency>
```

写 4 行、保存、reload，完事。别人 clone 下来 `mvn package`，自动下同样版本。

### 1.3 GAV 坐标

上面三行就是坐标，靠它**在全球仓库里唯一确定一个 jar**：

- `groupId`：组织（谁做的）
- `artifactId`：构件名（做的什么）
- `version`：版本

---

## 2. 项目结构约定

```
Second-hand trading\
├── pom.xml                    ← 项目身份证 + 依赖清单
├── src\
│   ├── main\
│   │   ├── java\              ← 生产代码（打包进 jar）
│   │   └── resources\         ← 配置文件（打包时复制进 jar）
│   └── test\
│       └── java\              ← 测试代码（只在跑测试时编译，不进 jar）
└── target\                    ← 构建产物（可随时删，Maven 会重建）
```

| 目录 | Maven 认为里面是什么 |
| --- | --- |
| `src/main/java` | 生产代码 —— 会被打包进最终 jar |
| `src/test/java` | 测试代码 —— 只在跑测试时编译，不进 jar |
| `src/main/resources` | 配置文件 —— 打包时复制进 jar |
| `target/` | 编译产物 —— 可随时删 |

**不按约定放会怎样**：Maven 根本不去看那个目录。**不是报"路径不对"，而是默默什么都不编译**，然后运行时报 `ClassNotFoundException`——比直接报错难查得多。这就是"约定优于配置"。

---

## 3. 生命周期

```
validate → compile → test → package → verify → install → deploy
（校验）   （编译）  （测）  （打包）  （验证）  （装进本地仓库）（发布到远程仓库）
```

**关键理解：它们是流水线上的阶段，不是独立命令。** 执行 `package` 时，Maven 会自动把前面的 `compile`、`test` 全跑一遍。

`clean` 是**另一条独立生命周期**（`pre-clean → clean → post-clean`），只负责删 `target\`，和上面那条线没有先后依赖。所以 `mvn clean package` 是"两个生命周期各跑各的"。

| 命令 | 干什么 | 产物 | 场景 |
| --- | --- | --- | --- |
| `compile` | 编译 `src/main/java` | `target\classes` | 几乎不用手动敲（IDEA 自动编译） |
| `test` | 编译 + 运行 `src/test` 里的测试 | `target\surefire-reports` | 写了 JUnit 测试之后 |
| `package` | 上面全部 + 打成 jar | `target\xxx.jar` | **最常用**：提交前、交付时 |
| `clean` | 删 `target\` | 无 | 怀疑产物脏了、换分支后行为诡异 |
| `install` | 打包 + 装进本地仓库 | `~\.m2\repository\com\campus\...` | 你的项目要被另一个项目当依赖引用 |
| `deploy` | 发布到远程仓库 | 私服 | 公司发版，现在用不到 |

**跳过测试的两种写法（以后会用到）：**

- `mvn package -DskipTests`：编译测试代码，但不运行
- `mvn package -Dmaven.test.skip=true`：连测试代码都不编译

---

## 4. 日常到底用哪个：右键 Run 还是 Maven 按钮？

### 4.1 它们不是二选一

```
pom.xml（唯一真相）
   ├── IDEA 读它 → 右键 Run 能用到依赖
   └── mvn 读它 → 命令行 package 也能用到依赖
```

**IDEA 的 Run 按钮没有魔法**，它做的事就是：

```bash
java -cp "target\classes;…所有依赖jar…" com.campus.trade.Main
```

`-cp`（classpath）告诉 JVM 去哪找类。IDEA 读 `pom.xml`，把依赖 jar 路径全部拼进去，然后启动 JVM。

### 4.2 决策表

| 你在干什么 | 用什么 | 为什么 |
| --- | --- | --- |
| 写代码、改 bug、打断点、看变量 | **右键 Run / Debug** | 快，改一行就能重跑 |
| 保存文件想确认有没有语法错 | IDEA 自动编译（或 `Ctrl+F9`） | 不用管 Maven |
| 每天收工 / 提交前，确认没搞坏 | `mvn clean package` | 从零构建，模拟别人的环境 |
| 要交给别人 / 部署，出 jar | `mvn package`，去 `target\` 拿 | 交付物标准路径 |
| 写了单元测试，想跑一遍 | `mvn test` 或点测试类绿三角 | 两者等价 |
| 怀疑"改了代码没生效" | `mvn clean package` | `clean` 强制从零 |

### 4.3 一天的流程图

```
写代码 ──▶ 保存（IDEA 自动编译）──▶ 右键 Run 看效果 ──▶ 打断点调试
                                          │
                                          ▼
                              今天收工 / 要提交了
                                          │
                                          ▼
                        mvn clean package（看到 BUILD SUCCESS）
                                          │
                             ┌────────────┴────────────┐
                             ▼                         ▼
                     只是提交代码              要交给别人 / 部署
                     git add / commit / push       去 target\ 拿 jar
                                                  （注意第 5 节的坑）
```

---

## 5. 演示实录（2026-11-10）：改了代码 → Run → 打包 → 跑 jar

在 `Main.java` 里临时加了两行（演示后已还原）：

```java
System.out.println("=== 演示：代码改动已生效（v0.2-demo）===");
FileUtils.writeStringToFile(new File("out/demo.txt"), "hello maven", StandardCharsets.UTF_8);
```

| 步骤 | 命令 | 结果 |
| --- | --- | --- |
| A 编译 | `mvn -q compile` | exit 0，`target\classes` 更新 |
| B 模拟右键 Run | `java -cp "target\classes;commons-io-2.22.0.jar" com.campus.trade.Main` | ✅ 打印出演示那行 |
| C 打包 | `mvn clean package` | ✅ BUILD SUCCESS，9.9 秒 |
| D 只拿 jar 跑 | `java -cp "target\second-hand-trading-1.0-SNAPSHOT.jar" com.campus.trade.Main` | ❌ `NoClassDefFoundError: org/apache/commons/io/FileUtils` |
| E jar + 依赖一起跑 | `java -cp "target\xxx.jar;commons-io-2.22.0.jar" com.campus.trade.Main` | ✅ 正常 |
| F 检查产物 | `out\demo.txt` | 内容 `hello maven` |

### 结论（今天最值得记住的一条）

**`package` 打出来的 jar 里只有你自己的 class，不包含依赖。**

自己验证：

```bash
jar tf target\second-hand-trading-1.0-SNAPSHOT.jar
```

只会看到 `com/campus/trade/*.class`，没有 `org/apache/commons/io/`。

所以"打成 jar 就能发给别人双击运行"是错觉。想让 jar 自带依赖，需要额外的 `maven-assembly-plugin` 或 `maven-shade-plugin`（打 fat jar / uber jar），以后学 Spring Boot 时会自动获得这个能力。

`NoClassDefFoundError` 的本质是：**不是代码错，是运行时 classpath 里找不到这个类。**

---

## 6. 本地仓库与镜像（本机实际配置）

文件：`E:\Develop_Java\apache-maven-3.10.0\conf\settings.xml`

```xml
<!-- 第 55 行 -->
<localRepository>E:\Develop_Java\maven-repository</localRepository>

<!-- 第 168 行左右：阿里云镜像 -->
<mirror>
  <id>aliyunmaven</id>
  <mirrorOf>central</mirrorOf>
  <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

| 概念 | 含义 |
| --- | --- |
| 本地仓库 | 下载过的 jar 都存这里，永久缓存。换项目不用重下 |
| 中央仓库 | Maven 官方的全球仓库（在国外，慢） |
| 镜像 | 中央仓库的国内副本（阿里云），速度从 45 kB/s → 11 MB/s |
| IDEA 的 Local repository | 必须和 `settings.xml` 指向同一个目录，否则命令行和 IDEA 各下一份 |

> 之前踩过的坑：`~\.m2\repository` 和 `E:\Develop_Java\maven-repository` 各有一份缓存（26 MB + 21 MB）。现在统一到 E 盘。

---

## 7. 踩过的坑清单（都是真实发生过的）

| # | 现象 | 原因 | 处理 |
| --- | --- | --- | --- |
| 1 | `mvn -v` 报 `JAVA_HOME is not defined correctly` | `JAVA_HOME` 值开头有一个**不可见字符 U+202A**，长度 22 而不是 21 | 用 `setx /M` 重设，或手打（别粘贴） |
| 2 | `FileNotFoundException: out\products.txt` | 相对路径跟着**当前工作目录**走，`FileWriter` 不会创建父目录 | `parent.mkdirs()` 兜底，或改用 commons-io |
| 3 | 项目树里文件名红棕色、Run 配置显示 `module not specified` | IDEA 没有模块（`.iml` / `modules.xml` 缺失） | Maven 面板 Reload，或重开项目触发导入 |
| 4 | 改完环境变量，旧 cmd 里 `mvn` 仍不可用 | 已打开的窗口用的是启动时的环境快照 | 关掉重开；IDEA 要完全退出重启 |
| 5 | 两个本地仓库各存一份依赖 | `settings.xml` 改了 `localRepository`，但 IDEA 没跟着变 | 统一到一处 |
| 6 | `warning: LF will be replaced by CRLF` | `core.autocrlf=true`，仓库存 LF、检出转 CRLF | 无害，可无视 |
| 7 | 单独跑 jar 报 `NoClassDefFoundError` | jar 不含依赖 | classpath 补依赖 jar，或将来打 fat jar |
| 8 | 移动 `src` 后文件名变红 | 包名与目录层级不对应 | 保证 `com/campus/trade` 三层目录完整 |

---

## 8. 常用命令速查

```bash
# 构建
mvn clean package              # 最常用：清空 + 编译 + 测试 + 打包
mvn -q package                 # 安静模式，只输出错误
mvn package -DskipTests        # 编译测试但不运行

# 排错
mvn -v                         # 版本 + JAVA_HOME + 本地仓库位置
mvn validate                   # 只校验 pom 结构（不产出文件）
mvn dependency:tree            # 看依赖树，排查版本冲突
mvn -X clean package           # 超详细日志，看"到底用了哪个仓库"

# 产物
jar tf target\xxx.jar          # 看 jar 里有什么

# 运行（打包后）
java -cp "target\xxx.jar;依赖.jar" com.campus.trade.Main
```

---

## 9. 现在还用不到、但迟早会遇到的

| 概念 | 什么时候需要 |
| --- | --- |
| fat jar / uber jar（shade、assembly 插件） | 要把"带依赖的完整程序"发给别人 |
| `mvn install` + 多模块 | 一个项目拆成多个模块互相引用 |
| `mvn deploy` + 私服（Nexus） | 团队内部共享构件 |
| profile | 开发/测试/生产用不同配置 |
| Maven Wrapper（`mvnw`） | 让别人不用装 Maven 也能构建 |

---

## 10. 一句话总结

> **写代码调试用 Run，收工交付用 `mvn clean package`。**
> Maven 不运行你的程序，它保证——**换台电脑、交给别人、放到服务器上，都能用同一条命令得到同样的结果。**
