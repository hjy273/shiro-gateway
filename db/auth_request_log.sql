create table auth_operation_log
(
    id          int(11) unsigned auto_increment comment '主键' primary key,
    log_name    varchar(255) null comment '日志名称',
    user_id     varchar(30)  null comment '用户id',
    api         varchar(255) null comment 'api名称',
    method      varchar(255) null comment '方法名称',
    create_time datetime     null comment '创建时间',
    is_success     tinyint      null comment '是否执行成功(0失败1成功)',
    message     varchar(255) null comment '具体消息'
)
comment '权限-api请求日志表';

