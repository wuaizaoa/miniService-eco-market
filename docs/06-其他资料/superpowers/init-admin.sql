-- 初始化管理员账号
-- 账号: admin
-- 密码: admin123 (BCrypt加密后)
-- 角色: 1 (管理员)

INSERT INTO t_user (username, password, role, created_at, updated_at)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 1, NOW(), NOW());
