package com.cq.spring;

/**
 * 程崎Bean定义
 *
 * @author 程崎
 * @version 1.0.0
 * @since 2022/06/15
 */
public class CqBeanDefinition {
    private Class<?> type;
    private String scope;

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
