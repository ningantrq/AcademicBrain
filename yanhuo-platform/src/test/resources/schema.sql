-- H2数据库兼容的测试表结构

-- 用户表
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    id VARCHAR(50) NOT NULL,
    yx_id BIGINT DEFAULT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) DEFAULT NULL,
    avatar VARCHAR(225) DEFAULT NULL,
    gender TINYINT DEFAULT NULL,
    phone VARCHAR(50) DEFAULT NULL,
    email VARCHAR(100) DEFAULT NULL,
    description LONGTEXT,
    status TINYINT DEFAULT NULL,
    user_cover LONGTEXT,
    birthday VARCHAR(50) DEFAULT NULL,
    address VARCHAR(50) DEFAULT NULL,
    trend_count BIGINT DEFAULT 0,
    follower_count BIGINT DEFAULT 0,
    fan_count BIGINT DEFAULT 0,
    creator VARCHAR(50) DEFAULT NULL,
    create_date DATETIME DEFAULT NULL,
    updater VARCHAR(50) DEFAULT NULL,
    update_date DATETIME DEFAULT NULL,
    PRIMARY KEY (id)
);

-- 笔记表
DROP TABLE IF EXISTS t_note;
CREATE TABLE t_note (
    id VARCHAR(50) NOT NULL,
    title VARCHAR(255) DEFAULT NULL,
    content LONGTEXT,
    note_cover VARCHAR(500) DEFAULT NULL,
    note_cover_height INT DEFAULT NULL,
    uid VARCHAR(50) DEFAULT NULL,
    cid VARCHAR(50) DEFAULT NULL,
    cpid VARCHAR(50) DEFAULT NULL,
    urls LONGTEXT,
    count INT DEFAULT NULL,
    sort INT DEFAULT NULL,
    pinned INT DEFAULT NULL,
    status INT DEFAULT NULL,
    type INT DEFAULT NULL,
    view_count BIGINT DEFAULT 0,
    like_count BIGINT DEFAULT 0,
    collection_count BIGINT DEFAULT 0,
    comment_count BIGINT DEFAULT 0,
    creator VARCHAR(50) DEFAULT NULL,
    create_date DATETIME DEFAULT NULL,
    updater VARCHAR(50) DEFAULT NULL,
    update_date DATETIME DEFAULT NULL,
    PRIMARY KEY (id)
);

-- 点赞收藏表
DROP TABLE IF EXISTS t_like_or_collection;
CREATE TABLE t_like_or_collection (
    id VARCHAR(50) NOT NULL,
    uid VARCHAR(50) DEFAULT NULL,
    like_or_collection_id VARCHAR(50) DEFAULT NULL,
    publish_uid VARCHAR(50) DEFAULT NULL,
    type INT DEFAULT NULL,
    timestamp BIGINT DEFAULT NULL,
    creator VARCHAR(50) DEFAULT NULL,
    create_date DATETIME DEFAULT NULL,
    updater VARCHAR(50) DEFAULT NULL,
    update_date DATETIME DEFAULT NULL,
    PRIMARY KEY (id)
);

-- 专辑表
DROP TABLE IF EXISTS t_album;
CREATE TABLE t_album (
    id VARCHAR(50) NOT NULL,
    title VARCHAR(255) DEFAULT NULL,
    uid VARCHAR(50) DEFAULT NULL,
    album_cover VARCHAR(500) DEFAULT NULL,
    type INT DEFAULT NULL,
    sort INT DEFAULT NULL,
    img_count INT DEFAULT 0,
    collection_count INT DEFAULT 0,
    creator VARCHAR(50) DEFAULT NULL,
    create_date DATETIME DEFAULT NULL,
    updater VARCHAR(50) DEFAULT NULL,
    update_date DATETIME DEFAULT NULL,
    PRIMARY KEY (id)
);

-- 标签笔记关联表
DROP TABLE IF EXISTS t_tag_note_relation;
CREATE TABLE t_tag_note_relation (
    id VARCHAR(50) NOT NULL,
    nid VARCHAR(50) DEFAULT NULL,
    tid VARCHAR(50) DEFAULT NULL,
    creator VARCHAR(50) DEFAULT NULL,
    create_date DATETIME DEFAULT NULL,
    updater VARCHAR(50) DEFAULT NULL,
    update_date DATETIME DEFAULT NULL,
    PRIMARY KEY (id)
);

