## 基于Shiro的轻量级认证授权网关

### 登录说明

#### 请求示例

```json
{
      "appId": "180180",
      "password": "180180",
      "methodName": "login",
      "timestamp": "1587018743"
}
```

#### 响应示例

```json
{
  "success": true,
  "code": "200",
  "message": "OK",
  "results": {
    "mjbh": "180180",
    "mjmc": "权限测试",
    "bmbm": "440600060000",
    "bmmc": "佛山市公安局警卫处",
    "jh": "180180",
    "zw": "副支队长",
    "sjlybm": 1,
    "bmqxbm": "1",
    "roleCodeList": "role_user",
    "token": "eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNosi20KwjAQRO-yvxvYfNTd9jKSlA1UxUqSFkG8u5siDAzzmPeBW1thBsyORx_JWG_RhCkFEyUtJuOU6RKIxQsMUPekZ8uo0bnWqrNtd3maKuWQ0mFsMNuRCR2SwwHk_foDzyco20O62Pu6qwjfHwAAAP__.A3ZsDulxsmAlTzgtjp43lZjgRjTOLJ2Cj0kGS2YbxuZt8l6pqmMB2jpZ7pQWbGVPl_14H1FI8wj8_IUGqxY_ew",
    "dlmm": null
  }
}
```

> 注意事项：
>
> 1. 请使用当前时间戳，后台用于设置颁发Token的过期时间。
> 2. appId是用户登录凭证，可以是民警编号、民警姓名、邮箱、身份证号等，目前暂时约定为民警编号(mjbh)。
> 3. 登录后会返回Token，请前端将Token和AppId放到Header中，后端将进行权限校验操作。

### 异常码值说明对照表

| 码值   | 说明                                     |
| ------ | ---------------------------------------- |
| A00001 | 统一认证授权中心异常                     |
| A00002 | 路由转发失败，请求路径有误               |
| A10001 | 无效的请求                               |
| A10002 | 登录失败，用户名或密码错误               |
| A10003 | Toke已过期，不在缓冲时间内，请重新登录   |
| A10004 | Token已过期，在缓冲时间内，颁发新的Token |
| A10005 | Token无效，请重新登录                    |
| A10006 | 无访问权限，请申请相应功能使用权限       |
| A10007 | 非法请求，未携带Token                    |

