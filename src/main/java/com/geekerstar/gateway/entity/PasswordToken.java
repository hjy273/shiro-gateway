package com.geekerstar.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author geekerstar
 * @date 2020/4/14 10:45
 * @description
 */
@Getter
@Setter
@AllArgsConstructor
public class PasswordToken implements AuthenticationToken {

    private String appId;
    private String password;
    private String timestamp;
    private String host;

    @Override
    public Object getPrincipal() {
        return this.appId;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }
}
