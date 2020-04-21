package com.geekerstar.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.geekerstar.gateway.common.Response;
import com.geekerstar.gateway.entity.PasswordToken;
import com.geekerstar.gateway.util.IpUtil;
import com.geekerstar.gateway.util.RequestResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.geekerstar.gateway.constant.CommonConstant.*;
import static com.geekerstar.gateway.exception.AuthException.ERROR_APPID_OR_PASSWORD;
import static com.geekerstar.gateway.exception.AuthException.ERROR_REQUEST;

/**
 * @author geekerstar
 * @date 2020/4/14 11:06
 * @description 认证过滤器，为了提升用户体验，如果认证失败，返回相关信息而不抛出异常
 */
@Slf4j
public class AuthenticationFilter extends AccessControlFilter {

    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        // 如果已经登录再次发送此请求，交给onAccessDenied处理
        return null != subject && subject.isAuthenticated();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        if (isPasswordLoginPost(request)) {
            AuthenticationToken authenticationToken;
            try {
                authenticationToken = createPasswordToken(request);
            } catch (Exception e) {
                log.error("");
                RequestResponseUtil.responseWrite(JSON.toJSONString(Response.failed(ERROR_REQUEST)), response);
                return false;
            }
            Subject subject = getSubject(request, response);
            try {
                subject.login(authenticationToken);
                return true;
            } catch (AuthenticationException e) {
                log.warn(authenticationToken.getPrincipal() + "::" + e.getMessage());
                RequestResponseUtil.responseWrite(JSON.toJSONString(Response.failed(ERROR_APPID_OR_PASSWORD)), response);
                return false;
            } catch (Exception e) {
                log.error(authenticationToken.getPrincipal() + "::认证异常::" + e.getMessage(), e);
                RequestResponseUtil.responseWrite(JSON.toJSONString(Response.failed(ERROR_APPID_OR_PASSWORD)), response);
                return false;
            }
        }
        if (isAccountRegisterPost(request)) {
            return true;
        }
        RequestResponseUtil.responseWrite(JSON.toJSONString(Response.failed(ERROR_REQUEST)), response);
        return false;
    }


    /**
     * 是否为登录请求
     *
     * @param request
     * @return
     */
    private boolean isPasswordLoginPost(ServletRequest request) {
        Map<String, String> map = RequestResponseUtil.getRequestBodyMap(request);
        String appId = map.get(APPID);
        String password = map.get(PASSWORD);
        String methodName = map.get(METHOD_NAME);
        String timestamp = map.get(TIMESTAMP);
        return (request instanceof HttpServletRequest)
                && POST.equals(((HttpServletRequest) request).getMethod().toUpperCase())
                && null != password
                && null != timestamp
                && null != appId
                && LOGIN.equals(methodName);
    }

    /**
     * 是否为注册请求
     *
     * @param request
     * @return
     */
    private boolean isAccountRegisterPost(ServletRequest request) {
        Map<String, String> map = RequestResponseUtil.getRequestBodyMap(request);
        // todo 注册参数待设计
        String appId = map.get(APPID);
        String password = map.get(PASSWORD);
        String methodName = map.get(METHOD_NAME);
        return (request instanceof HttpServletRequest)
                && POST.equals(((HttpServletRequest) request).getMethod().toUpperCase())
                && null != appId
                && null != password
                && REGISTER.equals(methodName);
    }


    private AuthenticationToken createPasswordToken(ServletRequest request) throws Exception {
        Map<String, String> map = RequestResponseUtil.getRequestBodyMap(request);
        String appId = map.get(APPID);
        String password = map.get(PASSWORD);
        String timestamp = map.get(TIMESTAMP);
        String host = IpUtil.getIpFromRequest(WebUtils.toHttp(request));
        return new PasswordToken(appId, password, timestamp, host);
    }

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

}
