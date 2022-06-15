package com.cq.spring;

/**
 * 程崎bean后处理程序
 *
 * @author 程崎
 * @version 1.0.0
 * @since 2022/06/15
 */
public interface CqBeanPostProcessor {

    /**
     * 在初始化过程
     *
     * @param beanName bean名字
     * @param bean     bean
     * @return {@link Object}
     */
    Object postProcessBeforeInitialization(String beanName, Object bean);


    /**
     * 发布过程初始化后
     *
     * @param beanName bean名字
     * @param bean     bean
     * @return {@link Object}
     */
    Object postProcessAfterInitialization(String beanName, Object bean);
}
