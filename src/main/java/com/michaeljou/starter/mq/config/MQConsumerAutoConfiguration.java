package com.michaeljou.starter.mq.config;


import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.michaeljou.starter.mq.annotation.RocketMQConsumer;
import com.michaeljou.starter.mq.base.AbstractRocketMQPushConsumer;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;


import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by suclogger on 2017/6/28.
 * 自动装配消息消费者
 */
@Slf4j
@Configuration
@ConditionalOnBean(MQBaseAutoConfiguration.class)
public class MQConsumerAutoConfiguration extends MQBaseAutoConfiguration {
    @PostConstruct
    public void init() throws Exception {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RocketMQConsumer.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            publishConsumer(entry.getKey(), entry.getValue());
        }
    }

    private void publishConsumer(String beanName, Object bean) throws Exception {
        RocketMQConsumer mqConsumer = applicationContext.findAnnotationOnBean(beanName, RocketMQConsumer.class);
        if(StringUtils.isEmpty(mqConsumer.consumerGroup())) {
            throw new RuntimeException("consumer's consumerGroup must be defined");
        }
        if(StringUtils.isEmpty(mqConsumer.topic())) {
            throw new RuntimeException("consumer's topic must be defined");
        }
        String consumerGroup = applicationContext.getEnvironment().getProperty(mqConsumer.consumerGroup());
        if(StringUtils.isEmpty(consumerGroup)) {
            consumerGroup = mqConsumer.consumerGroup();
        }
        String topic = applicationContext.getEnvironment().getProperty(mqConsumer.topic());
        if(StringUtils.isEmpty(topic)) {
            topic = mqConsumer.topic();
        }
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(mqProperties.getNameServerAddress());
        consumer.setMessageModel(MessageModel.valueOf(mqConsumer.messageMode()));
        consumer.subscribe(topic, StringUtils.join(mqConsumer.tag(),"||"));
        consumer.setInstanceName(UUID.randomUUID().toString());
        consumer.registerMessageListener((List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) -> {
            if(!AbstractRocketMQPushConsumer.class.isAssignableFrom(bean.getClass())) {
                throw new RuntimeException(bean.getClass().getName() + " - consumer未实现IMQPushConsumer接口");
            }
            AbstractRocketMQPushConsumer abstractMQPushConsumer = (AbstractRocketMQPushConsumer) bean;
            return abstractMQPushConsumer.dealMessage(list, consumeConcurrentlyContext);
        });
        consumer.start();
        log.info(String.format("%s is ready to subscribe message", bean.getClass().getName()));
    }
}