package com.geekerstar.gateway.controller;

import com.geekerstar.gateway.common.Response;
import com.geekerstar.gateway.entity.LoginRequest;
import com.geekerstar.gateway.service.AccountService;
import com.geekerstar.gateway.vo.AccountInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author geekerstar
 * @date 2020/4/13 14:15
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/account")
@Api(tags = "账户相关接口")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "登录，测试用例\n{\n" +
            "      \"appId\": \"geekerstar\",\n" +
            "      \"password\": \"111111\",\n" +
            "      \"methodName\": \"login\",\n" +
            "      \"timestamp\": \"1587277981\"\n" +
            "}\n注意事项：\n1、请使用当前时间戳，后台用于设置颁发Token的过期时间。在线获取时间戳：https://tool.lu/timestamp/\n2、appId是用户登录凭证，可以是民警编号、民警姓名、邮箱、身份证号等，目前暂时约定为民警编号(mjbh)。\n3、登录后会返回Token，请前端将Token和AppId放到Header中，后端将进行权限校验操作。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginRequest", value = "用户登录请求对象", paramType = "body", dataType = "LoginRequest", required = true, defaultValue = "")
    })
    public Response<AccountInfo> login(@Validated @RequestBody LoginRequest loginRequest) {
        AccountInfo result;
        try {
            result = accountService.login(loginRequest);
        } catch (Exception e) {
            return Response.failed();
        }
        return Response.success(result);
    }

}
