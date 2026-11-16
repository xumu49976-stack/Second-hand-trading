-- ============================================================
-- 校园二手交易与互助平台 · 数据库初始化脚本
-- 库名：second_hand   表名：product
-- 创建：2026-11-11（第 11 周 · 周三任务）
-- 说明：本脚本可重复执行（幂等）。每次执行会清空 product 表，
--       重新灌入 3 条示例数据，结果是确定的。
-- 用法：在 DBeaver 里打开本文件，按 Alt+X 执行整个脚本。
-- ============================================================

-- 1. 建库（已存在则跳过）
CREATE DATABASE IF NOT EXISTS second_hand
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

-- 2. 切到该库（后面对表的操作都作用在这里）
USE second_hand;

-- 3. 建表（已存在则跳过）
--    price 用 DECIMAL(10,2) 而不是 DOUBLE：
--    二进制浮点无法精确表示 0.1/0.2 这类十进制小数，累加会累积误差；
--    金额必须精确，用定点数。Java 侧对应 BigDecimal（第 13 周 JDBC 时会用到）。
CREATE TABLE IF NOT EXISTS product (
    id    BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键，自增',
    name  VARCHAR(100)    NOT NULL                  COMMENT '商品名称',
    price DECIMAL(10, 2)  NOT NULL                  COMMENT '价格（元），两位小数',
    stock INT             NOT NULL DEFAULT 0        COMMENT '库存数量'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COMMENT='商品表';

-- 4. 清空表（关键：让脚本可以反复执行，不会重复插入）
--    注意：TRUNCATE 会同时把 AUTO_INCREMENT 重置为 1。
--    对比 DELETE FROM product; —— 清数据但不重置自增，且可以带 WHERE、可以回滚。
TRUNCATE TABLE product;

-- 5. 初始数据（id 不写，由 AUTO_INCREMENT 分配）
INSERT INTO product (name, price, stock) VALUES
    ('康师傅红烧牛肉面', 4.50,  100),
    ('米雪',             34.00, 12),
    ('二手自行车',       199.00, 3);

-- 6. 验证
SELECT * FROM product;                  -- 应该 3 行，id = 1,2,3
SELECT COUNT(*) AS 总数 FROM product;    -- 应该是 3
DESC product;                           -- 字段类型、可空、默认值
SHOW CREATE TABLE product;              -- 完整建表语句（含引擎、字符集）
