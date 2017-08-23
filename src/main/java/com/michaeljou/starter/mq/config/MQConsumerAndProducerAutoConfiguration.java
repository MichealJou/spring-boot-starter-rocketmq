package com.michaeljou.starter.mq.config;

import com.michaeljou.starter.mq.factory.bean.BeanFactory;
import com.michaeljou.starter.mq.factory.bean.ConsumerBeanFactory;
import com.michaeljou.starter.mq.factory.bean.ProducerBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by lenovo on 2017/8/23.
 */
@Slf4j
@Configuration
@ConditionalOnBean({MQBaseAutoConfiguration.class})
public class MQConsumerAndProducerAutoConfiguration extends MQBaseAutoConfiguration {

    private BeanFactory beanFactory;


    @PostConstruct
    public void init() throws Exception {
        log.info("初始化: MQConsumerAndProducerAutoConfiguration 开始");
        if (beanFactory == null) {
            if (log.isDebugEnabled()) {
                log.debug("初始化数据");
            }
            beanFactory = new BeanFactory(applicationContext, mqProperties);
        }

        beanFactory.build();
        log.info("初始化: MQConsumerAndProducerAutoConfiguration 结束");
    }


}