-- 标签表
DROP TABLE IF EXISTS t_tag;
CREATE TABLE t_tag (
    id VARCHAR(50) NOT NULL,
    title VARCHAR(255) DEFAULT NULL,
    like_count BIGINT DEFAULT 0,
    sort INT DEFAULT NULL,
    creator VARCHAR(50) DEFAULT NULL,
    create_date DATETIME DEFAULT NULL,
    updater VARCHAR(50) DEFAULT NULL,
    update_date DATETIME DEFAULT NULL,
    PRIMARY KEY (id)
);

-- 分类表 - 添加缺失的字段
DROP TABLE IF EXISTS t_category;
CREATE TABLE t_category (
    id VARCHAR(50) NOT NULL,
    title VARCHAR(255) DEFAULT NULL,
    pid VARCHAR(50) DEFAULT NULL,
    description LONGTEXT DEFAULT NULL,
    sort INT DEFAULT NULL,
    icon VARCHAR(255) DEFAULT NULL,
    normal_cover VARCHAR(500) DEFAULT NULL,
    hot_cover VARCHAR(500) DEFAULT NULL,
    note_count BIGINT DEFAULT 0,
    like_count BIGINT DEFAULT 0,
    creator VARCHAR(50) DEFAULT NULL,
    create_date DATETIME DEFAULT NULL,
    updater VARCHAR(50) DEFAULT NULL,
    update_date DATETIME DEFAULT NULL,
    PRIMARY KEY (id)
);

-- 专辑笔记关联表
DROP TABLE IF EXISTS t_album_note_relation;
CREATE TABLE t_album_note_relation (
    id VARCHAR(50) NOT NULL,
    aid VARCHAR(50) DEFAULT NULL,
    nid VARCHAR(50) DEFAULT NULL,
    creator VARCHAR(50) DEFAULT NULL,
    create_date DATETIME DEFAULT NULL,
    updater VARCHAR(50) DEFAULT NULL,
    update_date DATETIME DEFAULT NULL,
    PRIMARY KEY (id)
);

-- 插入测试数据
INSERT INTO t_user (id, username, password, avatar, gender, phone, email, description, status, trend_count, follower_count, fan_count, creator, create_date, updater, update_date) 
VALUES ('test-user-1', 'testuser', 'password', 'avatar.jpg', 1, '12345678901', 'test@test.com', 'Test user description', 1, 0, 0, 0, 'system', NOW(), 'system', NOW());

INSERT INTO t_category (id, title, pid, description, sort, icon, normal_cover, hot_cover, note_count, like_count, creator, create_date, updater, update_date)
VALUES 
('test-category-1', 'Test Category', NULL, 'Test category description', 1, 'icon.jpg', 'normal_cover.jpg', 'hot_cover.jpg', 1, 0, 'system', NOW(), 'system', NOW()),
('test-category-2', 'Test Sub Category', 'test-category-1', 'Test sub category description', 1, 'icon2.jpg', 'normal_cover2.jpg', 'hot_cover2.jpg', 1, 0, 'system', NOW(), 'system', NOW());

INSERT INTO t_note (id, title, content, note_cover, uid, cid, cpid, type, view_count, like_count, collection_count, comment_count, creator, create_date, updater, update_date)
VALUES ('test-note-1', 'Test Note', 'Test content', 'cover.jpg', 'test-user-1', 'test-category-2', 'test-category-1', 1, 0, 0, 0, 0, 'system', NOW(), 'system', NOW());

INSERT INTO t_like_or_collection (id, uid, like_or_collection_id, publish_uid, type, timestamp, creator, create_date, updater, update_date)
VALUES ('test-like-1', 'test-user-1', 'test-note-1', 'test-user-1', 1, 1677654321, 'system', NOW(), 'system', NOW());

INSERT INTO t_album (id, title, uid, album_cover, type, sort, img_count, collection_count, creator, create_date, updater, update_date)
VALUES ('test-album-1', 'Test Album', 'test-user-1', 'album-cover.jpg', 1, 1, 1, 0, 'system', NOW(), 'system', NOW());

INSERT INTO t_tag (id, title, like_count, sort, creator, create_date, updater, update_date)
VALUES ('test-tag-1', 'Test Tag', 0, 1, 'system', NOW(), 'system', NOW());

INSERT INTO t_tag_note_relation (id, nid, tid, creator, create_date, updater, update_date)
VALUES ('test-tag-note-1', 'test-note-1', 'test-tag-1', 'system', NOW(), 'system', NOW());

INSERT INTO t_album_note_relation (id, aid, nid, creator, create_date, updater, update_date)
VALUES ('test-album-note-1', 'test-album-1', 'test-note-1', 'system', NOW(), 'system', NOW()); 