# 校园二手交易与互助平台

长期主项目。按固定版本迭代，**每个版本只做当前能力范围内的功能**，不把项目变成第二套庞大课程。

- 路线与验收标准：[docs/学习路线-初步版.md](docs/学习路线-初步版.md)
- **可执行任务大纲**：[docs/学习任务大纲.md](docs/学习任务大纲.md) ← 集数、时长、阶段计划
- **每日任务**：[docs/每日任务.md](docs/每日任务.md) ← 每天改这一个
- 实际进度记录：[docs/进度记录.md](docs/进度记录.md)

## 当前版本

**V0.1 商品管理程序** —— 控制台版，纯 Java，无 Maven、无数据库、无网页。

## 目录结构

```text
Second-hand trading/
├── docs/                              学习路线与进度记录
├── src/com/campus/trade/
│   ├── Main.java                      程序入口，负责组织流程
│   ├── ProductManager.java            负责新增、展示、查询、删除商品
│   └── Product.java                   表示一个具体的商品
├── out/                               编译输出（已被 .gitignore 忽略）
├── .gitignore
└── second-hand-trading.iml            IDEA 模块配置
```

## 怎么运行

### 在 IDEA 里

打开本目录（IDEA 会按 `second-hand-trading.iml` 识别为 Java 项目），
然后运行 `src/com/campus/trade/Main.java`。

### 命令行

```powershell
# 中文输出先把控制台切成 UTF-8，否则会乱码
chcp 65001

# 编译：所有源文件编译到 out 目录
javac -encoding UTF-8 -d out src\com\campus\trade\*.java

# 运行：类名要用全限定名
java -cp out com.campus.trade.Main
```

## 版本路线

| 版本 | 内容 | 对应阶段 |
|---|---|---|
| **V0.1**（当前） | 控制台实现商品新增、展示、查询、删除 | 第 1–4 周 · Java 基础 |
| V0.2 | 商品存入 MySQL，Spring Boot 提供增删改查接口 | 第 9–13 周 · MySQL + Spring Boot |
| V0.3 | 用户注册登录、发布商品、详情、搜索、分页、权限 | 第 13–18 周 |
| V1.0 | 订单流程、管理员、日志、测试、部署 | 第 19–24 周 |
| V2.0 | 校园互助、站内消息、Redis、AI 推荐/问答 | 后续 |

> V0.1 里写的 `Product` 类不会白写：到 V0.2 它就是 `product` 表对应的实体类，
> 字段名基本不变。这是同一栋楼的第一层，不是练手作业。

## 技术栈演进

| 阶段 | 技术栈 | 说明 |
|---|---|---|
| V0.1 | JDK 17 + 纯 Java | 不需要 Maven，`javac`/`java` 足够 |
| V0.2 起 | + Maven + MySQL + Spring Boot 3 | 学完第 5–8 周的 Maven 与第 9–12 周的 MySQL 再加入 |
| V1.0 起 | + Redis + 单元测试 | 按路线第 19–24 周引入 |

## 提交前检查

- [ ] 代码能编译、能运行，没有留在控制台的调试输出
- [ ] 新功能自己测过：正常输入 + 至少一种非法输入
- [ ] `.gitignore` 里的东西没被提交（`out/`、`.idea/`、`*.iml`）
- [ ] 进度记录里补一条
