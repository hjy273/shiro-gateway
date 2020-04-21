package com.geekerstar.gateway.service;

import com.geekerstar.gateway.entity.Account;
import com.geekerstar.gateway.entity.LoginRequest;
import com.geekerstar.gateway.vo.AccountInfo;

/**
 * @author geekerstar
 * @date 2020/4/13 14:20
 * @description
 */
public interface AccountService {

    AccountInfo login(LoginRequest loginRequest) throws Exception;

    /**
     * 获取账户信息
     *
     * @param appId
     * @return
     * @throws Exception
     */
    Account getAccount(String appId) throws Exception;

    String getAccountRole(String appId) throws Exception;
}
