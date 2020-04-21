package com.geekerstar.gateway.config;

import com.geekerstar.gateway.filter.CustomWebSubjectFactory;
import com.geekerstar.gateway.filter.RestShiroFilterFactoryBean;
import com.geekerstar.gateway.filter.ShiroFilterChainManager;
import com.geekerstar.gateway.realm.CustomModularRealmAuthenticator;
import com.geekerstar.gateway.realm.RealmManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author geekerstar
 * @date 2020/4/14 09:01
 * @description shiro配置类
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, ShiroFilterChainManager filterChainManager) throws Exception {
        RestShiroFilterFactoryBean shiroFilterFactoryBean = new RestShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilters(filterChainManager.initGetFilters());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainManager.initGetFilterChain());
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(RealmManager realmManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(new CustomModularRealmAuthenticator());
        securityManager.setRealms(realmManager.initGetRealm());
        DefaultSessionStorageEvaluator evaluator = (DefaultSessionStorageEvaluator) ((DefaultSubjectDAO) securityManager.getSubjectDAO()).getSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(Boolean.FALSE);
        CustomWebSubjectFactory subjectFactory = new CustomWebSubjectFactory();
        securityManager.setSubjectFactory(subjectFactory);
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }
}
