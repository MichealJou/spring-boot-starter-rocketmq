package com.michaeljou.starter.mq.factory.bean;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.michaeljou.starter.mq.annotation.RocketMQConsumer;
import com.michaeljou.starter.mq.base.AbstractRocketMQPushConsumer;
import com.michaeljou.starter.mq.config.MQProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lenovo on 2017/8/23.
 */
@Slf4j
public class ConsumerBeanFactory {


    private ApplicationContext applicationContext;


    private MQProperties mqProperties;


    /**
     * 构建消费者
     */
    public void buildConsumer(ApplicationContext applicationContext, MQProperties mqProperties) throws Exception {

        if (this.mqProperties == null) {
            this.mqProperties = mqProperties;
        }
        if (this.applicationContext == null) {
            this.applicationContext = applicationContext;
        }
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RocketMQConsumer.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            publishConsumer(entry.getKey(), entry.getValue());
        }
    }

    private void publishConsumer(String beanName, Object bean) throws Exception {
        RocketMQConsumer mqConsumer = applicationContext.findAnnotationOnBean(beanName, RocketMQConsumer.class);
        if (StringUtils.isEmpty(mqConsumer.consumerGroup())) {
            throw new RuntimeException("consumer's consumerGroup must be defined");
        }
        if (StringUtils.isEmpty(mqConsumer.topic())) {
            throw new RuntimeException("consumer's topic must be defined");
        }
        String consumerGroup = applicationContext.getEnvironment().getProperty(mqConsumer.consumerGroup());
        if (StringUtils.isEmpty(consumerGroup)) {
            consumerGroup = mqConsumer.consumerGroup();
        }
        String topic = applicationContext.getEnvironment().getProperty(mqConsumer.topic());
        if (StringUtils.isEmpty(topic)) {
            topic = mqConsumer.topic();
        }
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);

        buildMQConsumer(consumer, mqConsumer);
        consumer.registerMessageListener((List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) -> {
            if (!AbstractRocketMQPushConsumer.class.isAssignableFrom(bean.getClass())) {
                throw new RuntimeException(bean.getClass().getName() + " - consumer未实现IMQPushConsumer接口");
            }
            AbstractRocketMQPushConsumer abstractMQPushConsumer = (AbstractRocketMQPushConsumer) bean;
            return abstractMQPushConsumer.dealMessage(list, consumeConcurrentlyContext);
        });
        consumer.start();
        log.info(String.format("%s is ready to subscribe message", bean.getClass().getName()));
    }

    private void buildMQConsumer(DefaultMQPushConsumer consumer, RocketMQConsumer mqConsumer) throws MQClientException {
        consumer.setNamesrvAddr(mqProperties.getNameServerAddress());
        consumer.setMessageModel(MessageModel.valueOf(mqConsumer.messageMode()));
        consumer.setVipChannelEnabled(mqProperties.isVipChannelEnabled());
        consumer.subscribe(mqConsumer.topic(), StringUtils.join(mqConsumer.tag(), "||"));
        consumer.setInstanceName(UUID.randomUUID().toString());
        consumer.setConsumeThreadMin(mqProperties.getConsumeThreadMin());
        consumer.setConsumeThreadMax(mqProperties.getConsumeThreadMax());
        consumer.setAdjustThreadPoolNumsThreshold(mqProperties.getAdjustThreadPoolNumsThreshold());
        consumer.setConsumeConcurrentlyMaxSpan(mqProperties.getConsumeConcurrentlyMaxSpan());
        consumer.setPullThresholdForQueue(mqProperties.getPullThresholdForQueue());
        consumer.setPullInterval(mqProperties.getPullInterval());
        consumer.setConsumeMessageBatchMaxSize(mqProperties.getConsumeMessageBatchMaxSize());
        consumer.setPullBatchSize(mqProperties.getPullBatchSize());
        consumer.setPostSubscriptionWhenPull(mqProperties.isPostSubscriptionWhenPull());
        consumer.setMaxReconsumeTimes(mqProperties.getMaxReconsumeTimes());
        consumer.setSuspendCurrentQueueTimeMillis(mqProperties.getSuspendCurrentQueueTimeMillis());
        consumer.setConsumeTimeout(mqProperties.getConsumeTimeout());

    }

}
