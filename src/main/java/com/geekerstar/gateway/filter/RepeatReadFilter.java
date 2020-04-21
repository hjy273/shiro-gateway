package com.geekerstar.gateway.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author geekerstar
 * @date 2020/4/15 16:24
 * @description 集成拦截器后，会对请求中的流进行一次校验操作，流被读取一次就不存在了，而@RequestBody只能以流的方式读取，因此会导致无法获取到Body中的数据，这里做了一个处理，将HttpServletRequest变成可重复读
 */
@Component
public class RepeatReadFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            request = new RepeatHttpServletRequest((HttpServletRequest) request);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
