package com.cq.spring;

/**
 * 程崎Bean名字通知(回调接口)
 *
 * @author 程崎
 * @version 1.0.0
 * @since 2022/06/15
 */
public interface CqBeanNameAware {
    /**
     * 传递bean名称
     *
     * @param beanName bean名字
     */
    void setBeanName(String beanName);
}
