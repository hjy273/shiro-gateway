package com.geekerstar.gateway.filter;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.stereotype.Component;

/**
 * @author geekerstar
 * @date 2020/4/14 09:29
 * @description 认证匹配器
 */
@Component
public class AuthenticationMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        return authenticationToken.getPrincipal().toString().equals(authenticationInfo.getPrincipals().getPrimaryPrincipal().toString()) && authenticationToken.getCredentials().toString().equals(authenticationInfo.getCredentials().toString());
    }
}
