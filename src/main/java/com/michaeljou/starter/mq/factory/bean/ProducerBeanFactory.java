package com.michaeljou.starter.mq.factory.bean;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.common.MixAll;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.alibaba.rocketmq.remoting.common.RemotingUtil;
import com.michaeljou.starter.mq.annotation.RcoketMQProducer;
import com.michaeljou.starter.mq.annotation.RocketMQConsumer;
import com.michaeljou.starter.mq.base.AbstractRocketMQProducer;
import com.michaeljou.starter.mq.config.MQProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Created by lenovo on 2017/8/23.
 */
@Slf4j
@Component
@ConditionalOnMissingBean
public class ProducerBeanFactory {

    private DefaultMQProducer producer;

    private ApplicationContext applicationContext;

    @Autowired
    private MQProperties mqProperties;

    /**
     * 构建消费者
     */
    public void buildProducer(ApplicationContext applicationContext, MQProperties mqProperties) throws Exception {
        if (producer == null) {
            if (StringUtils.isEmpty(mqProperties.getProducerGroup())) {
                throw new RuntimeException("请在配置文件中指定消息发送方group！");
            }
            producer = new DefaultMQProducer(mqProperties.getProducerGroup());
            buildMQProducer(producer);
            producer.start();
        }
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RcoketMQProducer.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            publishProducer(entry.getKey(), entry.getValue());
        }
    }

    private void publishProducer(String beanName, Object bean) throws Exception {
        if (!AbstractRocketMQProducer.class.isAssignableFrom(bean.getClass())) {
            throw new RuntimeException(beanName + " - producer未继承AbstractMQProducer");
        }
        AbstractRocketMQProducer abstractMQProducer = (AbstractRocketMQProducer) bean;
        abstractMQProducer.setProducer(producer);
        RcoketMQProducer mqProducer = applicationContext.findAnnotationOnBean(beanName, RcoketMQProducer.class);
        String topic = mqProducer.topic();
        if (!StringUtils.isEmpty(topic)) {
            String transTopic = applicationContext.getEnvironment().getProperty(topic);
            if (StringUtils.isEmpty(transTopic)) {
                abstractMQProducer.setTopic(topic);
            } else {
                abstractMQProducer.setTopic(transTopic);
            }
        }
        String tag = mqProducer.tag();
        if (!StringUtils.isEmpty(tag)) {
            String transTag = applicationContext.getEnvironment().getProperty(tag);
            if (StringUtils.isEmpty(transTag)) {
                abstractMQProducer.setTag(tag);
            } else {
                abstractMQProducer.setTag(transTag);
            }
        }
        log.info(String.format("%s is ready to produce message", beanName));
    }

    public void buildMQProducer(DefaultMQProducer producer) throws MQClientException {
        producer.setNamesrvAddr(mqProperties.getNameServerAddress());
        producer.setVipChannelEnabled(mqProperties.isVipChannelEnabled());
        producer.setCreateTopicKey(mqProperties.getCreateTopicKey());
        producer.setDefaultTopicQueueNums(mqProperties.getDefaultTopicQueueNums());
        producer.setSendMsgTimeout(mqProperties.getSendMsgTimeout());
        producer.setCompressMsgBodyOverHowmuch(mqProperties.getCompressMsgBodyOverHowmuch());
        producer.setRetryTimesWhenSendFailed(mqProperties.getRetryTimesWhenSendFailed());
        producer.setRetryTimesWhenSendAsyncFailed(mqProperties.getRetryTimesWhenSendAsyncFailed());
        producer.setRetryAnotherBrokerWhenNotStoreOK(mqProperties.isRetryAnotherBrokerWhenNotStoreOK());
        producer.setMaxMessageSize(mqProperties.getMaxMessageSize());
        producer.setInstanceName(mqProperties.getInstanceName());
        producer.setClientIP(mqProperties.getClientIP());
        producer.setPollNameServerInteval(mqProperties.getPollNameServerInteval());
        producer.setHeartbeatBrokerInterval(mqProperties.getHeartbeatBrokerInterval());
        producer.setPersistConsumerOffsetInterval(mqProperties.getPersistConsumerOffsetInterval());
        ;

    }
}
