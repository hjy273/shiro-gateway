package com.geekerstar.gateway.exception;

/**
 * @author geekerstar
 * @date 2020/4/15 11:25
 * @description 权限相关异常
 */
public class AuthException {

    /**
     * 系统异常相关
     */
    public static final BusinessException SYS_ERROR = new BusinessException("A00001", "统一认证授权中心异常");
    public static final BusinessException ROUTER_ERROR = new BusinessException("A00002", "路由转发失败，请求路径有误");

    /**
     * 业务异常相关
     */
    public static final BusinessException ERROR_REQUEST = new BusinessException("A10001", "无效的请求");
    public static final BusinessException ERROR_APPID_OR_PASSWORD = new BusinessException("A10002", "登录失败，用户名或密码错误");
    public static final BusinessException EXPIRED_TOKEN = new BusinessException("A10003", "Toke已过期，不在缓冲时间内，请重新登录");
    public static final BusinessException EXPIRED_RESIGN_TOKEN = new BusinessException("A10004", "Token过期，在缓冲时间内，颁发新的Token");
    public static final BusinessException INVALID_TOKEN = new BusinessException("A10005", "Token无效，请重新登录");
    public static final BusinessException NO_AUTH_TOKEN = new BusinessException("A10006", "无访问权限，请申请相应功能使用权限");
    public static final BusinessException NO_TOKEN = new BusinessException("A10007", "非法请求，未携带Token");


}
