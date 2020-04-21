create table auth_user
(
    id          varchar(30)  not null comment 'uid,用户账号,主键' primary key,
    username     varchar(30)  not null comment '用户名(nick_name)',
    password     varchar(50)  not null comment '密码(MD5(密码+盐))',
    salt         varchar(20)  null comment '盐',
    real_name    varchar(30)  null comment '用户真名',
    avatar       varchar(100) null comment '头像',
    phone        varchar(20)  null comment '电话号码(唯一)',
    email        varchar(50)  null comment '邮件地址(唯一)',
    sex          tinyint      null comment '性别(1.男、2.女)',
    status       tinyint      null comment '账户状态(1.正常、2.锁定、3.删除、4.非法)',
    create_time  datetime     null comment '创建时间',
    update_time  datetime     null comment '更新时间',
    create_where tinyint      null comment '创建来源(1.web、2.android、3.ios、4.win、5.macos、6.ubuntu)',
    constraint email unique (email),
    constraint phone unique (phone)
)
    comment '权限-用户信息表';

