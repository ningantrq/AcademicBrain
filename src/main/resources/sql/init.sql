-- 创建数据库
CREATE DATABASE IF NOT EXISTS academic_brain DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE academic_brain;

-- 创建用户表
CREATE TABLE IF NOT EXISTS t_user (
    id VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    avatar VARCHAR(255) DEFAULT NULL COMMENT '头像',
    gender TINYINT DEFAULT 0 COMMENT '性别 0-女 1-男',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    description TEXT DEFAULT NULL COMMENT '个人描述',
    status TINYINT DEFAULT 0 COMMENT '状态 0-正常 1-禁用',
    birthday VARCHAR(20) DEFAULT NULL COMMENT '生日',
    address VARCHAR(255) DEFAULT NULL COMMENT '地址',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入测试用户数据
INSERT INTO t_user (id, username, password, avatar, gender, phone, email, description, status) VALUES
('test001', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKTnl5iHkE6wJP7EE9b7LKTNWLjG', NULL, 1, '13800138000', 'admin@test.com', '系统管理员', 0),
('test002', 'user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKTnl5iHkE6wJP7EE9b7LKTNWLjG', NULL, 0, '13800138001', 'user1@test.com', '普通用户1', 0);

-- 注意：上面的密码是 "123456" 经过BCrypt加密后的结果 