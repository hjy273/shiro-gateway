package com.geekerstar.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.geekerstar.gateway.common.Response;
import com.geekerstar.gateway.entity.JwtToken;
import com.geekerstar.gateway.service.AccountService;
import com.geekerstar.gateway.util.IpUtil;
import com.geekerstar.gateway.util.JsonWebTokenUtil;
import com.geekerstar.gateway.util.RequestResponseUtil;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.geekerstar.gateway.constant.CommonConstant.*;
import static com.geekerstar.gateway.exception.AuthException.*;

/**
 * @author geekerstar
 * @date 2020/4/14 11:47
 * @description 授权过滤器
 * <p>
 * 如果Token过期，则在Redis中查询当前appID对应的Token，其过期时间为Token的两倍作为缓冲时间bufferTime
 * 当Token有效期过后，查询其bufferTime，时间有效则重新颁发Token给客户端
 */
@Slf4j
public class AuthorizationFilter extends BaseUrlMatchingFilter {

    private StringRedisTemplate stringRedisTemplate;

    private AccountService accountService;

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        // TODO 记录调用api日志到数据库
        boolean requestAuthorization = (null != subject && !subject.isAuthenticated()) && isSubmission(request);
        if (requestAuthorization) {
            AuthenticationToken token = createToken(request);
            try {
                subject.login(token);
                return this.verifyRoles(subject, mappedValue);
            } catch (AuthenticationException e) {
                // TODO Token过期后续签处理，后续再优化，目前是设置一个缓冲时间为Token时间的两倍，如果该时间有效则颁发新的Token给客户端然后重新请求
                if (TOKEN_EXPIRED.equals(e.getMessage())) {
                    // refresh也过期则告知客户端JWT时间过期重新认证
                    String appId = WebUtils.toHttp(request).getHeader(APPID);
                    String jwt = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
                    String refreshJwt = stringRedisTemplate.opsForValue().get(TOKEN_PREFIX + appId);
                    if (null != refreshJwt && refreshJwt.equals(jwt)) {
                        String roles = accountService.getAccountRole(appId);
                        long refreshPeriodTime = 36000L;
                        String newJwt = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(), appId,
                                TOKEN_ISSUER, refreshPeriodTime >> 1, roles, null, SignatureAlgorithm.HS512);
                        stringRedisTemplate.opsForValue().set(TOKEN_PREFIX + appId, newJwt, refreshPeriodTime, TimeUnit.SECONDS);
                        RequestResponseUtil.responseWrite(JSON.toJSONString(Response.failed(EXPIRED_RESIGN_TOKEN), SerializerFeature.valueOf(newJwt)), response);
                        return false;
                    } else {
                        RequestResponseUtil.responseWrite(JSON.toJSONString(Response.failed(EXPIRED_TOKEN)), response);
                        return false;
                    }
                }
                RequestResponseUtil.responseWrite(JSON.toJSONString(Response.failed(INVALID_TOKEN)), response);
                return false;
            } catch (Exception e) {
                log.error(IpUtil.getIpFromRequest(WebUtils.toHttp(request)) + "::Token认证失败::" + e.getMessage(), e);
                RequestResponseUtil.responseWrite(JSON.toJSONString(Response.failed(INVALID_TOKEN)), response);
                return false;
            }
        } else {
            RequestResponseUtil.responseWrite(JSON.toJSONString(Response.failed(NO_TOKEN)), response);
            return false;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (subject != null && subject.isAuthenticated()) {
            log.debug("请求被拦截，Token无权限");
            RequestResponseUtil.responseWrite(JSON.toJSONString(Response.failed(NO_AUTH_TOKEN)), response);
        }
        return false;
    }

    private boolean isSubmission(ServletRequest request) {
        String jwt = RequestResponseUtil.getHeader(request, AUTHORIZATION);
        String appId = RequestResponseUtil.getHeader(request, APPID);
        return (request instanceof HttpServletRequest)
                && !StringUtils.isEmpty(jwt)
                && !StringUtils.isEmpty(appId);
    }

    private AuthenticationToken createToken(ServletRequest request) {
        Map<String, String> maps = RequestResponseUtil.getRequestHeaders(request);
        String appId = maps.get(APPID);
        String ipHost = request.getRemoteAddr();
        String jwt = maps.get(AUTHORIZATION);
        String deviceInfo = maps.get(DEVICE_INFO);
        return new JwtToken(ipHost, deviceInfo, jwt, appId);
    }

    /**
     * 验证当前用户是否属于mappedValue任意一个角色
     *
     * @param subject
     * @param mappedValue
     * @return
     */
    private boolean verifyRoles(Subject subject, Object mappedValue) {
        String[] rolesArray = (String[]) mappedValue;
        return rolesArray == null || rolesArray.length == 0 || Stream.of(rolesArray).anyMatch(role -> subject.hasRole(role.trim()));
    }
}
