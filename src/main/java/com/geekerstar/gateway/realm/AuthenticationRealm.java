package com.geekerstar.gateway.realm;

import com.geekerstar.gateway.entity.Account;
import com.geekerstar.gateway.entity.PasswordToken;
import com.geekerstar.gateway.service.AccountService;
import com.geekerstar.gateway.util.Md5Util;
import lombok.SneakyThrows;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author geekerstar
 * @date 2020/4/14 10:38
 * @description 认证Realm
 */
public class AuthenticationRealm extends AuthorizingRealm {

    private AccountService accountService;

    /**
     * 获取PasswordToken
     *
     * @return
     */
    @Override
    public Class<?> getAuthenticationTokenClass() {
        return PasswordToken.class;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 这里不做授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证登录，成功后派发Token
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @SneakyThrows
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (!(authenticationToken instanceof PasswordToken)) {
            return null;
        }
        if (null == authenticationToken.getPrincipal() || null == authenticationToken.getCredentials()) {
            throw new UnknownAccountException();
        }
        String appId = (String) authenticationToken.getPrincipal();
        Account account = accountService.getAccount(appId);
        if (account != null) {
            ((PasswordToken) authenticationToken).setPassword(Md5Util.md5(((PasswordToken) authenticationToken).getPassword() + account.getSalt()));
            return new SimpleAuthenticationInfo(appId, account.getPassword(), getName());
        } else {
            return new SimpleAuthenticationInfo(appId, "", getName());
        }
    }
}
