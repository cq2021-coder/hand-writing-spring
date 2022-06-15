package com.cq.service;

import com.cq.spring.*;

/**
 * 用户服务
 *
 * @author 程崎
 * @version 1.0.0
 * @since 2022/06/15
 */
@CqComponent("userService")
@CqScope("singleton")
public class UserService implements CqBeanNameAware, CqInitializingBean,UserInterface {

    @CqAutowired
    private OrderService orderService;

    private String beanName;

    @Override
    public void test() {
        System.out.println(orderService);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("初始化方法...");
    }
}
