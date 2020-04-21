package com.geekerstar.gateway.service.impl;

import com.geekerstar.gateway.entity.RolePermRule;
import com.geekerstar.gateway.mapper.ResourceMapper;
import com.geekerstar.gateway.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author geekerstar
 * @date 2020/4/14 09:59
 * @description
 */
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceMapper resourceMapper;

    @Override
    public List<RolePermRule> getRolePermRules() throws Exception {
        return resourceMapper.selectRoleRules();
    }
}
