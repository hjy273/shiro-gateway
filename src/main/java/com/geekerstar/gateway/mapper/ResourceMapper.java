package com.geekerstar.gateway.mapper;

import com.geekerstar.gateway.entity.RolePermRule;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author geekerstar
 * @date 2020/4/19 10:23
 * @description
 */
@Mapper
@Repository
public interface ResourceMapper {

    List<RolePermRule> selectRoleRules();

}
