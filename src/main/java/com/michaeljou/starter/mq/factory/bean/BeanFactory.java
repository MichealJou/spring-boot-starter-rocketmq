package com.michaeljou.starter.mq.factory.bean;

import com.michaeljou.starter.mq.config.MQProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by lenovo on 2017/8/23.
 */
@Component
@ConditionalOnMissingBean
public class BeanFactory extends AbstractRocketMQBeanFactory {

    @Autowired(required = false)
    private ConsumerBeanFactory consumerBeanFactory;


    @Autowired(required = false)
    private ProducerBeanFactory producerBeanFactory;

    @Autowired(required = false)
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

    }

    @Override
    public void build() {

    }


    @Override
    protected void checkConsumer() {


    }

    @Override
    protected void checkProducer() {

    }

    @Override
    protected void buildComsumer() {

    }

    @Override
    protected void buildProducer() {

    }
}
