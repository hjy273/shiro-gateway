package com.geekerstar.gateway.constant;

/**
 * @author geekerstar
 * @date 2020/4/16 09:18
 * @description 公共常量
 */
public interface CommonConstant {

    /**
     * 认证相关
     */
    String APPID = "appId";
    String PASSWORD = "password";
    String METHOD_NAME = "methodName";
    String TIMESTAMP = "timestamp";
    String REGISTER = "register";
    String LOGIN = "login";
    String AUTHORIZATION = "authorization";
    String DEVICE_INFO = "deviceInfo";
    String SALT = "geekerstar";

    /**
     * 授权相关
     */
    String TOKEN_PREFIX = "gateway:JWT-SESSION-";
    String TOKEN_ISSUER = "token-server";
    String TOKEN_EXPIRED = "expiredJwt";
    String TOKEN_ERROR = "errJwt";
    String ANON = "anon";
    String AUTH = "auth";
    String JWT = "jwt";
    String ROLES = "roles";
    String PERMS = "perms";
    String ROLE_ANON = "role_anon";

    /**
     * HTTP请求方法相关
     */
    String GET = "GET";
    String POST = "POST";
    String PUT = "PUT";
    String DELETE = "DELETE";

    /**
     * 路由相关
     */
    String URL_PREFIX = "router";
    String HTTP_PREFIX = "http://";
    String HTTPS_PREFIX = "https://";
    String REQUEST_SPLIT = "?";
    String REQUEST_CONTACT = "&";

    /**
     * 其他
     */
    String DEFAULT_PATH_SEPARATOR = "/";
    String SPLIT = "==";
    String GATEWAY = "gateway";
    String SWAGGER_SUFFIX = "/doc.html";
    int URL_SPLIT_PART = 2;
}
