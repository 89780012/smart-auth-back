-- 用户信息表
CREATE TABLE users (
                       user_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户的唯一标识符',
                       username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名，必须唯一',
                       password_hash VARCHAR(255) NOT NULL COMMENT '存储用户密码的哈希值',
                       profile VARCHAR(255) COMMENT '用户头像',
                       email VARCHAR(100) NOT NULL UNIQUE COMMENT '用户的电子邮件地址，必须唯一',
                       phone VARCHAR(20) COMMENT '用户的手机号码',
                       wechat_id VARCHAR(50) UNIQUE COMMENT '用户的微信ID，必须唯一',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '用户记录的创建时间',
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '用户记录的最后更新时间',
                       is_active TINYINT DEFAULT 1 COMMENT '用户账户是否激活，1:激活 0:未生效'
) COMMENT='用户信息表';

-- 角色信息表
CREATE TABLE `roles` (
                         `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色的唯一标识符',
                         `role_code` varchar(50) NOT NULL UNIQUE  COMMENT '角色编码，必须唯一',
                         `role_name` varchar(50) NOT NULL COMMENT '角色名称',
                         `description` text COMMENT '角色描述',
                         `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '角色记录的创建时间',
                         `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '角色记录的最后更新时间',
                         PRIMARY KEY (`role_id`),
                         UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- 权限信息表
CREATE TABLE permissions (
                             permission_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '权限的唯一标识符',
                             permission_code VARCHAR(50) NOT NULL UNIQUE COMMENT '权限编码，必须唯一',
                             permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
                             description TEXT COMMENT '权限描述',
                             resource_code INT COMMENT '关联的资源标识符',
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '权限记录的创建时间',
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '权限记录的最后更新时间'
) COMMENT='权限信息表';

-- 资源信息表
CREATE TABLE resources (
                           resource_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '资源的唯一标识符',
                           resource_code VARCHAR(50) NOT NULL UNIQUE COMMENT '资源的唯一标识符',
                           resource_name VARCHAR(100) NOT NULL COMMENT '资源名称',
                           resource_type ENUM('page', 'operation', 'data') NOT NULL COMMENT '资源类型，支持页面权限、操作权限和数据权限',
                           parent_id INT DEFAULT NULL COMMENT '父资源ID，用于构建菜单树',
                           path VARCHAR(100) COMMENT '路由路径',
                           component VARCHAR(200) COMMENT '组件路径',
                           icon VARCHAR(50) COMMENT '菜单图标',
                           layout VARCHAR(20) DEFAULT 'default' COMMENT '布局类型：full/default',
                           sort_order INT DEFAULT 0 COMMENT '排序号',
                           hidden TINYINT(4) DEFAULT 0 COMMENT '是否隐藏菜单 1:隐藏 0:显示',
                           redirect VARCHAR(100) COMMENT '重定向路径',
                           is_active TINYINT(4) DEFAULT 1 COMMENT '1:启用 0:禁用',
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '资源记录的创建时间',
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '资源记录的最后更新时间',
                           FOREIGN KEY (parent_id) REFERENCES resources(resource_id) ON DELETE CASCADE
) COMMENT='资源信息表';

-- 用户与角色关联表
CREATE TABLE user_roles (
                            user_id INT COMMENT '用户的唯一标识符',
                            role_id INT COMMENT '角色的唯一标识符',
                            PRIMARY KEY (user_id, role_id) COMMENT '复合主键，确保用户和角色的唯一组合'
) COMMENT='用户与角色关联表';

-- 角色与权限关联表
CREATE TABLE role_permissions (
                                  role_id INT COMMENT '角色的唯一标识符',
                                  permission_id INT COMMENT '权限的唯一标识符',
                                  PRIMARY KEY (role_id, permission_id) COMMENT '复合主键，确保角色和权限的唯一组合'
) COMMENT='角色与权限关联表';

-- 权限资源关联表
CREATE TABLE permission_resources (
                                  role_id INT COMMENT '角色的唯一标识符',
                                  permission_id INT COMMENT '权限的唯一标识符',
                                  PRIMARY KEY (role_id, permission_id) COMMENT '复合主键，确保角色和权限的唯一组合'
) COMMENT='角色与权限关联表';