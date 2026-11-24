-- 建用户表
CREATE TABLE IF NOT EXISTS users (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键，自增',
    username      VARCHAR(100)    UNIQUE NOT NULL              COMMENT '登录用户名，唯一',
    password_hash VARCHAR(255)     NOT NULL                     COMMENT '密码哈希值（Bcrypt）',
    nickname      VARCHAR(100)    NOT NULL                     COMMENT '用户昵称',
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COMMENT='用户表';