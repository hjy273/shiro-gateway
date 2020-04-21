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
 * 权限-资源信息表
 * </p>
 *
 * @author Geekerstar
 * @since 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AuthResource对象", description = "权限-资源信息表")
public class AuthResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资源码值")
    private String resourceCode;

    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "父资源id")
    private Integer parentId;

    @ApiModelProperty(value = "访问地址URL")
    private String url;

    @ApiModelProperty(value = "类型(1:菜单menu、2:资源element(rest-api)、3:资源分类)")
    private Integer type;

    @ApiModelProperty(value = "访问方式(GET POST PUT DELETE PATCH)")
    private String method;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "状态(0:删除、1:正常、2：禁用)")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
