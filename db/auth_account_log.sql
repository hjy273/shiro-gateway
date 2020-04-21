create table auth_account_log
(
    id          int(11) unsigned auto_increment  comment '主键' primary key,
    log_name    varchar(255) null comment '日志名称(login,register,logout)',
    user_id     varchar(30)  null comment '用户id',
    create_time datetime     null comment '创建时间',
    is_success     tinyint      null comment '是否执行成功(0失败1成功)',
    message     varchar(255) null comment '具体消息',
    ip          varchar(255) null comment '登录ip'
)
comment '权限-用户登录登出日志表';

