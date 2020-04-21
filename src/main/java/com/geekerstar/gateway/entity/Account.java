package com.geekerstar.gateway.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author geekerstar
 * @date 2020/4/14 09:40
 * @description
 */
@Data
@AllArgsConstructor
@ApiModel("账户权限验证实体")
public class Account {

    @ApiModelProperty("登录凭证：民警编号，电话等等，可扩充")
    private String appId;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("盐值")
    private String salt;
}
