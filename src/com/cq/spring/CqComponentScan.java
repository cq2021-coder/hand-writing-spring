package com.cq.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 程崎组件扫描
 *
 * @author 程崎
 * @version 1.0.0
 * @since 2022/06/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CqComponentScan {
    String value() default "";
}
