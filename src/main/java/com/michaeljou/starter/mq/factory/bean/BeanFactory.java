package com.michaeljou.starter.mq.factory.bean;

import com.michaeljou.starter.mq.config.MQBaseAutoConfiguration;
import com.michaeljou.starter.mq.config.MQProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by lenovo on 2017/8/23.
 */
@Slf4j
public class BeanFactory extends AbstractRocketMQBeanFactory {


    private ConsumerBeanFactory consumerBeanFactory;


    private ProducerBeanFactory producerBeanFactory;


    private MQProperties mqProperties;

    public BeanFactory() {

    }

    public BeanFactory(ApplicationContext applicationContext, MQProperties mqProperties) {
        if (this.applicationContext == null) {
            this.applicationContext = applicationContext;
        }

        if (this.mqProperties == null) {
            this.mqProperties = mqProperties;
        }
        if (this.consumerBeanFactory == null) {
            consumerBeanFactory = new ConsumerBeanFactory();
        }

        if (this.producerBeanFactory == null) {
            producerBeanFactory = new ProducerBeanFactory();
        }

    }

    @Override
    public void build() throws Exception {
        producerBeanFactory.buildProducer(this.applicationContext, this.mqProperties);
        consumerBeanFactory.buildConsumer(this.applicationContext, this.mqProperties);
        ;
    }


}
