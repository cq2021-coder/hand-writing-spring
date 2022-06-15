package com.cq.spring;

/**
 * 程崎初始化Bean
 *
 * @author 程崎
 * @version 1.0.0
 * @since 2022/06/15
 */
public interface CqInitializingBean {
    /**
     * bean的生命周期：实例化->生成对象->属性填充->执行afterPropertiesSet
     * 在属性填充后会执行该方法
     */
    void afterPropertiesSet();
}
