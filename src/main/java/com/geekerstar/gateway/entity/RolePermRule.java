package com.geekerstar.gateway.entity;

import com.geekerstar.gateway.util.JsonWebTokenUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Set;

import static com.geekerstar.gateway.constant.CommonConstant.*;

/**
 * @author geekerstar
 * @date 2020/4/14 09:57
 * @description
 */
@Getter
@Setter
public class RolePermRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源URL
     */
    private String url;
    /**
     * 访问资源所需要的角色列表，多个列表用逗号间隔
     */
    private String needroles;

    /**
     * 将url needRoles 转化成shiro可识别的过滤器链：url=jwt[角色1、角色2、角色n]
     *
     * @return
     */
    public StringBuilder toFilterChain() {

        if (null == this.url || this.url.isEmpty()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Set<String> setRole = JsonWebTokenUtil.split(this.getNeedroles());
        if (!StringUtils.isEmpty(this.getNeedroles()) && setRole.contains(ROLE_ANON)) {
            stringBuilder.append(ANON);
        }
        if (!StringUtils.isEmpty(this.getNeedroles()) && !setRole.contains(ROLE_ANON)) {
            stringBuilder.append(JWT + "[").append(this.getNeedroles()).append("]");
        }
        return stringBuilder.length() > 0 ? stringBuilder : null;
    }

    @Override
    public String toString() {
        return "RolePermRule [url=" + url + ", needRoles=" + needroles + "]";
    }
}

