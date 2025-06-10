-- H2数据库初始化脚本
-- 用户表
DROP TABLE IF EXISTS t_user;

CREATE TABLE t_user (
    id VARCHAR(50) PRIMARY KEY,
    yx_id BIGINT,
    username VARCHAR(100),
    password VARCHAR(255),
    avatar VARCHAR(500),
    gender INTEGER,
    phone VARCHAR(20),
    email VARCHAR(100),
    description TEXT,
    status INTEGER DEFAULT 1,
    birthday VARCHAR(20),
    address VARCHAR(500),
    user_cover VARCHAR(500),
    trend_count BIGINT DEFAULT 0,
    follower_count BIGINT DEFAULT 0,
    fan_count BIGINT DEFAULT 0,
    creator VARCHAR(50),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updater VARCHAR(50),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_user_username ON t_user(username);
CREATE INDEX idx_user_email ON t_user(email);
CREATE INDEX idx_user_status ON t_user(status); 