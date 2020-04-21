package com.geekerstar.gateway.service.impl;


import com.geekerstar.gateway.entity.Account;
import com.geekerstar.gateway.entity.LoginRequest;
import com.geekerstar.gateway.mapper.AccountMapper;
import com.geekerstar.gateway.service.AccountService;
import com.geekerstar.gateway.util.JsonWebTokenUtil;
import com.geekerstar.gateway.vo.AccountInfo;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.geekerstar.gateway.constant.CommonConstant.*;

/**
 * @author geekerstar
 * @date 2020/4/13 14:20
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public AccountInfo login(LoginRequest loginRequest) {
        // TODO 首先从Redis获取用户信息，不存在则从数据库获取，然后存一份到Redis，当部门角色等发生变更时清楚Redis缓存信息
        String appId = loginRequest.getAppId();
        String roles = accountMapper.selectUserRoleByAppId(appId);
        long refreshPeriodTime = 36000L;
        String jwt = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(), appId,
                TOKEN_ISSUER, refreshPeriodTime >> 1, roles, null, SignatureAlgorithm.HS512);
        stringRedisTemplate.opsForValue().set(TOKEN_PREFIX + appId, jwt, refreshPeriodTime, TimeUnit.SECONDS);
        AccountInfo accountInfo = accountMapper.selectAccountInfoByAppId(appId);
        accountInfo.setPassword(null);
        accountInfo.setToken(jwt);
        accountInfo.setRoleCodeList(roles);
        return accountInfo;
    }

    @Override
    public Account getAccount(String appId) {
        AccountInfo accountInfo = accountMapper.selectAccountInfoByAppId(appId);
        return accountInfo != null ? new Account(accountInfo.getUsername(), accountInfo.getPassword(), SALT) : null;
    }

    @Override
    public String getAccountRole(String appId) {
        return accountMapper.selectUserRoleByAppId(appId);
    }


}
