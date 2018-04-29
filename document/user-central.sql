-- 用户账号表
create table t_user (
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
  third_id varchar(255) null comment '第三方平台ID',
  third_type tinyint null default 0 comment '第三方平台类型',
  avatar varchar(255) null comment '头像',
  status bit not null default 1 comment '账号状态 0-禁用 1-启用',
  create_time datetime not null default current_timestamp comment '创建时间',
  update_time datetime null on update current_timestamp comment '更新时间'
) engine Innodb charset utf8;

create unique index uniq_user_user_id on t_user(user_id);
create unique index uniq_user_username on t_user(username);
create unique index uniq_user_nickname on t_user(nickname);
create unique index uniq_user_phone on t_user(phone);
create index idx_user_username_password on t_user(username, password);

-- -------- 权限相关 ----------

-- 权限表
create table t_privilege (
  id int not null auto_increment primary key comment '主键ID',
  privilege_id varchar(64) not null comment '权限ID',
  parent_id varchar(64) null comment '入口权限ID',
  user_id varchar(64) not null comment '操作人ID',
  name varchar(255) not null comment '权限名称',
  url varchar(1000) not null comment '绑定地址',
  remark varchar(1000) null comment '备注',
  status bit not null default 1 comment '状态 0-禁用; 1-启用',
  create_time datetime not null default current_timestamp comment '创建时间',
  update_time datetime null on update current_timestamp comment '更新时间'
) engine Innodb charset utf8;

-- 权限组表
create table t_privilege_group (
  id int not null auto_increment primary key comment '主键ID',
  group_id varchar(64) not null comment '组ID',
  user_id varchar(64) not null comment '操作人ID',
  name varchar(255) not null comment '权限组名称',
  privilege_ids varchar(500) not null comment '权限ID列表',
  status bit not null default 1 comment '状态 0-禁用; 1-启用',
  create_time datetime not null default current_timestamp comment '创建时间',
  update_time datetime null on update current_timestamp comment '更新时间'
) engine Innodb charset utf8;

-- 用户权限表
create table t_user_privilege (
  id int not null auto_increment primary key comment '主键ID',
  user_id int not null comment '用户ID',
  privilege_id int not null comment '权限ID',
  status bit not null default 0 comment '状态 0-禁用; 1-启用',
  create_time datetime not null default current_timestamp comment '创建时间',
  update_time datetime null on update current_timestamp comment '更新时间'
) engine Innodb charset utf8;

-- 用户角色表
create table t_user_role (
  id int not null auto_increment primary key comment '主键ID',
  user_id int not null comment '用户ID',
  group_id int not null comment '权限组ID',
  create_time datetime not null default current_timestamp comment '创建时间'
) engine Innodb charset utf8;