package com.geekerstar.gateway.realm;

import com.geekerstar.gateway.entity.JwtToken;
import com.geekerstar.gateway.entity.PasswordToken;
import com.geekerstar.gateway.filter.AuthenticationMatcher;
import com.geekerstar.gateway.filter.AuthorizationMatcher;
import com.geekerstar.gateway.service.AccountService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author geekerstar
 * @date 2020/4/14 09:04
 * @description realm管理器
 */
@Component
@RequiredArgsConstructor
public class RealmManager {

    private final AuthenticationMatcher authenticationMatcher;
    private final AuthorizationMatcher authorizationMatcher;
    private final AccountService accountService;

    public List<Realm> initGetRealm() {
        List<Realm> realmList = Lists.newLinkedList();
        // 认证 Authentication
        AuthenticationRealm authenticationRealm = new AuthenticationRealm();
        authenticationRealm.setAccountService(accountService);
        authenticationRealm.setCredentialsMatcher(authenticationMatcher);
        authenticationRealm.setAuthenticationTokenClass(PasswordToken.class);
        realmList.add(authenticationRealm);
        // 授权 Authorization
        AuthorizationRealm authorizationRealm = new AuthorizationRealm();
        authorizationRealm.setCredentialsMatcher(authorizationMatcher);
        authorizationRealm.setAuthenticationTokenClass(JwtToken.class);
        realmList.add(authorizationRealm);
        return Collections.unmodifiableList(realmList);
    }
}
