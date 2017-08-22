package com.michaeljou.starter.mq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by yipin on 2017/6/28.
 * RocketMQ的配置参数
 */
@Data
@ConfigurationProperties(prefix = "rocketmq")
public class MQProperties {
    /**
     * 服务器对应ip
     */
    private String nameServerAddress;
    /**
     *生产者的组名。
     */
    private String producerGroup;
    private String createTopicKey;
    private volatile int defaultTopicQueueNums;
   // 发送消息超时时间，默认为3秒
    private int sendMsgTimeout;
    private int compressMsgBodyOverHowmuch;
    private int retryTimesWhenSendFailed;
    private int retryTimesWhenSendAsyncFailed;
    //消息没有存储成功是否发送到另外一个broker.
    private boolean retryAnotherBrokerWhenNotStoreOK;
    private int maxMessageSize;
}
