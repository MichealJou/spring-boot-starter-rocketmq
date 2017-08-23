package com.michaeljou.starter.mq.config;

import com.alibaba.rocketmq.common.MixAll;
import com.alibaba.rocketmq.remoting.common.RemotingUtil;
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
    private String nameServerAddress = System.getProperty(MixAll.NAMESRV_ADDR_PROPERTY, System.getenv(MixAll.NAMESRV_ADDR_ENV));
    ;
    /**
     * 生产者的组名。
     */
    private String producerGroup;
    //TBW102 默认主题
    private String createTopicKey = MixAll.DEFAULT_TOPIC;
    ;
    ////默认topic对了数量
    private volatile int defaultTopicQueueNums = 4;
    // 发送消息超时时间，默认为3秒
    private int sendMsgTimeout = 3000;
    //压缩
    private int compressMsgBodyOverHowmuch = 1024 * 4;
    ;
    private int retryTimesWhenSendFailed = 2;
    //发送重试次数
    private int retryTimesWhenSendAsyncFailed = 2;
    //消息没有存储成功是否发送到另外一个broker. 默认false
    private boolean retryAnotherBrokerWhenNotStoreOK = false;
    //最大msgBody长度 默认1024 * 128
    private int maxMessageSize = 1024 * 128;

    private String instanceName = System.getProperty("rocketmq.client.name", "DEFAULT");

    private String clientIP = RemotingUtil.getLocalAddress();

    private boolean vipChannelEnabled = Boolean.parseBoolean(System.getProperty(SendMessageWithVIPChannelProperty, "true"));

    public static final String SendMessageWithVIPChannelProperty = "com.rocketmq.sendMessageWithVIPChannel";
    /**
     * 轮询时间
     * 默认情况下，消费者每隔30秒从nameserver获取所有topic的最新队列情况，
     * 这意味着某个broker如果宕机，客户端最多要30秒才能感知。
     * 该时间由DefaultMQPushConsumer的pollNameServerInteval参数决定，可手动配置。
     */
    private int pollNameServerInteval = 1000 * 30;

    /**
     * 与broker关系
     连接
     单个消费者和该消费者关联的所有broker保持长连接。
     心跳
     默认情况下，消费者每隔30秒向所有broker发送心跳，该时间由DefaultMQPushConsumer的heartbeatBrokerInterval参数决定，
     可手动配置。broker每隔10秒钟（此时间无法更改），扫描所有还存活的连接，
     若某个连接2分钟内（当前时间与最后更新时间差值超过2分钟，此时间无法更改）
     没有发送心跳数据，则关闭连接，并向该消费者分组的所有消费者发出通知，分组内消费者重新分配队列继续消费
     断开
     时机：消费者挂掉；心跳超时导致broker主动关闭连接
     动作：一旦连接断开，broker会立即感知到，并向该消费者分组的所有消费者发出通知，分组内消费者重新分配队列继续消费
     */
    private int heartbeatBrokerInterval = 1000 * 30;


    private int persistConsumerOffsetInterval = 1000 * 5;

    /**
     * Minimum consumer thread number
     */
    private int consumeThreadMin = 20;
    /**
     * Max consumer thread number
     */
    private int consumeThreadMax = 64;

    /**
     * Threshold for dynamic adjustment of the number of thread pool
     */
    private long adjustThreadPoolNumsThreshold = 100000;

    /**
     * Concurrently max span offset.it has no effect on sequential consumption
     */
    private int consumeConcurrentlyMaxSpan = 2000;
    /**
     * Flow control threshold
     */
    private int pullThresholdForQueue = 1000;
    /**
     * Message pull Interval
     */
    private long pullInterval = 0;
    /**
     * Batch consumption size
     */
    private int consumeMessageBatchMaxSize = 1;
    /**
     * Batch pull size
     */
    private int pullBatchSize = 32;

    /**
     * Whether update subscription relationship when every pull
     */
    private boolean postSubscriptionWhenPull = false;


    private int maxReconsumeTimes = 16;
    private long suspendCurrentQueueTimeMillis = 1000;
    private long consumeTimeout = 15;
}
