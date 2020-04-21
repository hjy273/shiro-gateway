package com.geekerstar.gateway.controller;

import com.geekerstar.gateway.common.Response;
import com.geekerstar.gateway.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author geekerstar
 * @date 2020/4/19 13:56
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/router/ht/test")
@Api(tags = "测试接口")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping("/test1")
    @ApiOperation(value = "测试菜单1", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "test", value = "测试", paramType = "query", required = true, defaultValue = "")
    })
    public Response<String> test1(@RequestParam String test) {
        String result;
        try {
            result = testService.test1(test);
        } catch (Exception e) {
            return Response.failed();
        }
        return Response.success(result);
    }

    @PostMapping("/test2")
    @ApiOperation(value = "测试菜单2", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "test", value = "测试", paramType = "query", required = true, defaultValue = "")
    })
    public Response<String> test2(@RequestParam String test) {
        String result;
        try {
            result = testService.test2(test);
        } catch (Exception e) {
            return Response.failed();
        }
        return Response.success(result);
    }

    @PostMapping("/test3")
    @ApiOperation(value = "测试菜单3", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "test", value = "测试", paramType = "query", required = true, defaultValue = "")
    })
    public Response<String> test3(@RequestParam String test) {
        String result;
        try {
            result = testService.test3(test);
        } catch (Exception e) {
            return Response.failed();
        }
        return Response.success(result);
    }

}
