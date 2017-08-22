package com.michaeljou.starter.mq.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by lenovo on 2017/8/22.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RocketMQConsumer {

    String consumerGroup();

    String topic();

    String messageMode() default "CLUSTERING";

    String[] tag() default {"*"};
}
