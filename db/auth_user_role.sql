create table auth_user_role
(
    id          int(11) unsigned auto_increment comment '主键' primary key,
    user_id     varchar(30) not null comment '用户UID',
    role_id     int         not null comment '角色ID',
    create_time datetime    null comment '创建时间',
    update_time datetime    null comment '更新时间',
    constraint user_id unique (user_id, role_id)
)
    comment '权限-用户角色关联表';

