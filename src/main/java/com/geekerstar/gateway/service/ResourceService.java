package com.geekerstar.gateway.service;

import com.geekerstar.gateway.entity.RolePermRule;

import java.util.List;

/**
 * @author geekerstar
 * @date 2020/4/14 09:56
 * @description 动态过滤规则提供者接口
 */
public interface ResourceService {

    /**
     * 加载基于角色/资源的过滤规则
     * 即：用户-角色-资源（URL），对应关系存储与数据库中
     * 在shiro中生成的过滤器链为：url=jwt[角色1、角色2、角色n]
     *
     * @return
     */
    List<RolePermRule> getRolePermRules() throws Exception;
}
