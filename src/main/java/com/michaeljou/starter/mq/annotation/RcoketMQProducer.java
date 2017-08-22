package com.michaeljou.starter.mq.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by michealjou on 2017/8/22.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RcoketMQProducer {
    String topic() default "";

    String tag() default "";
}
