-- 更新admin用户的密码为明文123456
USE academic_brain;

UPDATE t_user 
SET password = '123456' 
WHERE username = 'admin';

-- 验证更新结果
SELECT id, username, password FROM t_user WHERE username = 'admin'; 