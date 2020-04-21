package com.geekerstar.gateway.filter;

import com.geekerstar.gateway.entity.RolePermRule;
import com.geekerstar.gateway.resolver.RestfulUrlMatchingFilterChainResolver;
import com.geekerstar.gateway.service.AccountService;
import com.geekerstar.gateway.service.ResourceService;
import com.geekerstar.gateway.util.SpringContextHolder;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.geekerstar.gateway.constant.CommonConstant.*;

/**
 * @author geekerstar
 * @date 2020/4/14 11:00
 * @description
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ShiroFilterChainManager {

    private final ResourceService resourceService;
    private final StringRedisTemplate stringRedisTemplate;
    private final AccountService accountService;

    /**
     * 初始化获取过滤链
     *
     * @return
     */
    public Map<String, Filter> initGetFilters() {
        LinkedHashMap<String, Filter> filters = Maps.newLinkedHashMap();
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setStringRedisTemplate(stringRedisTemplate);
        filters.put(AUTH, authenticationFilter);
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        authorizationFilter.setStringRedisTemplate(stringRedisTemplate);
        authorizationFilter.setAccountService(accountService);
        filters.put(JWT, authorizationFilter);
        return filters;
    }

    /**
     * 初始化获取过滤链规则
     *
     * @return
     */
    public Map<String, String> initGetFilterChain() throws Exception {
        Map<String, String> filterChain = Maps.newLinkedHashMap();
        // 设置忽略的过滤链规则
        List<String> defaultAnon = Arrays.asList("/css/**", "/js/**", "/webjars/**", "/doc.html");
        defaultAnon.forEach(ignored -> filterChain.put(ignored, ANON));
        // 设置需要走授权的过滤链规则
        List<String> defalutAuth = Arrays.asList("/account/**");
        defalutAuth.forEach(auth -> filterChain.put(auth, AUTH));
        // 设置Restful风格URL动态授权
        if (resourceService != null) {
            List<RolePermRule> rolePermRules = this.resourceService.getRolePermRules();
            if (null != rolePermRules) {
                rolePermRules.forEach(rule -> {
                    StringBuilder chain = rule.toFilterChain();
                    if (null != chain) {
                        filterChain.putIfAbsent(rule.getUrl(), chain.toString());
                    }
                });
            } else {
                log.info("加载授权信息失败");
            }
        }
        return filterChain;
    }

    /**
     * 动态重新加载过滤链规则，权限业务程序里调用
     */
    public void reloadFilterChain() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = SpringContextHolder.getBean(ShiroFilterFactoryBean.class);
        AbstractShiroFilter abstractShiroFilter;
        try {
            abstractShiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            assert abstractShiroFilter != null;
            RestfulUrlMatchingFilterChainResolver filterChainResolver = (RestfulUrlMatchingFilterChainResolver) abstractShiroFilter.getFilterChainResolver();
            DefaultFilterChainManager filterChainManager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
            filterChainManager.getFilterChains().clear();
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(this.initGetFilterChain());
            shiroFilterFactoryBean.getFilterChainDefinitionMap().forEach(filterChainManager::createChain);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
