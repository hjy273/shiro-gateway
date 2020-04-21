package com.geekerstar.gateway.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * @author geekerstar
 * @date 2020/4/14 13:30
 * @description 无状态的Token不创建Session
 */
public class CustomWebSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        context.setSessionCreationEnabled(Boolean.FALSE);
        return super.createSubject(context);
    }

    public CustomWebSubjectFactory() {
    }
}
