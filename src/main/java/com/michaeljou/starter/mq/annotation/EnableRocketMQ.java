package com.michaeljou.starter.mq.annotation;

import java.lang.annotation.*;

/**
 * Created by michaeljou on 2017/8/22.
 * 启用状态
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EnableRocketMQ {
}
