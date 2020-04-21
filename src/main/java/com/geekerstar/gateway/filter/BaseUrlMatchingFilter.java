package com.geekerstar.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static com.geekerstar.gateway.constant.CommonConstant.DEFAULT_PATH_SEPARATOR;
import static com.geekerstar.gateway.constant.CommonConstant.SPLIT;

/**
 * @author geekerstar
 * @date 2020/4/14 11:47
 * @description 重写过滤链路径匹配规则，增加REST风格URL
 */
@Slf4j
public abstract class BaseUrlMatchingFilter extends PathMatchingFilter {

    public BaseUrlMatchingFilter() {

    }

    /**
     * 重写URL匹配  加入httpMethod支持
     *
     * @param path
     * @param request
     * @return
     */
    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestUrl = this.getPathWithinApplication(request);
        if (requestUrl != null && requestUrl.endsWith(DEFAULT_PATH_SEPARATOR)) {
            requestUrl = requestUrl.substring(0, requestUrl.length() - 1);
        }
        String[] strings = path.split(SPLIT);
        if (strings[0] != null && strings[0].endsWith(DEFAULT_PATH_SEPARATOR)) {
            strings[0] = strings[0].substring(0, strings[0].length() - 1);
        }
        if (strings.length <= 1) {
            return this.pathsMatch(strings[0], requestUrl);
        } else {
            String httpMethod = WebUtils.toHttp(request).getMethod().toUpperCase();
            return httpMethod.equals(strings[1].toUpperCase()) && this.pathsMatch(strings[0], requestUrl);
        }
    }

    protected Subject getSubject(ServletRequest request, ServletResponse response) {
        return SecurityUtils.getSubject();
    }

    protected abstract boolean isAccessAllowed(ServletRequest var1, ServletResponse var2, Object var3) throws Exception;

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return this.onAccessDenied(request, response);
    }

    protected abstract boolean onAccessDenied(ServletRequest var1, ServletResponse var2) throws Exception;

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        log.info("URL重写");
        return this.isAccessAllowed(request, response, mappedValue) || this.onAccessDenied(request, response, mappedValue);
    }

    protected void saveRequest(ServletRequest request) {
        WebUtils.saveRequest(request);
    }
}
