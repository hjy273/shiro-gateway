create table auth_resource
(
    id          int(11) unsigned auto_increment comment '主键-资源id' primary key,
    code        varchar(30)           null comment '资源码值',
    name        varchar(30)           null comment '资源名称',
    parent_id   int                   null comment '父资源id',
    url         varchar(100)          null comment '访问地址URL',
    type        smallint(4)           null comment '类型(1:菜单menu、2:资源element(rest-api)、3:资源分类)',
    method      varchar(10)           null comment '访问方式(GET POST PUT DELETE PATCH)',
    icon        varchar(100)          null comment '图标',
    status      smallint(4) default 1 null comment '状态(0:删除、1:正常、2：禁用)',
    create_time datetime              null comment '创建时间',
    update_time datetime              null comment '更新时间'
)
comment '权限-资源信息表';

