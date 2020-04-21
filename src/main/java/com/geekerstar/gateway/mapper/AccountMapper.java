package com.geekerstar.gateway.mapper;

import com.geekerstar.gateway.vo.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author geekerstar
 * @date 2020/4/19 10:23
 * @description
 */
@Mapper
@Repository
public interface AccountMapper {

    String selectUserRoleByAppId(String appId);

    AccountInfo selectAccountInfoByAppId(String appId);

}
