package com.geekerstar.gateway.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author geekerstar
 * @date 2020/4/13 14:22
 * @description
 */
@Data
@ApiModel(value = "LoginRequest", description = "用户登录请求对象")
public class LoginRequest {

    @NotBlank(message = "用户唯一标识不能为空")
    @Length(min = 3, max = 20, message = "用户名长度限制为3~20字符")
    @ApiModelProperty("用户唯一标识")
    private String appId;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 100, message = "密码长度限制为6~20字符")
    private String password;

    @ApiModelProperty("请求方法，大写的HttpMethod，如GET、POST、PUT、DELETE等")
    @NotBlank(message = "请求方法不能为空")
    private String methodName;

    @ApiModelProperty("请求时间戳")
    private String timestamp;
}
