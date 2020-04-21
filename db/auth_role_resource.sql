create table auth_role_resource
(
    id          int(11) unsigned auto_increment comment '主键' primary key,
    role_id     int      not null comment '角色ID',
    resource_id int      not null comment '资源ID',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '更新时间',
    constraint role_id unique (role_id, resource_id)
)
    comment '权限-资源角色关联表';

