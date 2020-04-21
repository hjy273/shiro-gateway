package com.geekerstar.gateway.realm;

import com.geekerstar.gateway.entity.JwtToken;
import com.geekerstar.gateway.util.JsonWebTokenUtil;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Map;
import java.util.Set;

import static com.geekerstar.gateway.constant.CommonConstant.PERMS;
import static com.geekerstar.gateway.constant.CommonConstant.ROLES;

/**
 * @author geekerstar
 * @date 2020/4/14 10:53
 * @description
 */
public class AuthorizationRealm extends AuthorizingRealm {

    private static final String JWT = "jwt:";
    private static final int NUM_4 = 4;
    private static final char LEFT = '{';
    private static final char RIGHT = '}';

    /**
     * 此realm只支持jwtToken
     *
     * @return
     */
    @Override
    public Class<?> getAuthenticationTokenClass() {
        return JwtToken.class;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String payload = (String) principalCollection.getPrimaryPrincipal();
        if (payload.startsWith(JWT) && payload.charAt(NUM_4) == LEFT && payload.charAt(payload.length() - 1) == RIGHT) {
            Map<String, Object> payloadMap = JsonWebTokenUtil.readValue(payload.substring(4));
            Set<String> roles = JsonWebTokenUtil.split((String) payloadMap.get(ROLES));
            Set<String> permissions = JsonWebTokenUtil.split((String) payloadMap.get(PERMS));
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            if (!roles.isEmpty()) {
                info.setRoles(roles);
            }
            if (!permissions.isEmpty()) {
                info.setStringPermissions(permissions);
            }
            return info;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (!(authenticationToken instanceof JwtToken)) {
            return null;
        }
        JwtToken jwtToken = (JwtToken) authenticationToken;
        // todo 这里注意
        String jwt = jwtToken.getDeviceInfo();
        String payload;
        try {
            // 预先解析Payload
            // 没有做任何的签名校验
            payload = JsonWebTokenUtil.parseJwtPayload(jwt);
        } catch (MalformedJwtException e) {
            throw new AuthenticationException("Token格式错误");
        } catch (Exception e) {
            throw new AuthenticationException("Token无效");
        }
        if (null == payload) {
            throw new AuthenticationException("Token无效");
        }
        return new SimpleAuthenticationInfo("jwt:" + payload, jwt, this.getName());
    }
}
