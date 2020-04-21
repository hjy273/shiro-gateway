package com.geekerstar.gateway.filter;

import com.geekerstar.gateway.entity.JwtAccount;
import com.geekerstar.gateway.util.JsonWebTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.stereotype.Component;

import static com.geekerstar.gateway.constant.CommonConstant.TOKEN_ERROR;
import static com.geekerstar.gateway.constant.CommonConstant.TOKEN_EXPIRED;

/**
 * @author geekerstar
 * @date 2020/4/14 09:06
 * @description 授权匹配器
 */
@Component
public class AuthorizationMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        String jwtToken = (String) authenticationInfo.getCredentials();
        JwtAccount jwtAccount;
        try {
            jwtAccount = JsonWebTokenUtil.parseJwt(jwtToken, JsonWebTokenUtil.SECRET_KEY);
        } catch (ExpiredJwtException e) {
            // 令牌过期
            throw new AuthenticationException(TOKEN_EXPIRED);
        } catch (Exception e) {
            // 令牌错误
            throw new AuthenticationException(TOKEN_ERROR);
        }
        if (null == jwtAccount) {
            throw new AuthenticationException(TOKEN_ERROR);
        }
        return true;
    }
}
