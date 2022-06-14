package com.cq.service;

import com.cq.spring.CqAutowired;
import com.cq.spring.CqBeanNameAware;
import com.cq.spring.CqComponent;
import com.cq.spring.CqScope;

/**
 * 用户服务
 *
 * @author 程崎
 * @version 1.0.0
 * @since 2022/06/15
 */
@CqComponent("userService")
@CqScope("singleton")
public class UserService implements CqBeanNameAware {

    @CqAutowired
    private OrderService orderService;

    private String beanName;

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
}
