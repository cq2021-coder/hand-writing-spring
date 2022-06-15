package com.cq.service;

import com.cq.spring.CqApplicationContext;

/**
 * 测试
 *
 * @author 程崎
 * @version 1.0.0
 * @since 2022/06/15
 */
public class Test {
    public static void main(String[] args) {
        CqApplicationContext context = new CqApplicationContext(AppConfig.class);
        UserInterface userService = (UserInterface) context.getBean("userService");
        userService.test();
    }
}
