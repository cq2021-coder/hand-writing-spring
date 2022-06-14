package com.cq.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 程崎Scope
 *
 * @author 程崎
 * @version 1.0.0
 * @since 2022/06/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CqScope {
    /**
     * 判断bean是单例还是多例
     *
     * @return {@link String}
     */
    String value() default "";
}
