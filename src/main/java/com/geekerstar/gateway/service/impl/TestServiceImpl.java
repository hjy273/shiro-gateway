package com.geekerstar.gateway.service.impl;

import com.geekerstar.gateway.service.TestService;
import org.springframework.stereotype.Service;

/**
 * @author geekerstar
 * @date 2020/4/19 13:57
 * @description
 */
@Service
public class TestServiceImpl implements TestService {
    @Override
    public String test1(String test) {
        return "正在访问测试菜单1";
    }

    @Override
    public String test2(String test) {
        return "正在访问测试菜单2";
    }

    @Override
    public String test3(String test) {
        return "正在访问测试菜单3";
    }
}
