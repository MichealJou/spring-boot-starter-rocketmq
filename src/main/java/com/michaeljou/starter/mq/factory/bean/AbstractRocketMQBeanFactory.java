package com.michaeljou.starter.mq.factory.bean;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.michaeljou.starter.mq.config.MQProperties;
import org.springframework.context.ApplicationContext;

/**
 * Created by Michaeljou 2017/8/23.
 * 构建消费者 和 生成者bean 抽象工厂
 */
public abstract class AbstractRocketMQBeanFactory {



    protected ApplicationContext applicationContext;

    /**
     * 构建生成者和消费者
     */
    public abstract void build();

    /**
     * 检查消费
     */
    protected abstract void checkConsumer();


    /**
     * 检查生产者
     */
    protected abstract void checkProducer();

    /**
     * 生成消费者
     */
    protected abstract void buildComsumer();

    /**
     * 生产生产者对象
     */
    protected abstract void buildProducer();
}
