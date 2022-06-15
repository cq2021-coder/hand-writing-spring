package com.cq.service;

import com.cq.spring.CqBeanPostProcessor;
import com.cq.spring.CqComponent;

import java.lang.reflect.Proxy;

/**
 * 程崎bean处理器
 *
 * @author 程崎
 * @version 1.0.0
 * @since 2022/06/15
 */
@CqComponent
public class CqBeanPostProcessorCustomer implements CqBeanPostProcessor {

    private static final String USER_SERVICE_BEAN_NAME = "userService";

    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        if (USER_SERVICE_BEAN_NAME.equals(beanName)) {
            System.out.println("执行初始化方法前");
            System.out.println(bean);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {
        if (USER_SERVICE_BEAN_NAME.equals(beanName)) {

            return Proxy.newProxyInstance(CqBeanPostProcessorCustomer.class.getClassLoader(),
                    bean.getClass().getInterfaces(), (proxy, method, args) -> {
                        System.out.println("切面逻辑");
                        return method.invoke(bean, args);
                    });
        }
        return bean;
    }
}
