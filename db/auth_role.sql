create table auth_role
(
    id          int(11) unsigned auto_increment comment '主键-角色id' primary key,
    code        varchar(30)           not null comment '角色编码',
    name        varchar(30)           null comment '角色名称',
    status      smallint(4) default 1 null comment '状态(0:删除、1:正常、2：禁用)',
    create_time datetime              null comment '创建时间',
    update_time datetime              null comment '更新时间'
)
comment '角色信息表';

