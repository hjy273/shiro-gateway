package com.geekerstar.gateway.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author geekerstar
 * @date 2020/4/13 14:24
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户登录响应对象")
public class AccountInfo {

    @ApiModelProperty("id,用户账号,主键")
    private Long id;

    @ApiModelProperty("用户名(nick_name)")
    private String username;

    @ApiModelProperty("密码(MD5(密码+盐))")
    private String password;

    @ApiModelProperty("盐")
    private String salt;

    @ApiModelProperty("用户真名")
    private String realName;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("电话号码(唯一)")
    private String phone;

    @ApiModelProperty("邮件地址(唯一)")
    private String email;

    @ApiModelProperty("性别(1.男、2.女)")
    private Integer sex;

    @ApiModelProperty("账户状态(1.正常、2.锁定、3.删除、4.非法)")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建来源(1.web、2.android、3.ios、4.win、5.macos、6.ubuntu)")
    private Integer createFrom;

    @ApiModelProperty("角色列表")
    private String RoleCodeList;

    @ApiModelProperty("Token")
    private String Token;

}
