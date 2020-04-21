package com.geekerstar.gateway.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 权限-用户信息表
 * </p>
 *
 * @author Geekerstar
 * @since 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AuthUser对象", description = "权限-用户信息表")
public class AuthUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名(nick_name)")
    private String username;

    @ApiModelProperty(value = "密码(MD5(密码+盐))")
    private String password;

    @ApiModelProperty(value = "盐")
    private String salt;

    @ApiModelProperty(value = "用户真名")
    private String realName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "电话号码(唯一)")
    private String phone;

    @ApiModelProperty(value = "邮件地址(唯一)")
    private String email;

    @ApiModelProperty(value = "性别(1.男、2.女)")
    private Integer sex;

    @ApiModelProperty(value = "账户状态(1.正常、2.锁定、3.删除、4.非法)")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建来源(1.web、2.android、3.ios、4.win、5.macos、6.ubuntu)")
    private Integer createFrom;


}
