package com.michaeljou.starter.mq.config;


import com.michaeljou.starter.mq.annotation.EnableRocketMQConfiguration;
import com.michaeljou.starter.mq.base.AbstractRocketMQProducer;
import com.michaeljou.starter.mq.base.AbstractRocketMQPushConsumer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yipin on 2017/6/28.
 * RocketMQ配置文件
 */
@Configuration
@ConditionalOnBean(annotation = EnableRocketMQConfiguration.class)
@AutoConfigureAfter({AbstractRocketMQProducer.class, AbstractRocketMQPushConsumer.class})
@EnableConfigurationProperties(MQProperties.class)
public class MQBaseAutoConfiguration implements ApplicationContextAware {
    @Autowired
    protected MQProperties mqProperties;
    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
