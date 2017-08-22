package com.michaeljou.starter.mq.config;


import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MQProducer;
import com.michaeljou.starter.mq.annotation.RcoketMQProducer;
import com.michaeljou.starter.mq.base.AbstractRocketMQProducer;
import com.michaeljou.starter.mq.base.AbstractRocketMQPushConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by yipin on 2017/6/29.
 * 自动装配消息生产者
 */
@Slf4j
@Configuration
@ConditionalOnBean(MQBaseAutoConfiguration.class)
public class MQProducerAutoConfiguration extends MQBaseAutoConfiguration {

    private DefaultMQProducer producer;

    @PostConstruct
    public void init() throws Exception {
        if (producer == null) {
//            if(StringUtils.isEmpty(mqProperties.getProducerGroup())) {
//                throw new RuntimeException("请在配置文件中指定消息发送方group！");
//            }
            producer = new DefaultMQProducer(mqProperties.getProducerGroup());
            producer.setNamesrvAddr(mqProperties.getNameServerAddress());
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
}