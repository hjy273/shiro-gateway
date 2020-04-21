package com.geekerstar.gateway.resolver;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Iterator;

import static com.geekerstar.gateway.constant.CommonConstant.*;

/**
 * @author geekerstar
 * @date 2020/4/14 13:17
 * @description 支持Restful风格URL的过滤链匹配解析器
 */
@Slf4j
public class RestfulUrlMatchingFilterChainResolver extends PathMatchingFilterChainResolver {

    public RestfulUrlMatchingFilterChainResolver() {
        super();
    }

    public RestfulUrlMatchingFilterChainResolver(FilterConfig filterConfig) {
        super(filterConfig);
    }

    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = this.getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        } else {
            String requestUrl = this.getPathWithinApplication(request);
            // 预处理请求路径，防止请求路径末尾携带"/"导致过滤链失效
            if (requestUrl != null && requestUrl.endsWith(DEFAULT_PATH_SEPARATOR)) {
                requestUrl = requestUrl.substring(0, requestUrl.length() - 1);
            }
            Iterator chainUrl = filterChainManager.getChainNames().iterator();
            String pathPattern;
            boolean flag;
            String[] strings;
            do {
                if (!chainUrl.hasNext()) {
                    return null;
                }
                pathPattern = (String) chainUrl.next();
                strings = pathPattern.split(SPLIT);
                if (strings.length == URL_SPLIT_PART) {
                    // 判断请求httpMethod和请求的Method是否一致，不一致则非法
                    flag = !WebUtils.toHttp(request).getMethod().toUpperCase().equals(strings[1].toUpperCase());
                } else {
                    flag = false;
                }
                pathPattern = strings[0];
                if (pathPattern != null && pathPattern.endsWith(DEFAULT_PATH_SEPARATOR)) {
                    pathPattern = pathPattern.substring(0, pathPattern.length() - 1);
                }
            } while (!this.pathMatches(pathPattern, requestUrl) || flag);
            if (log.isTraceEnabled()) {
                log.trace("进行过滤链匹配，请求路径 [" + requestUrl + "]， 路径规则 [" + pathPattern + "].");
            }
            if (strings.length == URL_SPLIT_PART) {
                pathPattern = pathPattern.concat(SPLIT).concat(WebUtils.toHttp(request).getMethod().toUpperCase());
            }
            return filterChainManager.proxy(originalChain, pathPattern);
        }
    }

}
