package com.michaeljou.starter.mq.config;

import com.michaeljou.starter.mq.factory.bean.BeanFactory;
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
@ConditionalOnBean({MQBaseAutoConfiguration.class,BeanFactory.class})
public class MQConsumerAndProducerAutoConfiguration extends MQBaseAutoConfiguration {

    @Autowired(required = false)
    private BeanFactory beanFactory;


    @PostConstruct
    public void init() throws Exception {
        if (beanFactory == null) {
            if (log.isDebugEnabled()) {
                log.debug("初始化数据");
            }
            beanFactory = new BeanFactory(applicationContext,mqProperties);
        }

        beanFactory.build();
    }


}
