
-- 用户账号表
create table t_users (
  id int not null auto_increment primary key comment '主键ID',
  user_id varchar(64) not null comment '用户ID',
  username varchar(255) not null comment '账号',
  password varchar(255) not null comment '密码',
  salt varchar(255) not null comment '盐值',
  nickname varchar(255) not null comment '昵称',
  gender tinyint not null default 0 comment '性别 0-保密; 1-男; 2-女',
  id_card varchar(20) null comment '身份证号码',
  birth date null comment '出生日期',
  phone varchar(50) null comment '联系号码',
  email varchar(255) null comment '电子邮箱',
  wechat varchar(255) null comment '微信号',
  weibo varchar(1000) null comment '微博地址',
  province varchar(255) null comment '省份',
  city varchar(255) null comment '城市',
  address varchar(1000) null comment '地址',
  third_id varchar(255) null comment '第三方平台ID',
  third_type varchar(20) null comment '第三方平台类型 WECHAT WEIBO QQ ZHIHU GITHUB',
  avatar varchar(255) null comment '头像',
  status bit not null default 1 comment '账号状态 0-禁用 1-启用',
  create_time datetime not null default current_timestamp comment '创建时间',
  update_time datetime null on update current_timestamp comment '更新时间'
) engine Innodb charset utf8;

create unique index uniq_users_user_id on t_users(user_id);
create unique index uniq_users_username on t_users(username);
create unique index uniq_users_nickname on t_users(nickname);
create unique index uniq_users_phone on t_users(phone);
create index idx_users_username_password on t_users(username, password);

-- -------- 权限相关 ----------

-- 权限表（修改控制器访问路径后，必须要到后台系统同步修改权限路径）
create table t_privileges (
  id int not null auto_increment primary key comment '主键ID',
  privilege_id varchar(64) not null comment '权限ID',
  parent_id varchar(64) null comment '入口权限ID',
  oper_id varchar(64) not null comment '操作人ID',
  name varchar(255) not null comment '权限名称',
  url varchar(1000) not null comment '绑定地址',
  remark varchar(1000) null comment '备注',
  status bit not null default 1 comment '状态 0-禁用; 1-启用',
  create_time datetime not null default current_timestamp comment '创建时间',
  update_time datetime null on update current_timestamp comment '更新时间'
) engine Innodb charset utf8;

create index idx_privileges_privilege_id on t_privileges(privilege_id);
create index idx_privileges_oper_id on t_privileges(oper_id);

-- 权限组表（用户角色）
create table t_privilege_roles (
  id int not null auto_increment primary key comment '主键ID',
  role_id varchar(64) not null comment '角色ID',
  oper_id varchar(64) not null comment '操作人ID',
  name varchar(255) not null comment '角色名称',
  privilege_ids varchar(2000) not null comment '权限ID列表，逗号分隔',
  status bit not null default 1 comment '状态（是否可选） 0-禁用; 1-启用',
  create_time datetime not null default current_timestamp comment '创建时间',
  update_time datetime null on update current_timestamp comment '更新时间'
) engine Innodb charset utf8;

create index idx_privilege_roles_role_id on t_privilege_roles(role_id);
create index idx_privilege_roles_oper_id on t_privilege_roles(oper_id);

-- 用户权限明细表
create table t_user_privileges (
  id int not null auto_increment primary key comment '主键ID',
  user_id varchar(64) not null comment '用户ID',
  privilege_id varchar(64) not null comment '权限ID',
  create_time datetime not null default current_timestamp comment '创建时间',
  update_time datetime null on update current_timestamp comment '更新时间'
) engine Innodb charset utf8;

create index idx_user_privileges_user_id on t_user_privileges(user_id);

-- 用户和角色的关系表
create table t_user_role_relations (
  id int not null auto_increment primary key comment '主键ID',
  user_id varchar(64) not null comment '用户ID',
  role_id varchar(64) not null comment '角色ID',
  create_time datetime not null default current_timestamp comment '创建时间'
) engine Innodb charset utf8;

create index idx_user_role_relations_user_id on t_user_role_relations(user_id);
create index idx_user_role_relations_role_id on t_user_role_relations(role_id);

-- 子系统账号表
create table t_applications (
  id int not null auto_increment primary key comment '主键ID',
  oper_id varchar(64) not null comment '操作人ID',
  app_name varchar(255) not null comment '应用名称',
  app_key varchar(255) not null comment '账号',
  app_secret varchar(255) not null comment '密码',
  remark varchar(1000) null comment '备注',
  create_time datetime not null default current_timestamp comment '创建时间',
  update_time datetime null on update current_timestamp comment '更新时间'
) engine Innodb charset utf8;

create index idx_applications_oper_id on t_applications(oper_id);

