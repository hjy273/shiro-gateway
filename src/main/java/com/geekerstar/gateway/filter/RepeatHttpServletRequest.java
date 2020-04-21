package com.geekerstar.gateway.filter;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * @author geekerstar
 * @date 2020/4/15 16:24
 * @description 构造可重复读的HttpServletRequest
 */
public class RepeatHttpServletRequest extends HttpServletRequestWrapper {

    private final byte[] bytes;

    public RepeatHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        bytes = IOUtils.toByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() {
        return new RepeatServletInputStream(this.bytes);
    }
}
